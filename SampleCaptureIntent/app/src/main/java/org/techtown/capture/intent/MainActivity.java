package org.techtown.capture.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.graphics.Matrix;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    ImageView imageView;
    File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        AutoPermissions.Companion.loadAllPermissions(this,101);


    }

    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);




        if(requestCode==101&&resultCode==RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=8;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);

            imageView.setImageBitmap(bitmap);
        }
    }

    public void takePicture() {
        if (file == null) {
            file = createFile();
        }

        Uri fileUri = FileProvider.getUriForFile(this, "org.techtown.capture.intent.fileprovider", file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 101 );
        }
    }

    private File createFile(){
        String filename="capture.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File outFile=new File(storageDir,filename);

        return outFile;
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[],
                                           int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    public void onDenied(int requestCode, @NonNull  String[] permissions){
        Toast.makeText(this,"permission denied: "+permissions.length,
                Toast.LENGTH_LONG).show();
    }

    public void onGranted(int requestCode,@NonNull String[] permissions){
        Toast.makeText(this,"permissions granted: "+permissions.length,
                Toast.LENGTH_LONG).show();
    }









}


