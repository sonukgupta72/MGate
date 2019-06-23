package com.sonukgupta72.mygate;

public class UserData {

    String name;
    String uniqId;
    String filePath;

    public UserData () {

    }

    public UserData (String name, String uniqId) {
        this.name = name;
        this.uniqId = uniqId;
    }

    public UserData (String name, String uniqId, String imgPath) {

        this.name = name;
        this.uniqId = uniqId;
        this.filePath = imgPath;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
