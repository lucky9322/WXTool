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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zdd.recyclerview_lib.adapter.expand.StickyRecyclerHeadersDecoration;
import com.zdd.wxtool.R;
import com.zdd.wxtool.adapter.ContactAdapter;
import com.zdd.wxtool.model.ContactModel;
import com.zdd.wxtool.pinyin.CharacterParser;
import com.zdd.wxtool.pinyin.PinyinComparator;
import com.zdd.wxtool.util.LogUtils;
import com.zdd.wxtool.widget.DividerDecoration;
import com.zdd.wxtool.widget.SideBar;
import com.zdd.wxtool.widget.TouchableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ContactAdapter.DeleteItemCallback{
    private static final String TAG = "MainActivity";
    @BindView(R.id.list_content)
    TouchableRecyclerView mRecyclerView;
    @BindView(R.id.contact_dialog)
    TextView mContactDialog;
    @BindView(R.id.contact_sidebar)
    SideBar mContactSidebar;

    private List<ContactModel.MembersEntity> mMembers = new ArrayList<>();
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    ContactModel mModel;
    private ContactAdapter   mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        getNetData();
    }

    public void getNetData() {

        //id 已经被处理过
        String tempData = "{\"groupName\":\"中国\",\"admins\":[{\"id\":\"111221\",\"username\":\"程景瑞\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"钱黛\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"许勤颖\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"孙顺元\",\"profession\":\"teacher\"},{\"id\":\"fcd1feb5db2\",\"username\":\"朱佳\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"李茂\",\"profession\":\"teacher\"},{\"id\":\"d1feb5db2\",\"username\":\"周莺\",\"profession\":\"teacher\"},{\"id\":\"cd1feb5db2\",\"username\":\"任倩栋\",\"profession\":\"teacher\"},{\"id\":\"d1feb5db2\",\"username\":\"严庆佳\",\"profession\":\"teacher\"}],\"members\":[{\"id\":\"d1feb5db2\",\"username\":\"彭怡1\",\"profession\":\"student\"},{\"id\":\"d1feb5db2\",\"username\":\"方谦\",\"profession\":\"student\"},{\"id\":\"dd2feb5db2\",\"username\":\"谢鸣瑾\",\"profession\":\"student\"},{\"id\":\"dd2478fb5db2\",\"username\":\"孔秋\",\"profession\":\"student\"},{\"id\":\"dd24cd1feb5db2\",\"username\":\"曹莺安\",\"profession\":\"student\"},{\"id\":\"dd2478eb5db2\",\"username\":\"酆有松\",\"profession\":\"student\"},{\"id\":\"dd2478b5db2\",\"username\":\"姜莺岩\",\"profession\":\"student\"},{\"id\":\"dd2eb5db2\",\"username\":\"谢之轮\",\"profession\":\"student\"},{\"id\":\"dd2eb5db2\",\"username\":\"钱固茂\",\"profession\":\"student\"},{\"id\":\"dd2d1feb5db2\",\"username\":\"潘浩\",\"profession\":\"student\"},{\"id\":\"dd24ab5db2\",\"username\":\"花裕彪\",\"profession\":\"student\"},{\"id\":\"dd24ab5db2\",\"username\":\"史厚婉\",\"profession\":\"student\"},{\"id\":\"dd24a00d1feb5db2\",\"username\":\"陶信勤\",\"profession\":\"student\"},{\"id\":\"dd24a5db2\",\"username\":\"水天固\",\"profession\":\"student\"},{\"id\":\"dd24a5db2\",\"username\":\"柳莎婷\",\"profession\":\"student\"},{\"id\":\"dd2d1feb5db2\",\"username\":\"冯茜\",\"profession\":\"student\"},{\"id\":\"dd24a0eb5db2\",\"username\":\"吕言栋\",\"profession\":\"student\"}],\"creater\":{\"id\":\"1\",\"username\":\"褚奇清\",\"profession\":\"teacher\"}}";

        try {
            Gson gson = new GsonBuilder().create();
            mModel = gson.fromJson(tempData, ContactModel.class);
            setUI(mModel);
        } catch (Exception e) {

        }


    }

    private void setUI(ContactModel mModel) {

        for (int i = 0; i < mModel.getMembers().size(); i++) {
            ContactModel.MembersEntity entity = new ContactModel.MembersEntity();
            entity.setId(mModel.getMembers().get(i).getId());
            entity.setUsername(mModel.getMembers().get(i).getUsername());
            entity.setProfession(mModel.getMembers().get(i).getProfession());
            String pinyin = characterParser.getSelling(mModel.getMembers().get(i).getUsername());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                entity.setSortLetters(sortString.toUpperCase());
            } else {
                entity.setSortLetters("#");
            }
            mMembers.add(entity);
        }
        Collections.sort(mMembers, pinyinComparator);

        if (mAdapter == null) {
            mAdapter = new ContactAdapter(this, mMembers);
            int orientation = LinearLayoutManager.VERTICAL;
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(mAdapter);
            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            mRecyclerView.addItemDecoration(headersDecor);
            mRecyclerView.addItemDecoration(new DividerDecoration(this));

            //   setTouchHelper();
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecor.invalidateHeaders();
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
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
        LogUtils.i(TAG, "select ring");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SMS_RINGTONE_PICKED:
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = RingtoneManager.getRingtone(MainActivity.this, pickedUri);
                String strRingtone = ringtone.getTitle(MainActivity.this);
                LogUtils.i(TAG, pickedUri);
                LogUtils.i(TAG, strRingtone);
                break;
        }
    }

    @Override
    public void deletePosition(int position) {

    }
}
