package com.example.smishingdetectionapplication.model;

public class Record {

    private String sender;
    private String url;
    private String percentage;
    private int percentImg;
    private String msg;

    public Record(String sender, String percentage, int percentImg, String msg, String url)
    {
        this.percentImg = percentImg;
        this.sender = sender;
        this.percentage = percentage;
        this.msg = msg;
        this.url = url;
    }

    public Record(String sender,String msg,String url,String percentage){
        this.sender = sender;
        this.percentage = percentage;
        this.msg = msg;
        this.url = url;
    }


    public String getSender() { return sender; }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPercentage() {
        return percentage;
    }
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getPercentImg() { return percentImg; }
    public void setPercentImg(int percentImg) { this.percentImg = percentImg; }

    public String getUrl() { return url; }
    public void setUrl (String url) { this.url = url; }

    public String getMsg() { return msg; }
    public void setMsg (String msg) { this.msg = msg; }
}
