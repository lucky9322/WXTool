package com.zdd.wxtool.manager;

import com.zdd.wxtool.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @CreateDate: 2017/4/4 下午4:16
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */

public class ContactPeopleManager {
    private static ContactPeopleManager mManager;
    /**
     * 用于缓存数据库中的数据
     */
    private List<ContactModel> mContactModels;
    /**
     * 存储排序后的所有数据
     */
    private List<ContactModel> sordedContactModels=new ArrayList<>();

    public List<ContactModel> getSordedContactModels() {
        return sordedContactModels;
    }

    public void setSordedContactModels(List<ContactModel> sordedContactModels) {
        sordedContactModels.clear();
        sordedContactModels.addAll(sordedContactModels);
    }

    public List<ContactModel> getMembersEntities() {
        return mContactModels;
    }

    public void setMembersEntities(List<ContactModel> membersEntities) {
        mContactModels = membersEntities;
    }

    public static ContactPeopleManager getInstance() {
        if (null == mManager) {
            synchronized (ContactPeopleManager.class) {
                if (null == mManager) {
                    mManager = new ContactPeopleManager();
                }
            }
        }
        return mManager;
    }
}
