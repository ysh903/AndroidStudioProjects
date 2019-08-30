package com.example.kakaobookserach;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class KAKAOBookSearchService extends Service {

    //inner class 형태로 Thread를 생성하기 위한
    // Runnable interface를 구현한 class 정의

    private class BookSearchRunnable implements Runnable{
        private String keyword;

        BookSearchRunnable(String keyword){
            this.keyword=keyword;
        }

        @Override
        public void run() {
            //전달된 keyword를 이용해서 network 처리(API호출)
            String url = "https://dapi.kakao.com/v3/search/book?target=title&query="+ keyword;

            String myKey = "98dc7911d78f9e884fb01df70f8aa3dc";
            try{
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
                //request 방식을 지정
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization","KakaoAK "+myKey);
                //정상적으로 설정을 하면 API호출이 성공하고 결과를 받아 올 수 있음
                //연결 통로(stream)를 통해서 결과를 문자열로 얻어냄
                //기본적인 Stream을 BufferedReader형태로 생성
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream())
                );
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line=br.readLine())!=null){
                    sb.append(line);
                }
                //통로를 닫음
                br.close();
                Log.i("KAKAOBOOKLog",sb.toString());
                //데이터가 JSON형태로 정상적으로 출력되면 외부 API 호출 성공!
                //Jackson library 형태로 이용해서 Json 데이터 처리
                //{documents:[]}
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(sb.toString(),
                        new TypeReference<Map<String,Object>>() {
                        });

                Object obj = map.get("documents");
                String resultJsonData=mapper.writeValueAsString(obj);
                Log.i("KAKAOBOOKLog",resultJsonData);
                //결과적으로 우리가 얻은 데이터의 형태 [{1권의 데이터},{},{}]
                //책 1권의 데이터를 객체화=>KAKAOBookVO class를 이용
                //책 여러권의 데이터는 ArrayList로 표현
                //책 1권의 데이터는 key와 value의 쌍으로 표현되고 있음.
                ArrayList<KAKAOBookVO> myObject =
                        mapper.readValue(resultJsonData,
                                new TypeReference<ArrayList<KAKAOBookVO>>() {});
                for(KAKAOBookVO book:myObject){
                    Log.i("KAKAOBOOKLog",book.getTitle());

                }
                //정상적으로 객체화가 되었음면 Intent에 해당 데이터를 붙여서 Activity에 전달
                Intent i = new Intent(getApplicationContext(),MainActivity.class);

                //만약 Activity가 메모리에 존재하면 새로 생성하지 않고 기존 것 사용.
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //전달할 데이터를 intent에 붙일것
                //parcelable를 다른 method로 교체
                i.putParcelableArrayListExtra("resultData",myObject);

                //Activity에게 데이터를 전달
                startActivity(i);






           }catch (Exception e){
                Log.i("KAKAOBOOKLog",e.toString());
            }

        }
    }




    public KAKAOBookSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //서비스 객체가 만들어지는 시점에 1번 호출
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //on Create후에 자동적으로 호출되며
        //StartService()에 의해서호출!!
        //실제 로직처리는 onstartCommand 에서 진행
        Log.i("KAKAOBOOKLog","onStartCommand 호출됨!");
        //전달된 키워드를 이용해서 외부 네트워크 접속을 위한 Thread를 하나 생성해야함
        String keyword = intent.getExtras().getString("searchKeyword");
        //Thread를 만들기 위한 Runnable 객체부터 생성
        BookSearchRunnable runnable= new BookSearchRunnable(keyword);
        Thread t = new Thread(runnable);
        t.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //서비스 객체가 메모리 상에 서 1번 호출
        //사용한 resource를 전리하는 과정
        super.onDestroy();
    }
}
