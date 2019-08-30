package com.example.androidsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.security.Provider;

public class LifeCycleService extends Service {
    public LifeCycleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //3가지 callback method 사용

    @Override
    public void onCreate() {
        super.onCreate();
        //서비스에서 사용할 resource 준비작업
        Log.i("ServiceExam","onCreate() 호출!!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //서비스가 하는 로직처리
        Log.i("ServiceExam","onStartCommand() 호출!!");
        //return super.onStartCommand(intent, flags, startId);

        if(intent==null){
            //강제종료되서 서비스가 재시작된 경우
            Intent resultIntent = new Intent(getApplicationContext(),
                    MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            resultIntent.putExtra("ServiceToActivityData",
                    "새로운 Activity가 생성되었어요!!");
            startActivity(resultIntent);
        }else{
            //Activity가 보낸 데이터를 서비스 받음
            String msg = intent.getExtras().getString("ActivatyToServiceData");
            Log.i("ServiceExam","Activity가 보내준 메세지:"+msg);
            //로직처리가 진행!!=>Activity에게 전달해야 하는 최종결과 데이터
            Intent resultIntent = new Intent(getApplicationContext(),
                    MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            resultIntent.putExtra("ServiceToActivityData",
                    "결과데이터 전달이예요!!");
            startActivity(resultIntent);



        }
        return Service.START_STICKY; //강제종료되면 자동적으로 재시작.


    }

    @Override
    public void onDestroy() {
        //리소스 해제 작업
        super.onDestroy();
        Log.i("ServiceExam","onDestroy() 호출!!");
    }
}
