
package com.zdd.wxtool.model;


import com.zdd.wxtool.widget.Indexable;

import java.util.ArrayList;
import java.util.List;


public class ContactModel {


    private List<MembersEntity> members = new ArrayList<>();

    public void setMembers(List<MembersEntity> members) {
        this.members = members;
    }

    public List<MembersEntity> getMembers() {
        return members;
    }


    public static class MembersEntity implements Indexable {

        private String id;

        private String username;

        private String profession;

        public String getSortLetters() {
            return sortLetters;
        }

        @Override
        public String getIndex() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        private String sortLetters;

        public void setId(String id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getProfession() {
            return profession;
        }
    }
}
