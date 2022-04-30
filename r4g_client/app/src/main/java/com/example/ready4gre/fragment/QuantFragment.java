package com.example.ready4gre.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.ready4gre.R;
import com.example.ready4gre.activity.QuantTestActivity;
import com.example.ready4gre.adapter.QuantAdapter;
import com.example.ready4gre.entity.QuantTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuantFragment extends Fragment {

    private ListView listView;
    private QuantAdapter quantAdapter;
    JSONArray testsData = null;
    private ArrayList<QuantTest> testList = new ArrayList<QuantTest>();

    private String TEST_URL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_quant, null);

        TEST_URL = getResources().getString(R.string.base_url) + "/AllQuantTest";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(TEST_URL)
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

                    try {
                        testsData = new JSONArray(jsonObject.getString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for(int i = 0; i < testsData.length(); i ++) {
                        try {
                            JSONObject jobj = testsData.getJSONObject(i);
                            QuantTest qtmp = new QuantTest();
                            qtmp.setId(Integer.valueOf(jobj.getString("id")));
                            qtmp.setName(jobj.getString("name"));
                            testList.add(qtmp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        listView = rootView.findViewById(R.id.quantList);
        listView.setAdapter(quantAdapter = new QuantAdapter(testList, getActivity()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                try {
                    bundle.putString("questions", testsData.getJSONObject(position).getString("questions"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString("user_id", getActivity().getIntent().getStringExtra("user_id"));
                bundle.putString("test_id", String.valueOf(testList.get(position).getId()));
                //System.out.println(getActivity().getIntent().getStringExtra("user_id"));

                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), QuantTestActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
