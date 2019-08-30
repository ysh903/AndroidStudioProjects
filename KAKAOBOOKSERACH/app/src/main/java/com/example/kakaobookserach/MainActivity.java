package com.example.kakaobookserach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText)findViewById(R.id.keywordEditText);
        Button searchBtn = (Button)findViewById(R.id.searchBtn);
        // anonymous inner class를 이용한 Event 처리
        // 안드로이드의 전형적인 이벤트 처리방식
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼을 눌렀을 때 서비스를 생성하고 실행
                Intent i = new Intent();
                //명시적 인텐트를 사용
                ComponentName cname = new ComponentName("com.example.kakaobookserach"
                        ,"com.example.kakaobookserach.KAKAOBookSearchService");
                i.setComponent(cname);
                i.putExtra("searchKeyword",editText.getText().toString());
                startService(i);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i("KAKAOBOOKLog","데이터가 정상적으로 Activity에 전달");
        //intent에서 데이터를 추출해서 ListView에 작업을 진행
        //만약 그림까지 포함하려면 추가작업 필요.(CustomListView)

        ListView customLv = (ListView)findViewById(R.id.bookListView);

        ArrayList<KAKAOBookVO> list = intent.getParcelableArrayListExtra("resultData");
        for(KAKAOBookVO vo : list){
            Log.i("KAKAOBOOKLog",vo.getTitle());
        }
        KAKAOBOOKAdapter adapter = new KAKAOBOOKAdapter();

        customLv.setAdapter(adapter);
        // adapter에 그려야하는 데이터를 세팅
        for(KAKAOBookVO vo : list){
            adapter.addItem(vo);

        }
    }
}
