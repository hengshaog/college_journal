package com.hengshao.app.college_journal.Model;

/**
 * Created by Heng on 2017/5/31.
 */

public class ReturnGetUser {
    int count;
    Datauser[] data;

    public void setCount(int count) {
        this.count = count;
    }

    public void setData(Datauser[] data) {
        this.data = data;
    }

    public int getCount() {

        return count;
    }

    public Datauser[] getData() {
        return data;
    }
}
