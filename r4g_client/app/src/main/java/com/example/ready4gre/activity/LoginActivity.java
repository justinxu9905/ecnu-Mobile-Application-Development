package com.example.ready4gre.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ready4gre.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private Button btLogin;
    private Button btSignup;
    private EditText etUsername;
    private EditText etPassword;

    private String BASE_LOGIN_URL = "";
    private String LOGIN_URL = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BASE_LOGIN_URL = getResources().getString(R.string.base_url) + "/Login";

        btLogin = findViewById(R.id.btLogin);
        btSignup = findViewById(R.id.btSignup);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGIN_URL = BASE_LOGIN_URL + "?username=" + etUsername.getText() + "&password=" + etPassword.getText();

                FormBody formBody = new FormBody.Builder()
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(LOGIN_URL)
                        .addHeader("Content-Type","application/json;charset=UTF-8")
                        .post(formBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("post失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String msg = null;
                            try {
                                msg = jsonObject.getString("msg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final String finalMsg = msg;
                            if (msg.equals("Login successfully!")) {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, finalMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("user_id", String.valueOf(etUsername.getText()));
                                intent.putExtras(bundle);
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, finalMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
