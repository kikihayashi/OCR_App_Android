package com.wood.ocr_android;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//UserActivity繼承AppCompatActivity實作MyAdapter.RecyclerViewItemClickListener介面
public class UserActivity extends AppCompatActivity implements MyAdapter.RecyclerViewItemClickListener {

<<<<<<< HEAD
    private int i = 1;
=======
>>>>>>> dec68ab38416bf3713be1d1db5bbb769e77d2ca6
    private Ocr ocr;
    private EditText editText;
    private String textDisplay = "文字顯示區";
    private ToolDialog toolDialog;
    private static final String imagePath = Environment.getExternalStorageDirectory().toString()+"/OCRImage";//等於sdcard/OCRImage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //開啟全螢幕
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//設定隱藏APP標題
        setContentView(R.layout.activity_user);

        editText = findViewById(R.id.editText);

        Intent get_Camera_Intent = this.getIntent();//取得傳遞過來的Intent
        Uri uri_camera = get_Camera_Intent.getParcelableExtra("Uri");//取得uri_camera

        if(uri_camera!=null)//如果uri_camera有資料
        {
            ocr = new Ocr(uri_camera, getApplicationContext());//建立Ocr物件
            textDisplay = ocr.getImageString();//得到照片文字
        }

        editText.setText(textDisplay);//將照片文字設置在editText上
        //設置為不可編輯editText
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
    }

    //按鈕區---------------------------------------------------------------------------------------
    public void restartCamera(View view)//重新拍照
    {
        //建立一個temp_intent物件，new Intent(放當前類別, 放要跳轉的類別.class)
        Intent temp_intent = new Intent(this, CameraActivity.class);
        startActivity(temp_intent);//切換到CameraActivity
        this.finish();//結束UserActivity
    }

    public void openToolDialog(View view)//開啟功能表
    {
        //設定功能表的選單
        ArrayList<ToolLayoutModel> arrayList = new ArrayList<>();
        arrayList.add(new ToolLayoutModel("Copy", R.drawable.copy, "#043730"));
        arrayList.add(new ToolLayoutModel("Edit", R.drawable.edit, "#043730"));
        arrayList.add(new ToolLayoutModel("Clear", R.drawable.clear, "#043730"));
        arrayList.add(new ToolLayoutModel("Save", R.drawable.save, "#043730"));

        //建立自製的MyAdapter物件
        MyAdapter myadapter = new MyAdapter(arrayList,this);

        //得到手機寬高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cellphone_w = metrics.widthPixels;
        int cellphone_h = metrics.heightPixels;

        //建立自製的ToolDialog物件
        toolDialog = new ToolDialog(UserActivity.this, myadapter);
        toolDialog.show();//顯示Dialog
        toolDialog.getWindow().setLayout((int)(cellphone_w*0.65f),(int)(cellphone_h*0.52f));//設定Dialog寬高
        toolDialog.setCanceledOnTouchOutside(true);//設定碰到Dialog範圍以外，是否結束Dialog
    }

    //---------------------------------------------------------------------------------------

    //實作MyAdapter.RecyclerViewItemClickListener介面裡的clickOnItem抽象方法
    @Override
    public void clickOnItem(int dataIndex)//點按tool執行以下東西
    {
        if (toolDialog != null)//如果customDialog有資料
        {
            toolDialog.dismiss();
        }

        switch (dataIndex)
        {
            case 0://複製文字到剪貼簿
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(editText.getText());
                Toast.makeText(UserActivity.this, "已複製到剪貼簿", Toast.LENGTH_SHORT).show();
                break;

            case 1://允許編輯EditText
                editText.setFocusableInTouchMode(true);
                editText.setFocusable(true);
                editText.requestFocus();
                break;

            case 2://設置EditText為空
                editText.setText("");
                break;

            case 3://儲存文字為txt檔
                saveText();
                break;
        }
    }

    public void saveText()//另存新檔
    {
        Calendar mCal = Calendar.getInstance();
        String dateFormat = "yyyyMMdd_kkmmss";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String today = df.format(mCal.getTime());

        File file = new File(imagePath,"OCR("+today+").txt");

        try
        {
            FileOutputStream f_out = new FileOutputStream(file);
            f_out.write(editText.getText().toString().getBytes());
            f_out.flush();
            f_out.close();
            Toast.makeText(this,"已儲存至OCRImage裡",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Toast.makeText(this,"儲存失敗",Toast.LENGTH_SHORT).show();
        }
    }
}
