package com.example.androidsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);

        final TextView tv = (TextView)findViewById(R.id.contactTv);
        Button contactBtn = (Button)findViewById(R.id.contactBtn);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //보안 관련 코드가 나옴
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //6버전보다 높은 경우
                    if(checkSelfPermission(Manifest.permission.READ_CONTACTS)!=
                            PackageManager.PERMISSION_GRANTED){
                        //이전에 허용을 한적이 없을 경우
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                1111);
                    }else{
                        //이전에 이미 허용한 경우
                        //사용자의 허가를 얻어야함


                        ContentResolver cr = getContentResolver();
                        Uri uri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                        Cursor c = cr.query(uri,null,null,null,null);
                        String result ="";
                        while(c.moveToNext()) {
                            result+=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            result+=",";
                            result+=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            result+="\n";
                        }
                        TextView tv = (TextView)findViewById(R.id.contactTv);
                        tv.setText(result);
                    }
                }else{

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1111){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //사용자가 주소록 접근에 대한 권한 요청 허용을 누르면
                //ContentResolver를 이용해서 주소록에 접근
                ContentResolver cr = getContentResolver();
                Uri uri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                Cursor c = cr.query(uri,null,null,null,null);
                String result ="";
                while(c.moveToNext()) {
                    result+=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    result+=",";
                    result+=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    result+="\n";
                }
                TextView tv = (TextView)findViewById(R.id.contactTv);
                tv.setText(result);
            }
        }
    }
}
