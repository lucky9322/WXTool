package com.zdd.wxtool.pinyin;


import com.zdd.wxtool.model.ContactModel;

import java.util.Comparator;


public class PinyinComparator implements Comparator<ContactModel.MembersEntity> {

    public int compare(ContactModel.MembersEntity o1, ContactModel.MembersEntity o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
