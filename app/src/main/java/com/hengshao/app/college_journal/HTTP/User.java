package com.hengshao.app.college_journal.HTTP;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.Model.Datauser;
import com.hengshao.app.college_journal.Model.PutUserSign;
import com.hengshao.app.college_journal.Model.ReturnGetUser;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Heng on 2017/5/31.
 */

public interface User {


    @GET("/user/")
    Call<ReturnGetUser> GetUser(@Header("authority") String authority);


    @POST("/user/")
    Call<String> PostUser(@Body Datauser datauser);

    @PUT("/user/{id}")
    Call<JsonObject> PutUser(@Body PutUserSign putUserSign, @Path("id") int id);

    @PUT("/user/")
    Call<JsonObject> putUserdata(@Body Datauser datauser);

}
