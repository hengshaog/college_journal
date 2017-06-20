package com.hengshao.app.college_journal.HTTP;

import com.hengshao.app.college_journal.Model.PostUserLogin;
import com.hengshao.app.college_journal.Model.ReturnPostLogin;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Heng on 2017/6/1.
 */

public interface HTTPLogin {

    @POST("/login/")
    retrofit2.Call<ReturnPostLogin> PostLogin(@Body PostUserLogin postUserLogin);
}
