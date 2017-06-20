package com.hengshao.app.college_journal.Model;

/**
 * Created by Heng on 2017/5/31.
 */

public class Datauser {
    int id;
    String telephone;
    String school;
    String nickname;
    String sex;
    String signature;
    String createtime;
    String password;
    String praise;
    String authority;

    public void setId(int id) {
        this.id = id;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int getId() {

        return id;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getSchool() {
        return school;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSex() {
        return sex;
    }

    public String getSignature() {
        return signature;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getPassword() {
        return password;
    }

    public String getPraise() {
        return praise;
    }

    public String getAuthority() {
        return authority;
    }
}
