
package com.zdd.wxtool.model;


import com.zdd.wxtool.widget.Indexable;

import org.litepal.crud.DataSupport;


public class ContactModel extends DataSupport implements Indexable {




    private String username;
    private String sortLetters;
    private String ringName;
    private String ringUri;

    @Override
    public String getIndex() {
        return sortLetters;
    }

    public String getSortLetters() {
        return sortLetters;
    }


    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingUri() {
        return ringUri;
    }

    public void setRingUri(String ringUri) {
        this.ringUri = ringUri;
    }
}
