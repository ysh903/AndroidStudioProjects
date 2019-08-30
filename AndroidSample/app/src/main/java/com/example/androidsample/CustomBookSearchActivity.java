package com.example.androidsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class BookSearchRunnable implements Runnable {
    private Handler handler;
    private String keyword;


    BookSearchRunnable(Handler handler,String keyword){
        this.handler = handler;
        this.keyword = keyword;

    }

    @Override
    public void run(){
        String url = "http://70.12.115.80:8080/bookSearch/search?search_keyword=" +keyword;
        try{

            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input = "";
            StringBuffer sb = new StringBuffer();
            while ((input = br.readLine()) != null){
                sb.append(input);

            }
            //서버에 대한 스트림은 종료
            br.close();
            //결과적으로 우리가 얻어낸건..JSON형태의 문자열.
            // JSON 문자열을 원래 객체형태로 복원
            ObjectMapper mapper = new ObjectMapper();
            BookVO[] resultArr = mapper.readValue(sb.toString(),BookVO[].class);

            //UI Thread에게 데이터를 전달하기 전에 화면에 표현할 데이터가 완전히 준비되어야 함

            for(BookVO vo:resultArr){
                vo.drawableFromURL();
            }



            //최종적으로 얻어낸 객체를 UI Thread(Main Thread, Activity Thread)에 전달해야함.
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOKS",resultArr);
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);



            Log.i("customListView",sb.toString());
        }catch (Exception e){
            Log.i("customListView",e.toString());
        }
    }
}

public class CustomBookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_book_search);

        // 필요한 reference 얻어오기
        // Button, EditText,ListView

        final Button customBtn = (Button)findViewById(R.id.customBtn);
        final EditText customEt = (EditText)findViewById(R.id.customEt);
        final ListView customLv = (ListView)findViewById(R.id.customLv);


        final Handler handler = new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                // 외부 Thread에서 메세지를 받으면 호출
                Bundle bundle = msg.getData();
                BookVO[] list = (BookVO[])bundle.getSerializable("BOOKS");
                //listview에 adapter를 적용해서 ListView를 그리는 작업
                //ListView: 화면에 리스트형태로 보여주는 Widget
                // ListView에 데이터를 가져다가 실제 그려주는 작업=>adapter
                for(BookVO vo : list){
                    Log.i("qwer",vo.getBtitle());
                }

                CustomListViewAdapter adapter = new CustomListViewAdapter();

                customLv.setAdapter(adapter);

                //adapter에 그려야 하는 데이터 세팅
                for(BookVO vo : list){
                    adapter.addItem(vo);
                }




            }
        };


        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookSearchRunnable runnable = new BookSearchRunnable(handler,
                        customEt.getText().toString());
                Thread t = new Thread(runnable);
                t.start();
            }
        });
    }
}
