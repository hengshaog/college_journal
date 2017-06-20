package com.hengshao.app.college_journal.Model;

/**
 * Created by Heng on 2017/5/31.
 */

public class Data {
    int id;
    String title;
    String school;
    String createtime;
    int userid;
    String usernickname;
    String usertelephone;
    String content;
    String praise;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }

    public void setUsertelephone(String usertelephone) {
        this.usertelephone = usertelephone;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public int getId() {

        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSchool() {
        return school;
    }

    public String getCreatetime() {
        return createtime;
    }

    public int getUserid() {
        return userid;
    }

    public String getUsernickname() {
        return usernickname;
    }

    public String getUsertelephone() {
        return usertelephone;
    }

    public String getContent() {
        return content;
    }

    public String getPraise() {
        return praise;
    }
}
