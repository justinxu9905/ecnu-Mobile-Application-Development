package com.example.ready4gre.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.ready4gre.R;

public class VerbalFragment extends Fragment {
    private TextView tvContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_verbal, null);
        tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        return rootView;
    }
}