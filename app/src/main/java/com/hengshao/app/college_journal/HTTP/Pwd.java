package com.hengshao.app.college_journal.HTTP;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Heng on 2017/6/5.
 */

public interface Pwd {

    @PUT("/pwd/{pwd}/{id}")
    Call<JsonObject> PutPwd(@Path("pwd") String pwd,@Path("id") int id);
}
