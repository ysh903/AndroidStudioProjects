package com.example.kakaobookserach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KAKAOBOOKAdapter extends BaseAdapter {
    private List<KAKAOBookVO> list = new ArrayList<KAKAOBookVO>();
    // 반드시 overiding을 해야하는 method가 존재

    public void addItem(KAKAOBookVO vo) {
        list.add(vo);
    }


    @Override
    public int getCount() {
        // 총 몇개의 component를 그려야 하는지를 return
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        // 몇번쨰 데이터를 화면에 출력할지를 결정
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        // 추력할 View 객체를 생성
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 생성된 View의 객체에 XML layout을 설정
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }
        // 출력할 View Component의 reference를 획득
        ImageView iv = (ImageView)view.findViewById(R.id.customIv);
        TextView tv1 = (TextView)view.findViewById(R.id.customTv1);
        TextView tv2 = (TextView)view.findViewById(R.id.customTv2);

        KAKAOBookVO vo = list.get(i);  // 화면에 출력할 데이터를 가져온다

        // iv.setImageDrawable(vo.getDrawable());
        tv1.setText(vo.getTitle());
        tv2.setText(vo.getAuthors().toString());

        return view;
    }
}