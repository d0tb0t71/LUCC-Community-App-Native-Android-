package com.shieldsoft.lucc_community.models;

public class ContestModel {
    public final String name;
    public final String startTime;
    public final String url;
    public final String duration;

    public ContestModel(String name, String url, String startTime, String duration) {
        this.name = name;
        this.url = url;
        this.startTime = startTime;
        this.duration = duration;

    }

}

