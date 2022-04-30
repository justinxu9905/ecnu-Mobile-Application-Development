package com.example.ready4gre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ready4gre.entity.QuantTest;
import com.example.ready4gre.R;

import java.util.ArrayList;

public class QuantAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<QuantTest> tests;

    public QuantAdapter(ArrayList<QuantTest> testList, Context context){
        inflater = LayoutInflater.from(context);
        tests = testList;//new ArrayList<QuantTest>();
        //initData();
    }

    /*private void initData(){
        for(int i = 0; i < 5; i++){
            //tests.add(testList.get(i));
            QuantTest test = new QuantTest();
            tests.add(test);
        }
    }*/

    @Override
    public int getCount() {
        return tests.size();
    }

    @Override
    public Object getItem(int position) {
        return tests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tests.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_quant_item, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.tv_id);
            //holder.like = (TextView) convertView.findViewById(R.id.tv_like);
            //holder.comment = (TextView) convertView.findViewById(R.id.tv_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuantTest test = tests.get(position);
        holder.id.setText(test.getName());
        //holder.like.setText("99");
        //holder.comment.setText("98");
        return convertView;
    }

    private class ViewHolder {
        private TextView id;
        //private TextView like;
        //private TextView comment;
    }
}
