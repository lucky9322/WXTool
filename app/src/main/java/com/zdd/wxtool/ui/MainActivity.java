package com.zdd.wxtool.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zdd.recyclerview_lib.adapter.expand.StickyRecyclerHeadersDecoration;
import com.zdd.wxtool.R;
import com.zdd.wxtool.adapter.ContactAdapter;
import com.zdd.wxtool.manager.ContactPeopleManager;
import com.zdd.wxtool.model.ContactModel;
import com.zdd.wxtool.pinyin.CharacterParser;
import com.zdd.wxtool.pinyin.PinyinComparator;
import com.zdd.wxtool.util.ToastUtils;
import com.zdd.wxtool.widget.DividerDecoration;
import com.zdd.wxtool.widget.SideBar;
import com.zdd.wxtool.widget.TouchableRecyclerView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        ContactAdapter.DeleteItemCallback, View.OnClickListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.list_content)
    TouchableRecyclerView mRecyclerView;
    @BindView(R.id.contact_dialog)
    TextView mContactDialog;
    @BindView(R.id.contact_sidebar)
    SideBar mContactSidebar;
    @BindView(R.id.main_add)
    Button mMainAdd;
    @BindView(R.id.main_add_name)
    EditText mMainAddName;

    private List<ContactModel> mMembers = new ArrayList<>();
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    private ContactAdapter mAdapter;
    private ContactModel itemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        setUI();
    }

    private void initView() {
        ButterKnife.bind(this);
        mMainAdd.setOnClickListener(this);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

    }

    private void setUI() {
        List<ContactModel> models = ContactPeopleManager.
                getInstance().getMembersEntities();
        for (int i = 0; i < models.size(); i++) {
            ContactModel entity = new ContactModel();
            entity.setUsername(models.get(i).getUsername());
            String pinyin = characterParser.getSelling(models.get(i).getUsername());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                entity.setSortLetters(sortString.toUpperCase());
            } else {
                entity.setSortLetters("#");
            }
            mMembers.add(entity);
        }
        Collections.sort(mMembers, pinyinComparator);

        mAdapter = new ContactAdapter(this, mMembers);
        mAdapter.setDeleteItemCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);
        mRecyclerView.addItemDecoration(new DividerDecoration(this));

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
    }


    private String notificationStr;
    public static final String NOTIFICATION_RINGTONE = "pref_notification_ringtone";
    private static final int SMS_RINGTONE_PICKED = 1;

    private void doPickSmsRingtone() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationStr = sharedPreferences.getString(NOTIFICATION_RINGTONE, null);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        // Allow user to pick 'Default'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        // Show only ringtones
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        //set the default Notification value
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        // Don't show 'Silent'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);

        Uri notificationUri;
        if (notificationStr != null) {
            notificationUri = Uri.parse(notificationStr);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);
        } else {
            // Otherwise pick default ringtone Uri so that something is selected.
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);
        }

        // Launch!
        startActivityForResult(intent, SMS_RINGTONE_PICKED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            itemModel = null;
            return;
        }
        switch (requestCode) {
            case SMS_RINGTONE_PICKED:
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = RingtoneManager.getRingtone(MainActivity.this, pickedUri);
                String strRingtone = ringtone.getTitle(MainActivity.this);
                itemModel.setRingName(strRingtone);
                itemModel.setRingUri(pickedUri.toString());
                itemModel.save();

                ContactPeopleManager.getInstance().getMembersEntities().add(itemModel);
                mMembers.add(itemModel);
                Collections.sort(mMembers, pinyinComparator);
                mAdapter.add(mMembers.indexOf(itemModel), itemModel);
                itemModel = null;
                break;
        }
    }

    @Override
    public void deletePosition(int position) {
        String username = mMembers.get(position).getUsername();
        mAdapter.remove(mMembers.get(position));

        DataSupport.deleteAll(ContactModel.class, "username = ?", username);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_add:
                if (TextUtils.isEmpty(mMainAddName.getText().toString())) {
                    ToastUtils.makeText(MainActivity.this,
                            getResources().getString(R.string.content_name_empty),
                            ToastUtils.LENGTH_SHORT).show();
                    return;
                }

                itemModel = new ContactModel();
                itemModel.setUsername(mMainAddName.getText().toString().trim());

                String pinyin = characterParser.getSelling(itemModel.getUsername());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                if (sortString.matches("[A-Z]")) {
                    itemModel.setSortLetters(sortString.toUpperCase());
                } else {
                    itemModel.setSortLetters("#");
                }

                doPickSmsRingtone();
                break;
        }
    }
}
