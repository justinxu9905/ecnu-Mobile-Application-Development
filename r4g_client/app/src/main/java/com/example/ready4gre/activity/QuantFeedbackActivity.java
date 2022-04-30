package com.example.ready4gre.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.ready4gre.R;
import com.example.ready4gre.adapter.MyPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuantFeedbackActivity extends Activity {

    private ViewPager vPager;
    private ArrayList<View> vList;
    private MyPagerAdapter mAdapter;

    TextView tvFeedback, tvExit, tvStar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quant_feedback);

        vPager = (ViewPager) findViewById(R.id.viewpager_quant);

        vList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        vList.add(li.inflate(R.layout.view_quant_feedback, null,false));
        for(int i = 0; i < 5; i ++) {
            vList.add(li.inflate(R.layout.view_quant_answer, null,false));
        }

        Intent intent = getIntent();
        String selectedData = intent.getStringExtra("selected");
        List<String> selected = Arrays.asList(selectedData.substring(1, selectedData.length() - 1).split(","));
        for (int i = 0; i < selected.size(); i ++) {
            selected.set(i, selected.get(i).trim());
        }
        System.out.println(selected);
        JSONArray questions = null;
        List<String> optionsList;
        try {
            questions = new JSONArray(intent.getStringExtra("questions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int count = 0, correct = 0;
        for (int i = 0; i < 5; i ++) {
            JSONObject question = null;
            try {
                question = questions.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count ++;
            try {
                if (selected.get(i).equals(question.getString("answer"))) {
                    correct ++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView tvQuestion = vList.get(i + 1).findViewById(R.id.tvQuantQuestion);
            LinearLayout llOptions = vList.get(i + 1).findViewById(R.id.llQuantOptions);
            String options = null;
            try {
                tvQuestion.setText(question.getString("question"));
                options = question.getString("options");
                optionsList = Arrays.asList(options.substring(1, options.length() - 1).split(","));
                for(int j = 0; j < optionsList.size(); j ++) {
                    String option = optionsList.get(j);
                    TextView tv = new TextView(this);
                    tv.setText(option.substring(1, option.length() - 1));
                    if(option.substring(1, option.length() - 1).equals(question.getString("answer"))) {
                        tv.setTextColor(Color.rgb(0, 200, 0));
                    } else if (option.substring(1, option.length() - 1).equals(selected.get(i))) {
                        tv.setTextColor(Color.rgb(200, 0, 0));
                    }
                    llOptions.addView(tv);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvFeedback = vList.get(0).findViewById(R.id.tvFeedback);
        tvFeedback.setText(correct + "/" + count);

        tvExit = findViewById(R.id.tvExit);
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent2 = new Intent();
                bundle.putString("user_id", getIntent().getStringExtra("user_id"));
                intent2.setClass(QuantFeedbackActivity.this, MainActivity.class);
                intent2.putExtras(bundle);
                startActivity(intent2);
            }
        });



        mAdapter = new MyPagerAdapter(vList);
        vPager.setAdapter(mAdapter);
    }
}
