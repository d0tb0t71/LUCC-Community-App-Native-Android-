package com.shieldsoft.lucc_community.models;

public class ModelMeetings {

    String meetingTitle,meetingDescription,authorID,authorName,meetingLocation,meetingTime,meetingDate;

    public ModelMeetings() {
    }

    public ModelMeetings(String meetingTitle, String meetingDescription, String authorID, String authorName, String meetingLocation, String meetingTime, String meetingDate) {
        this.meetingTitle = meetingTitle;
        this.meetingDescription = meetingDescription;
        this.authorID = authorID;
        this.authorName = authorName;
        this.meetingLocation = meetingLocation;
        this.meetingTime = meetingTime;
        this.meetingDate = meetingDate;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public String getMeetingDate() {
        return meetingDate;
    }
}
