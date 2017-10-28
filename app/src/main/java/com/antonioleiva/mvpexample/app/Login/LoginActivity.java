/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.antonioleiva.mvpexample.app.Login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antonioleiva.mvpexample.app.R;
import com.antonioleiva.mvpexample.app.info.MainInfo;
import com.antonioleiva.mvpexample.app.main.MainActivity;
import com.antonioleiva.mvpexample.app.model.Model;

import org.apache.commons.io.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.antonioleiva.mvpexample.app.model.Model.decodeSampledBitmapFromFile;

public class LoginActivity extends Activity implements LoginView, View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private Activity activity;
    private EditText edt_firstname;
    private EditText edt_lastName;
    private EditText edt_emailId;
    private EditText edt_ssn ;
    private EditText edt_password;
    private EditText edt_phoneNumber;
    private CheckBox chk_backgroundCheck;
    private CheckBox chk_backgroundunCheck;
    private EditText edt_dateOfBirth;
    private EditText comfirmpass;
    private ImageView imguser;
    String get_check="YES";
    private  File myFile;
    private Uri uri=null;
    Bitmap bitmap1 = null;
    private LoginPresenter presenter;
    public final static int PICK_IMAGE_REQUEST = 1;
    public final static int READ_EXTERNAL_REQUEST  = 2;
    // your authority, must be the same as in your manifest file
    private static final String CAPTURE_IMAGE_FILE_PROVIDER = "your.package.name.fileprovider";
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity=this;
        edt_firstname = (EditText) findViewById(R.id.firstname);
        edt_lastName = (EditText) findViewById(R.id.lastname);
        edt_emailId = (EditText) findViewById(R.id.emailId);
        edt_ssn = (EditText) findViewById(R.id.ssn);
        edt_password = (EditText) findViewById(R.id.pass);
        edt_phoneNumber = (EditText) findViewById(R.id.phone_number);
        chk_backgroundCheck = (CheckBox) findViewById(R.id.chk_yes);
        chk_backgroundunCheck = (CheckBox) findViewById(R.id.chk_no);
        edt_dateOfBirth = (EditText) findViewById(R.id.birthday);
        comfirmpass = (EditText) findViewById(R.id.comfirmpass);
        imguser = (ImageView) findViewById(R.id.imguser);
        findViewById(R.id.btn_login).setOnClickListener(this);
        presenter = new LoginPresenterImpl(this,activity);
        imguser.setOnClickListener(this);
       /* String url=Model.encodeUrl(MainInfo.url_server);
        String api=Model.encodeUrl(MainInfo.MethodDemo);
        Log.d(TAG, "onCreate: api "+api);
        Log.d(TAG, "onCreate: url "+url);*/
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
       Model.showDialog(activity,"Please waiting!","registering",false);
    }

    @Override public void hideProgress() {
        Toast.makeText(activity, "ok xong", Toast.LENGTH_SHORT).show();;
    }

    @Override public void setFistNameError() {
        edt_firstname.setError(getString(R.string.username_error));
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermionAndPickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            chupHinh();
            return;
        }
        // Các bạn nhớ request permison cho các máy M trở lên nhé, k là crash ngay đấy.
        int result = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            chupHinh();
        } else {
            requestPermissions(new String[]{
                    WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode != READ_EXTERNAL_REQUEST) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chupHinh();
        } else {
            Toast.makeText(getApplicationContext(), R.string.permission_denied,
                    Toast.LENGTH_LONG).show();
        }
    }
    public String getRealPathFromURI (Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    @Override public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    @Override public void onClick(View v) {
        if (v.getId()==R.id.imguser){
            requestPermionAndPickImage();
        }else {
            if (bitmap1==null){
                Toast.makeText(activity, "Vui long chup anh dai dien", Toast.LENGTH_SHORT).show();
                return;
            }
            File file =myFile;
            // Khởi tạo RequestBody từ file đã được chọn
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse(myFile.getAbsolutePath()),
                    file);
            // Trong retrofit 2 để upload file ta sử dụng Multipart, khai báo 1 MultipartBody.Part
            // uploaded_file là key mà mình đã định nghĩa trong khi khởi tạo server
            MultipartBody.Part filePart =
                    MultipartBody.Part.createFormData("profileImage", file.getName(), requestBody);

            presenter.validateCredentials(filePart,edt_firstname.getText().toString(),edt_lastName.getText().toString(),
                    edt_emailId.getText().toString(),edt_ssn.getText().toString(),edt_password.getText().toString(),
                    edt_phoneNumber.getText().toString(),get_check,edt_dateOfBirth.getText().toString(),
                    "ANDROID","1");

        }

    }
    String tmpPath = "";
    private void chupHinh() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        File file = Model.createImageFile(activity);
        tmpPath = file.getAbsolutePath();
        Uri photoURI = FileProvider.getUriForFile(activity,getString(R.string.file_provider_authority),file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ChupAnhKhachHangActivity.onActivityResult", "requestCode = " + requestCode);
        try {
            myFile =new File(tmpPath);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                    data.getData() != null ) {
                uri = data.getData();
            }
            if (myFile.exists()) {
                Bitmap bm = decodeSampledBitmapFromFile(myFile.getAbsolutePath(), 640, 480);
                bm = Model.xuly(bm, myFile.getAbsolutePath());
                switch (requestCode) {
                    case 1:
                        bitmap1 = bm;
                        RefeshImage();
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ChupAnhKhachHangActivity.onActivityResult", "Error Message = " + e.getMessage());
        }
    }
    private void RefeshImage() {
        if (bitmap1 != null) {
            imguser.setImageBitmap(scaleDown(bitmap1, 500, true));
        }
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width,
                height, filter);

    }
}
