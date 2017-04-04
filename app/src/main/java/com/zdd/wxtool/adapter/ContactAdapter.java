
package com.zdd.wxtool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdd.recyclerview_lib.adapter.BaseAdapter;
import com.zdd.recyclerview_lib.adapter.expand.StickyRecyclerHeadersAdapter;
import com.zdd.recyclerview_lib.widget.SwipeItemLayout;
import com.zdd.wxtool.R;
import com.zdd.wxtool.model.ContactModel;
import com.zdd.wxtool.widget.IndexAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @CreateDate: 2017/4/4 下午2:37
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 * <p>
 * 根据当前权限进行判断相关的滑动逻辑
 */
public class ContactAdapter extends BaseAdapter<ContactModel.MembersEntity, ContactAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, IndexAdapter {
    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    private List<ContactModel.MembersEntity> mLists;

    private Context mContext;


    public static final String OWNER = "1";
    public static final String CREATER = "1";
    public static final String STUDENT = "student";

    public ContactAdapter(Context ct, List<ContactModel.MembersEntity> mLists) {
        this.mLists = mLists;
        mContext = ct;
        this.addAll(mLists);

    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, final int position) {
        SwipeItemLayout swipeRoot = holder.mRoot;
//        if (getItem(position).getId().equals(OWNER)) {
//            swipeRoot.setSwipeAble(false);
//        } else if (isCreator) {
//            swipeRoot.setSwipeAble(true);
//            swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
//                @Override
//                public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
//                    closeOpenedSwipeItemLayoutWithAnim();
//                    mOpenedSil.add(swipeItemLayout);
//                }
//
//                @Override
//                public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
//                    mOpenedSil.remove(swipeItemLayout);
//                }
//
//                @Override
//                public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
//                    closeOpenedSwipeItemLayoutWithAnim();
//                }
//            });
//            holder.mDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (null != mDeleteItemCallback) {
//                        mDeleteItemCallback.deletePosition(position);
//                    }
//                }
//            });
//        } else {
//            if (mPermission == CommonString.PermissionCode.TEACHER) {
//                if (position != 0) {
//                    if (getItem(position).getProfession().equals(STUDENT)) {
//
//                        swipeRoot.setSwipeAble(true);
//                        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
//                            @Override
//                            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
//                                closeOpenedSwipeItemLayoutWithAnim();
//                                mOpenedSil.add(swipeItemLayout);
//                            }
//
//                            @Override
//                            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
//                                mOpenedSil.remove(swipeItemLayout);
//                            }
//
//                            @Override
//                            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
//                                closeOpenedSwipeItemLayoutWithAnim();
//                            }
//                        });
//                        holder.mDelete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (null != mDeleteItemCallback) {
//                                    mDeleteItemCallback.deletePosition(position);
//                                }
//                            }
//                        });
//                    } else {
//                        swipeRoot.setSwipeAble(false);
//                    }
//                } else {
//                    swipeRoot.setSwipeAble(false);
//                }
//            } else {
//                swipeRoot.setSwipeAble(false);
//            }
//        }
        swipeRoot.setSwipeAble(true);
        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mDeleteItemCallback) {
                    mDeleteItemCallback.deletePosition(position);
                }
            }
        });
        TextView textView = holder.mName;
        textView.setText(getItem(position).getUsername());

    }

    @Override
    public long getHeaderId(int position) {

        return getItem(position).getSortLetters().charAt(0);

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        String showValue = String.valueOf(getItem(position).getSortLetters().charAt(0));
//        if ("$".equals(showValue)) {
//            textView.setText("群主");
//        } else if ("%".equals(showValue)) {
//            textView.setText("系统管理员");
//
//        } else {
//        }
        textView.setText(showValue);

    }


    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mLists.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;

    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public SwipeItemLayout mRoot;
        public TextView mDelete;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.item_contact_title);
            mRoot = (SwipeItemLayout) itemView.findViewById(R.id.item_contact_swipe_root);
            mDelete = (TextView) itemView.findViewById(R.id.item_contact_delete);


        }


    }

    private DeleteItemCallback mDeleteItemCallback;

    public void setDeleteItemCallback(DeleteItemCallback deleteItemCallback) {
        mDeleteItemCallback = deleteItemCallback;
    }

    public interface DeleteItemCallback {
        public void deletePosition(int position);
    }
}