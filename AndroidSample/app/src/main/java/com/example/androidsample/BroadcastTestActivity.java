package com.example.androidsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BroadcastTestActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_test);

        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Broadcast Receiver를 생성
                //먼저 Broadcast Receiver가 어떤 Broadcast를 청취할 수 있는지를 나타내는
                //intent filter를 생성

                IntentFilter filter = new IntentFilter();
                filter.addAction("MY_BROADCAST");
                //안드로이드 시스템에서 나오는 여러가지 정해져있는 Broadcast를 catch할 수 있음!


                //안드로이드의 3번째 component인 Broadcast receiver를 생성
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        //broadcast를 잡았을 때 처리해야 할 코드 작성
                        //Toast.makeText(context,"신호 캐치!!",
                          //      Toast.LENGTH_SHORT).show();

                        //Notification

                        //Notification MGR 객체를 획득
                        NotificationManager nManager
                                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        //channelID와 channelName, Notification중요도 설정
                        String channelID = "MY_CHANNEL_ID";
                        String channelName = "MY_CHANNEL_NAME";
                        int important = NotificationManager.IMPORTANCE_HIGH;

                        //Oreo버전 이상에서는 채널 객체를 생성해서 설정해야 함
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            NotificationChannel channel=
                                    new NotificationChannel(channelID,channelName,important);
                            nManager.createNotificationChannel(channel);
                        }

                        //Notification
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(context,channelID);

                        //Intent를 하나 생성 => 나중에 notification을 클릭했을 때
                        //화면에 activity를 보여주기 위한 용도

                        Intent nIntent = new Intent(context,BroadcastTestActivity.class);
                        nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        nIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        int requestID = 10;

                        //PendingIntent는 intent를 가지고 있는 Intent
                        //intent의 수행을 지연시키는 역할을 수행
                        PendingIntent pIntent = PendingIntent.getActivity(context,
                                requestID,nIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                        //Notification 설정부분
                        builder.setContentTitle("제목부분이예요!")
                                .setContentText("여기는 내용이 나와요")
                                .setAutoCancel(true) //터치했을 때 사라지도록 처리
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(android.R.drawable.btn_star) //별 모양의 아이콘
                                .setLargeIcon(BitmapFactory.decodeResource(
                                        getResources(),R.drawable.flare)) //비트맵 이미지 변환
                                .setContentIntent(pIntent);

                        //NotificationMgr를 통해서 notification 실행
                        nManager.notify(0,builder.build());

                    }
                };
                //등록작업(리시버가 등록되어야 신호를 잡을 수 있음)
                registerReceiver(receiver,filter);
            }
        });

        Button unregisterBtn = (Button)findViewById(R.id.unregisterBtn);
        unregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼이 클릭되면 receiver의 등록이 해제
                unregisterReceiver(receiver);

            }
        });

        Button sendSignalBtn = (Button)findViewById(R.id.sendSignalBtn);
        sendSignalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction("MY_BROADCAST");
                sendBroadcast(i);
            }
        });
    }
}
