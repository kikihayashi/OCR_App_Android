package com.wood.ocr_android;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class Ocr extends Application {

    private Bitmap rotatedBitmap;
    private String str;
    private static final String TAG = CameraActivity.class.getSimpleName();

    Ocr(){}

    Ocr(Uri imageUri, Context context)
    {
        rotatedBitmap = uriToFixBitmap(imageUri);
        str = bitmapToString(rotatedBitmap,context);
    }

    //將Uri物件轉成Bitmap物件
    private Bitmap uriToFixBitmap(Uri imageUri)
    {
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();//BitmapFactory.Options是對圖片解碼時，使用的配置參數類別
            options.inSampleSize = 10;//縮小原圖尺寸(寫1等於不變)
            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(),options);//將文件路徑解碼為bitmap

            ExifInterface ei = new ExifInterface(imageUri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = rotateImage(bitmap, 0);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
        }

        return rotatedBitmap;
    }

    //調整Bitmap被旋轉問題
    private static Bitmap rotateImage(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();//使用Matrix物件
        matrix.postScale(0.8f, 0.8f);//設定圖片比例
        matrix.postRotate(angle);//旋轉圖片角度(順時針)
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),matrix, true);
    }

    //辨識Bitmap並轉成String
    private String bitmapToString(Bitmap bitmap,Context context)
    {
        String b_str = null;
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

        if(!textRecognizer.isOperational())
        {
            Toast.makeText(context,"無法得到文字",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);

            if(items.size()==0)
            {
                Toast.makeText(context,"無法解析照片",Toast.LENGTH_SHORT).show();
                return "文字顯示區";
            }

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < items.size(); i++)
            {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            b_str = sb.toString();
        }

        return b_str;
    }

    protected Bitmap getImageBitmap()
    {
        return rotatedBitmap;
    }

    protected String getImageString()
    {
        return str;
    }

}
