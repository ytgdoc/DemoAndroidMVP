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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antonioleiva.mvpexample.app.R;
import com.antonioleiva.mvpexample.app.main.MainActivity;

public class LoginActivity extends Activity implements LoginView, View.OnClickListener {
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
    String get_check="YES";
    private LoginPresenter presenter;

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
        findViewById(R.id.btn_login).setOnClickListener(this);
        presenter = new LoginPresenterImpl(this,activity);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        Toast.makeText(activity, "vui long cho", Toast.LENGTH_SHORT).show();
    }

    @Override public void hideProgress() {
        Toast.makeText(activity, "ok xong", Toast.LENGTH_SHORT).show();;
    }

    @Override public void setFistNameError() {
        edt_firstname.setError(getString(R.string.username_error));
    }


    @Override public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override public void onClick(View v) {
        presenter.validateCredentials(edt_firstname.getText().toString(),edt_lastName.getText().toString(),
                edt_emailId.getText().toString(),edt_ssn.getText().toString(),edt_password.getText().toString(),
                edt_phoneNumber.getText().toString(),get_check,edt_dateOfBirth.getText().toString(),
                "ANDROID","1");
    }
}
