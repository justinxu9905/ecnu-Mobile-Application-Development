package com.example.ready4gre.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ready4gre.R;


public class QuantQuestionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.view_quant_question, null);

        return rootView;
    }
}
