package com.shieldsoft.lucc_community.models;

public class ModelPost {

    String authorID,authorName,postTime,woym;

    public ModelPost(String authorID, String authorName, String postTime, String woym) {
        this.authorID = authorID;
        this.authorName = authorName;
        this.postTime = postTime;
        this.woym = woym;
    }

    public ModelPost() {
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getWoym() {
        return woym;
    }

}
