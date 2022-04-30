package com.example.ready4gre.fragment;

import com.example.ready4gre.R;
import com.example.ready4gre.activity.QuantTestActivity;
import com.example.ready4gre.adapter.StarAdapter;
import com.example.ready4gre.entity.QuantQuestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StarFragment extends Fragment {

    private ListView listView;

    private String BASE_MY_STAR_URL = "";
    private String MY_STAR_URL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        final View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_star, null);

        BASE_MY_STAR_URL = getResources().getString(R.string.base_url) + "/MyStar";
        MY_STAR_URL = BASE_MY_STAR_URL + "?user_id=" + getActivity().getIntent().getStringExtra("user_id");

        final ArrayList<QuantQuestion> questions = new ArrayList<QuantQuestion>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(MY_STAR_URL)
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
                    JSONObject data = null;
                    String myStar = null;
                    List<String> listStar = null;
                    try {
                        data = new JSONObject(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        myStar = data.getString("data");
                        listStar = Arrays.asList(myStar.substring(1, myStar.length() - 1).split(","));
                        for (int i = 0; i < listStar.size(); i ++) {
                            QuantQuestion question = new QuantQuestion();
                            question.setId(Integer.valueOf(Arrays.asList(listStar.get(i).substring(1, listStar.get(i).length() - 1).split(":")).get(1)));
                            questions.add(question);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView = rootView.findViewById(R.id.starList);
                            listView.setAdapter(new StarAdapter(questions, getActivity()));
                        }
                    });
                }
            }
        });

        return rootView;
    }
}
