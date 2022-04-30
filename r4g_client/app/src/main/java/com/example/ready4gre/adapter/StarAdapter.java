package com.example.ready4gre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ready4gre.entity.QuantQuestion;
import com.example.ready4gre.entity.QuantTest;
import com.example.ready4gre.R;

import java.util.ArrayList;

public class StarAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<QuantQuestion> questions;

    public StarAdapter(ArrayList<QuantQuestion> listQuestions, Context context){
        inflater = LayoutInflater.from(context);
        questions = listQuestions;
        System.out.println(questions);
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(questions.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_star_item, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.tv_question_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuantQuestion question = questions.get(position);
        holder.id.setText("Quantitive Question ID: " + String.valueOf(question.getId()));
        return convertView;
    }

    private class ViewHolder {
        private TextView id;
    }
}
