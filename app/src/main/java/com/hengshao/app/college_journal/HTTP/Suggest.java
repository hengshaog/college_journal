package com.hengshao.app.college_journal.HTTP;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.Model.Postsuggest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hengshao on 2017/6/20.
 */

public interface Suggest {

    @POST("/suggest/")
    Call<JsonObject> Postsuggest(@Body Postsuggest postsuggest);
}
