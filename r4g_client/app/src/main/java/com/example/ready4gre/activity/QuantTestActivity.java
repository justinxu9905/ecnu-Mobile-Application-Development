package com.example.ready4gre.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.ready4gre.R;
import com.example.ready4gre.adapter.MyPagerAdapter;
import com.example.ready4gre.entity.QuantTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuantTestActivity extends Activity {

    private ViewPager vPager;
    private ArrayList<View> vList;
    private MyPagerAdapter mAdapter;
    private TextView tvExit, tvCounter, tvStar, tvSubmit;
    private Context thisActivity;

    //private String QUANT_TEST_URL = "http://192.168.1.100:8080/QuantTest";
    private String BASE_STAR_URL = "";
    private String STAR_URL = "";
    private String BASE_UNSTAR_URL = "";
    private String UNSTAR_URL = "";
    private String BASE_SUBMIT_URL = "";
    private String SUBMIT_URL = "";
    private String BASE_STARORNOT_URL = "";
    private String STARORNOT_URL = "";

    private ArrayList<Boolean> staredOrNot;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quant_test);

        thisActivity = this;

        BASE_STAR_URL = getResources().getString(R.string.base_url) + "/StarQuestion";
        BASE_UNSTAR_URL = getResources().getString(R.string.base_url) + "/UnstarQuestion";
        BASE_SUBMIT_URL = getResources().getString(R.string.base_url) + "/SubmitTest";
        BASE_STARORNOT_URL = getResources().getString(R.string.base_url) + "/StaredOrNot";

        staredOrNot = new ArrayList<Boolean>();

        vPager = (ViewPager) findViewById(R.id.viewpager_quant);

        vList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        for(int i = 0; i < 5; i ++) {
            vList.add(li.inflate(R.layout.view_quant_question, null,false));
        }

        intent = getIntent();
        JSONArray questions = null;
        List<String> optionsList;
        RadioGroup rgOptions;
        try {
            questions = new JSONArray(intent.getStringExtra("questions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 5; i++) {
            JSONObject question = null;
            try {
                question = questions.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView tvQuestion = vList.get(i).findViewById(R.id.tvQuantQuestion);
            String options = null;
            try {
                tvQuestion.setText(question.getString("question"));
                options = question.getString("options");
                optionsList = Arrays.asList(options.substring(1, options.length() - 1).split(","));
                rgOptions = vList.get(i).findViewById(R.id.rgQuantOptions);
                for(int j = 0; j < optionsList.size(); j ++) {
                    RadioButton rgOption = new RadioButton(thisActivity);
                    String option = optionsList.get(j);
                    rgOption.setText(option.substring(1, option.length() - 1));
                    rgOptions.addView(rgOption);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                STARORNOT_URL = BASE_STARORNOT_URL + "?user_id=" + intent.getStringExtra("user_id") + "&question_type=" + "quant" + "&question_id=" + question.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FormBody formBody = new FormBody.Builder()
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(STARORNOT_URL)
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
                    String responseData = response.body().string();
                    JSONObject data = null;
                    try {
                        data = new JSONObject(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        staredOrNot.add(data.getBoolean("data"));
                    } catch (JSONException e) {
                        staredOrNot.add(Boolean.FALSE);
                        e.printStackTrace();
                    }
                }
            });
        }

        mAdapter = new MyPagerAdapter(vList);
        vPager.setAdapter(mAdapter);
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (staredOrNot.get(position)) {
                    tvStar.setText("Unstar");
                } else {
                    tvStar.setText("Star");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvExit = (TextView)findViewById(R.id.tvExit);
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuitDialog();
            }
        });

        tvStar = (TextView)findViewById(R.id.tvStar);
        final JSONArray finalQuestions = questions;
        tvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvStar.getText().equals("Star")) {
                    tvStar.setText("Unstar");
                    staredOrNot.set(vPager.getCurrentItem(), Boolean.TRUE);
                    try {
                        STAR_URL = BASE_STAR_URL + "?user_id=" + intent.getStringExtra("user_id") + "&question_type=" + "quant" + "&question_id=" + finalQuestions.getJSONObject(vPager.getCurrentItem()).getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FormBody formBody = new FormBody.Builder()
                            .build();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(STAR_URL)
                            .addHeader("Content-Type","application/json;charset=UTF-8")
                            .post(formBody)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("post失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException { }
                    });
                } else if (tvStar.getText().equals("Unstar")) {
                    tvStar.setText("Star");
                    staredOrNot.set(vPager.getCurrentItem(), Boolean.FALSE);
                    try {
                        UNSTAR_URL = BASE_UNSTAR_URL + "?user_id=" + intent.getStringExtra("user_id") + "&question_type=" + "quant" + "&question_id=" + finalQuestions.getJSONObject(vPager.getCurrentItem()).getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FormBody formBody = new FormBody.Builder()
                            .build();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(UNSTAR_URL)
                            .addHeader("Content-Type","application/json;charset=UTF-8")
                            .post(formBody)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("post失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException { }
                    });
                }
            }
        });

        tvSubmit = (TextView)findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        tvCounter = (TextView)findViewById(R.id.tvCounter);
        new CountDownTimer(600000, 1000) {
            public void onTick(long time) {
                tvCounter.setText(time / 60000 + ":" + time / 1000 % 60);
            }

            public void onFinish() {
                submit();
            }
        }.start();
    }

    private void showQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(QuantTestActivity.this);
        quitDialog.setMessage("Are you sure you want to quit?");
        quitDialog.setPositiveButton("Resume",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ...To-do
                    }
                });
        quitDialog.setNeutralButton("Submit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submit();
                    }
                });
        quitDialog.setNegativeButton("Quit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", intent.getStringExtra("user_id"));
                        Intent intent2 = new Intent();
                        intent2.putExtras(bundle);
                        intent2.setClass(QuantTestActivity.this, MainActivity.class);
                        startActivity(intent2);
                    }
        });
        quitDialog.show();
    }

    private void submit() {
        final ArrayList<String> selected = new ArrayList<String>();
        for (int i = 0; i < 5; i ++) {
            RadioGroup rg = vList.get(i).findViewById(R.id.rgQuantOptions);
            int childCount = rg.getChildCount();
            for (int j = 0; j < childCount; j ++) {
                RadioButton rb = (RadioButton) rg.getChildAt(j);
                if (rb.isChecked()) {
                    selected.add(rb.getText().toString());
                    break;
                }
            }
            if (selected.size() <= i) {
                selected.add(null);
            }
        }

        SUBMIT_URL = BASE_SUBMIT_URL + "?user_id=" + getIntent().getStringExtra("user_id") + "&id=" + getIntent().getStringExtra("test_id") + "&selected=" + selected.toString();
        FormBody formBody = new FormBody.Builder()
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SUBMIT_URL)
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
                Bundle bundle = new Bundle();
                bundle.putString("questions", intent.getStringExtra("questions"));
                bundle.putString("selected", selected.toString());
                bundle.putString("user_id", intent.getStringExtra("user_id"));
                Intent intent2 = new Intent();
                intent2.putExtras(bundle);
                intent2.setClass(QuantTestActivity.this, QuantFeedbackActivity.class);
                startActivity(intent2);
            }
        });
    }
}

