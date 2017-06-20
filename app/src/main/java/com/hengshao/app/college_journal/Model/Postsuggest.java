package com.hengshao.app.college_journal.Model;

/**
 * Created by hengshao on 2017/6/20.
 */

public class Postsuggest {
    int id;
    String content;
    String createtime;
    int userid;
    String school;

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {

        return school;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {

        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public int getUserid() {
        return userid;
    }
}
