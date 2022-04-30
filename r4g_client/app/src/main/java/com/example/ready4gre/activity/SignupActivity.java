package com.example.ready4gre.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class SignupActivity extends Activity {

    private Button btSignup;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;

    private String BASE_SIGNUP_URL = "";
    private String SIGNUP_URL = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        BASE_SIGNUP_URL = getResources().getString(R.string.base_url) + "/Signup";

        btSignup = findViewById(R.id.btSignup);
        etUsername = findViewById(R.id.etSignupUsername);
        etPassword = findViewById(R.id.etSignupPassword);
        etEmail = findViewById(R.id.etSignupEmail);

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SIGNUP_URL = BASE_SIGNUP_URL + "?username=" + etUsername.getText() + "&password=" + etPassword.getText() + "&email=" + etEmail.getText();

                FormBody formBody = new FormBody.Builder()
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(SIGNUP_URL)
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
                            if (msg.equals("Signup successfully!")) {
                                SignupActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignupActivity.this, finalMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("user_id", String.valueOf(etUsername.getText()));
                                intent.putExtras(bundle);
                                intent.setClass(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                SignupActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignupActivity.this, finalMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }
}
