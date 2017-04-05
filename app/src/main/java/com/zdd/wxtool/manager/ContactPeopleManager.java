package com.zdd.wxtool.manager;

import com.zdd.wxtool.model.ContactModel;

import java.util.List;

/**
 * @CreateDate: 2017/4/4 下午4:16
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */

public class ContactPeopleManager {
    private static ContactPeopleManager mManager;
    private List<ContactModel> mContactModels;

    public List<ContactModel> getMembersEntities() {
        return mContactModels;
    }

    public void setMembersEntities(List<ContactModel> membersEntities) {
        mContactModels = membersEntities;
    }

    public static ContactPeopleManager getInstance(){
        if (null==mManager){
            synchronized (ContactPeopleManager.class){
                if (null==mManager){
                    mManager=new ContactPeopleManager();
                }
            }
        }
        return mManager;
    }
}
