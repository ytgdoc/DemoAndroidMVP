package com.antonioleiva.mvpexample.app.service;

import com.antonioleiva.mvpexample.app.info.MainInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @Multipart
    @POST(MainInfo.MethodDemo)
    Call<ServerResponse>CallRegUsers2(
            @Part MultipartBody.Part profileImage,
            @Part("firstName") RequestBody  firstName ,
            @Part("lastName") RequestBody  lastName ,
            @Part("emailId") RequestBody  emailId ,
            @Part("ssn") RequestBody  ssn ,
            @Part("password") RequestBody  password,
            @Part("phoneNumber") RequestBody  phoneNumber,
            @Part("backgroundCheck") RequestBody  backgroundCheck ,
            @Part("dateOfBirth") RequestBody  dateOfBirth ,
            @Part("deviceType") RequestBody  deviceType,
            @Part("deviceToken") RequestBody  deviceToken
    );
//https://stackoverflow.com/questions/40607862/retrofit-throwing-an-exception-java-lang-illegalargumentexception-only-one-en

}