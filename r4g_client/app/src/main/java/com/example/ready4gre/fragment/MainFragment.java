package com.example.ready4gre.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ready4gre.adapter.FragmentAdapter;
import com.example.ready4gre.entity.EstimatedScore;

import com.example.ready4gre.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainFragment extends Fragment {

    private String ES_URL = "";

    private int screenWidth, screenHeight;
    private ViewPager vPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentAdapter adapter;
    private TextView tvQuant, tvVerbal, tvVocab, tvQuantScore, tvVerbalScore;
    private View viewIndicator;
    private  EstimatedScore estimatedScore;

    private int currentIndex = 0;

    private int currentSelectId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getScreenSize(getActivity());

        ES_URL = getResources().getString(R.string.base_url) + "/EstimatedScore?user_id=" + getActivity().getIntent().getStringExtra("user_id");

        View rootView = inflater.inflate(R.layout.fragment_main, null);
        vPager = (ViewPager) rootView.findViewById(R.id.viewpager_home);

        tvQuantScore = (TextView) rootView.findViewById(R.id.quantScoreView);
        tvVerbalScore = (TextView) rootView.findViewById(R.id.verbalScoreView);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ES_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
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

                    JSONObject data = null;
                    try {
                        data = new JSONObject(jsonObject.getString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String quantScore = null, verbalScore = null;
                    try {
                        quantScore = data.getString("quantscore");
                        verbalScore = data.getString("verbalscore");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    tvQuantScore.setText(quantScore);
                    tvVerbalScore.setText(verbalScore);
                }
            }
        });

        QuantFragment quantFragment = new QuantFragment();
        VerbalFragment verbalFragment = new VerbalFragment();
        VocabFragment vocabFragment = new VocabFragment();

        fragmentList.add(quantFragment);
        fragmentList.add(verbalFragment);
        fragmentList.add(vocabFragment);

        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(pageChangeListener);

        tvQuant = (TextView) rootView.findViewById(R.id.tv_quant);
        tvVerbal = (TextView) rootView.findViewById(R.id.tv_verbal);
        tvVocab = (TextView) rootView.findViewById(R.id.tv_vocab);

        tvQuant.setOnClickListener(clickListener);
        tvVerbal.setOnClickListener(clickListener);
        tvVocab.setOnClickListener(clickListener);

        tvQuant.setSelected(true);

        viewIndicator = rootView.findViewById(R.id.view_indicator);

        initCursorPosition();

        return rootView;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if(currentSelectId != v.getId()){//防止重复点击
                switch (v.getId()) {
                    case R.id.tv_quant:
                        vPager.setCurrentItem(0);
                        break;
                    case R.id.tv_verbal:
                        vPager.setCurrentItem(1);
                        break;
                    case R.id.tv_vocab:
                        vPager.setCurrentItem(2);
                        break;
                }
                currentSelectId = v.getId();
            }
        }
    };

    private void initCursorPosition(){
        ViewGroup.LayoutParams layoutParams = viewIndicator.getLayoutParams();
        layoutParams.width = screenWidth / 3;
        viewIndicator.setLayoutParams(layoutParams);

        TranslateAnimation animation = new TranslateAnimation(-screenWidth / 3,0,0,0);
        animation.setFillAfter(true);
        viewIndicator.startAnimation(animation);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index){
            translateAnimation(index); //移动指示器
            changeTextColor(index); //改变文字颜色
            currentIndex = index; //设置当前选中
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    private void changeTextColor(int index){
        tvQuant.setSelected(false);
        tvVerbal.setSelected(false);
        tvVocab.setSelected(false);

        switch (index) {
            case 0:
                tvQuant.setSelected(true);
                break;
            case 1:
                tvVerbal.setSelected(true);
                break;
            case 2:
                tvVocab.setSelected(true);
                break;
        }
    }

    private void translateAnimation(int index){
        TranslateAnimation animation = null;
        switch (index){
            case 0:
                animation = new TranslateAnimation((screenWidth / 3),0,0,0);
                break;
            case 1:
                if(currentIndex == 0){
                    animation = new TranslateAnimation(0,screenWidth / 3,0,0);
                }else if(currentIndex == 2){
                    animation = new TranslateAnimation((screenWidth / 3) * 2,screenWidth / 3,0,0);
                }
                break;
            case 2:
                animation = new TranslateAnimation(screenWidth / 3,(screenWidth / 3) * 2,0,0);
                break;
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        viewIndicator.startAnimation(animation);
    }

    // 获取屏幕宽高
    private void getScreenSize(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
}
