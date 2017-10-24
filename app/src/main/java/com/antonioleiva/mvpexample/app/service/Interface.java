package com.antonioleiva.mvpexample.app.service;

import com.antonioleiva.mvpexample.app.info.MainInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Chungnv on 3/10/2016.
 */
public interface Interface {

    /*
    * Retrofit get annotation with our URL
    * And our method that will return us details of student.
    */
    @FormUrlEncoded
    @POST(MainInfo.MethodDemo)
    Call<ServerResponse>CallRegUsers(
            @Field("firstName") String firstName ,
            @Field("lastName") String lastName ,
            @Field("emailId") String emailId ,
            @Field("ssn") String ssn ,
            @Field("password") String password,
            @Field("phoneNumber") String phoneNumber,
            @Field("backgroundCheck") String backgroundCheck ,
            @Field("dateOfBirth") String dateOfBirth ,
            @Field("deviceType") String deviceType,
            @Field("deviceToken") String deviceToken
          );


}