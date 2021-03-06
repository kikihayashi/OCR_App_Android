package com.wood.ocr_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//UserActivity繼承AppCompatActivity實作MyAdapter.RecyclerViewItemClickListener介面
public class UserActivity extends AppCompatActivity implements MyAdapter.RecyclerViewItemClickListener {

    private Ocr ocr;
    private EditText editText;
    private String textDisplay = "文字顯示區";
    private ToolDialog toolDialog;
    private static final String imagePath = Environment.getExternalStorageDirectory().toString()+"/OCRImage";//等於sdcard/OCRImage

    private NotificationManager nm;
    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //關閉最上面的狀態bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //關閉APP標題bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_user);

        Intent get_Camera_Intent = this.getIntent();//取得CameraActivity傳遞過來的Intent
        Uri uri_camera = get_Camera_Intent.getParcelableExtra("Uri");//取得Intent裡的uri_camera

        if(uri_camera != null)//如果uri_camera有資料
        {
            ocr = new Ocr(uri_camera, getApplicationContext());//建立Ocr物件
            textDisplay = ocr.getImageString();//得到照片文字
        }

        editText = findViewById(R.id.editText);//設置好editText view
        editText.setText(textDisplay);//將照片文字設置在editText上
        editText.setFocusable(false);//設置不可編輯editText，此指令會自動讓editText.setFocusableInTouchMode(false);
    }

    //按鈕邏輯
    public void restartCamera(View view)//重新拍照
    {
        //建立一個temp_intent物件，new Intent(放當前類別, 放要跳轉的類別.class)
        Intent temp_intent = new Intent(this, CameraActivity.class);
        startActivity(temp_intent);//切換到CameraActivity
        this.finish();//結束UserActivity
    }

    //按鈕邏輯
    public void openToolDialog(View view)//開啟功能表
    {
        //設定功能表的選單
        ArrayList<ToolLayoutModel> arrayList = new ArrayList<>();
        arrayList.add(new ToolLayoutModel("Copy", R.drawable.copy, "#E80010"));
        arrayList.add(new ToolLayoutModel("Edit", R.drawable.edit, "#326846"));
        arrayList.add(new ToolLayoutModel("Clear", R.drawable.clear, "#FF9700"));
        arrayList.add(new ToolLayoutModel("Save", R.drawable.save, "#0E3965"));

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

    //實作MyAdapter.RecyclerViewItemClickListener介面的clickOnItem抽象方法
    @Override
    public void clickOnItem(int dataIndex)//開啟功能表後，點按tool執行以下東西
    {
        if (toolDialog != null)//如果customDialog有資料
        {
            toolDialog.dismiss();//點擊功能表的按鈕後，將會關閉對話方塊
        }

        switch (dataIndex)
        {
            case 0://複製文字到剪貼簿
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(editText.getText());
                Toast.makeText(UserActivity.this, "已複製到剪貼簿", Toast.LENGTH_SHORT).show();
                break;

            case 1://允許編輯EditText
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                break;

            case 2://設置EditText為空
                editText.setText("");
                break;

            case 3://儲存文字為txt檔，並發出通知訊息
                saveText();
                break;
        }
    }

    public void saveText()//另存新檔
    {
        //以下為TXT檔的名稱設置
        Calendar mCal = Calendar.getInstance();
        String dateFormat = "yyyyMMdd_kkmmss";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String today = df.format(mCal.getTime());
        String fileName = "OCR("+today+").txt";

        File file = new File(imagePath, fileName);

        try
        {
            FileOutputStream f_out = new FileOutputStream(file);
            f_out.write(editText.getText().toString().getBytes());
            f_out.flush();
            f_out.close();
            Toast.makeText(this,"已存到我的裝置/OCRImage資料夾中",Toast.LENGTH_SHORT).show();
            sendNotify(fileName);//發出通知訊息
        }
        catch (IOException e)
        {
            Toast.makeText(this,"儲存失敗",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendNotify(String name)//發出通知訊息
    {
        nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //處理Android 6以上的版本問題，設置好後在每個任務中多加setChannelId("OCR")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel =
                    new NotificationChannel("OCR","custom",NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(notificationChannel);
        }

        //當點擊通知訊息時，將會開啟儲存好的TXT檔
        Intent intent = new Intent("android.intent.action.VIEW");//implicit intent
        Uri filePath = Uri.parse("file://"+imagePath+"/"+name);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(filePath, "text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//可不加，但在不同Activity啟動時，一定要加

        TaskStackBuilder tsb = TaskStackBuilder.create(this);
        tsb.addNextIntent(intent);
        PendingIntent pendingIntent = tsb.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("儲存成功")
                .setContentText("已存到我的裝置/OCRImage資料夾中")
                .setSmallIcon(R.drawable.ic_click)
                .setChannelId("OCR")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.BLACK);

        nm.notify(1,builder.build());
    }
}
