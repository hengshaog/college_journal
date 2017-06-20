package com.hengshao.app.college_journal.HTTP;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.Model.Postinfo;
import com.hengshao.app.college_journal.Model.ReturnGetInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Heng on 2017/5/31.
 */

public interface Info {

    @GET("/info/")
    Call<ReturnGetInfo> Getinfo();

    @POST("/info/")
    Call<JsonObject> Postinfo(@Body Postinfo postinfo);
}
