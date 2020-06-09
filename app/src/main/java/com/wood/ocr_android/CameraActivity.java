package com.wood.ocr_android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    private Uri outputFileDir;
    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final String imagePath = Environment.getExternalStorageDirectory().toString()+"/OCRImage";//等於sdcard/OCRImage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkPermission();//執行權限檢查
    }

    private void checkPermission()//權限檢查
    {
        boolean camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        boolean wes = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED;

        if (camera||wes||res||internet)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                                     Manifest.permission.READ_EXTERNAL_STORAGE,
                                         Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                             Manifest.permission.INTERNET}, 1);
        }
        else
        {
            startCameraActivity();//處理照相程序
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED)
        {
            startCameraActivity();//處理照相程序
        }
        else
        {
            finish();
        }
    }

    public void startCameraActivity()//照相程序
    {
        try
        {
            File dir = new File(imagePath);//sdcard/OcrImage

            if(!dir.exists())//如果OcrImage資料夾不存在
            {
                dir.mkdir();//建立一個OcrImage資料夾
            }

            String imageFilePath = imagePath+"/OcrPicture.jpg";//設定ocr圖片檔的位置sdcard/OcrImage/OcrPicture.jpg
            outputFileDir = Uri.fromFile(new File(imageFilePath));//將ocr圖片檔的位置(uri形式)存入outputFileDir

            final Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//調用相機拍照功能

            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileDir);//傳遞outputFileDir。key等於MediaStore.EXTRA_OUTPUT

            if(pictureIntent.resolveActivity(getPackageManager()) != null)//確認Activity是否可收到Intent，不是null等於至少有一個可以
            {
                startActivityForResult(pictureIntent,100);//請求碼數字是自己設定
            }
        }

        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //建立一個intent物件，設置當前CameraActivity將切換到UserActivity.class
        Intent camera_Uri_Intent = new Intent(this,UserActivity.class);
        //另一個寫法：camera_Uri_Intent.setClass(this,UserActivity.class);

        if(requestCode == 100 && resultCode == Activity.RESULT_OK)//如果請求碼等於100，且操作正常RESULT_OK
        {
            camera_Uri_Intent.putExtra("Uri",outputFileDir);//傳遞outputFileDir，key等於Uri
        }

        else//請求碼不對or操作不正常(ex按返回鍵)
        {
            Toast.makeText(getApplicationContext(),"請拍照以辨識文字",Toast.LENGTH_SHORT).show();
        }

        startActivity(camera_Uri_Intent);//切換到UserActivity
        this.finish();//結束CameraActivity
    }
}
