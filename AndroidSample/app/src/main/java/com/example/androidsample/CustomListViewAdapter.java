package com.example.androidsample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {

    private List<BookVO> list = new ArrayList<BookVO>();

    // 반드시 overriding을 해야하는 method가 존재

    public void addItem(BookVO vo){
        Log.i("qwer1111","데이터추가");
        list.add(vo);
    }

    @Override
    public int getCount() {
        // 총 몇개의 component를 그려야 하는지를 return
        Log.i("qwer1111","getCount호출!!");
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        //몇번째 데이터를 화면에 출력할지를 결정
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Log.i("qwer1111","이거 수행되나??");
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater =
                    (LayoutInflater) ((Context) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 생성된 View객체에 XML Layout을 설정.
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);

        }
        // 출력할 View Component의 reference를 획득
        ImageView iv = (ImageView)view.findViewById(R.id.customIv);
        TextView tv1 = (TextView)view.findViewById(R.id.customTv1);
        TextView tv2 = (TextView)view.findViewById(R.id.customTv2);

        BookVO vo = list.get(i);    // 화면에 출력할 데이터를 가져와요

        iv.setImageDrawable(vo.getDrawable());
        tv1.setText(vo.getBtitle());
        tv2.setText(vo.getBauthor());

        return view;
    }
}
