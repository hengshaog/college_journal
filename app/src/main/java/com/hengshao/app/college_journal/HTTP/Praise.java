package com.hengshao.app.college_journal.HTTP;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.Model.Postpraise;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by hengshao on 2017/6/20.
 */

public interface Praise {
    @POST("/praise/")
    Call<JsonObject> Postpraise(@Body Postpraise postpraise, @Header("table") String table);
}
