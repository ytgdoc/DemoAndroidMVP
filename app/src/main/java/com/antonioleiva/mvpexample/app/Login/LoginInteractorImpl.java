package com.antonioleiva.mvpexample.app.Login;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.antonioleiva.mvpexample.app.info.MainInfo;
import com.antonioleiva.mvpexample.app.service.Config;
import com.antonioleiva.mvpexample.app.service.Interface;
import com.antonioleiva.mvpexample.app.service.ServerResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginInteractorImpl implements LoginInteractor {

    private static final String TAG = "LoginInteractorImpl";
    @Override
    public void login(final Activity activity, final String firstName, final String lastName,
                      final String emailId , final String ssn, final String password ,
                      final String phoneNumber , final String backgroundCheck,
                      final String dateOfBirth , final String deviceType , final String deviceToken ,
                      final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(firstName)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(lastName)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(emailId)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(ssn)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(backgroundCheck)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(dateOfBirth)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(deviceType)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(deviceToken)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (!error){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new PostRegUser(0,firstName,lastName,emailId,ssn,password,phoneNumber,
                                    backgroundCheck,dateOfBirth
                            ,deviceType,deviceToken,listener).execute();
                        }
                    });
                }
            }
        }, 2000);

    }
    class PostRegUser extends AsyncTask<String, Integer, String> {
        private int loai;
        private String firstName;
        private String lastName;
        private String emailId ;
        private String ssn;
        private String password ;
        private String phoneNumber ;
        private String backgroundCheck;
        private String dateOfBirth ;
        private String deviceType ;
        private String deviceToken ;
        private OnLoginFinishedListener listener;

        public PostRegUser(int loai,final String firstName, final String lastName,
                           final String emailId , final String ssn, final String password ,
                           final String phoneNumber , final String backgroundCheck,
                           final String dateOfBirth , final String deviceType , final String deviceToken ,
                           final OnLoginFinishedListener listener) {
            this.loai = loai;
            this.firstName = firstName;
            this.lastName = lastName;
            this.emailId = emailId;
            this.ssn = ssn;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.backgroundCheck = backgroundCheck;
            this.dateOfBirth = dateOfBirth;
            this.deviceType = deviceType;
            this.deviceToken = deviceToken;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (loai == 0)
                CreateUser(firstName,lastName,emailId,ssn,password,
                        phoneNumber,backgroundCheck,dateOfBirth,deviceType,deviceToken,listener);//create data)
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public void CreateUser(final String firstName, final String lastName,
                           final String emailId , final String ssn, final String password ,
                           final String phoneNumber , final String backgroundCheck,
                           final String dateOfBirth , final String deviceType , final String deviceToken ,
                           final OnLoginFinishedListener listener) {
        new Retrofit.Builder()
                .baseUrl(MainInfo.url_server)
                .build();
        Interface communicatorInterface = ServiceGenerator.createService(Interface.class, Config.YOUR_USERNAME, Config.YOUR_PASSWORD);
        Call<ServerResponse> callback = communicatorInterface.CallRegUsers( firstName,lastName,emailId,
                ssn,password,phoneNumber,backgroundCheck,dateOfBirth,deviceType,deviceToken);
        callback.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    boolean error = response.body().isError();
                    String message = response.body().getMessage();
                    Log.d(TAG, "error " + error + " message " + message);
                    if (error) {
                        listener.onSuccess();
                    } else {
                        listener.onUsernameError();
                    }
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                try {
                    if (t != null) {
                        Log.d(TAG, "onFailure: loi CheckEmail " + t.getMessage());

                    }
                } catch (Exception e) {
                }
            }
        });
    }
    public static class ServiceGenerator {

        public static final String API_BASE_URL =MainInfo.url_server;//db.getUrlServer(1);

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        public static <S> S createService(Class<S> serviceClass) {
            return createService(serviceClass, null, null);
        }

        public static <S> S createService(Class<S> serviceClass, String username, String password) {
            if (username != null && password != null) {
                String credentials = username + ":" + password;
                final String basic =
                        "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", basic)
                                .header("Accept", "application/json")
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
            }

            OkHttpClient client2 = httpClient.build();
            Retrofit retrofit = builder.client(client2).build();
            return retrofit.create(serviceClass);
        }
    }

}
