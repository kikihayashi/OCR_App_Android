OCR_App_Android
===
本專案使用Google的Mobile Vision Text API實現OCR(拍照圖片轉文字)功能

About APP
---
**開發環境：** Android Studio 3.6.2 \
**版本要求：** Android 4.4(API 19)以上 \
**測試機種：** SAMSUNG GALAXY A5 -> Android 6.0.1(API 23)\
**識別文字：** https://developers.google.com/vision/android/text-overview#recognized_languages \
**APK安裝檔：** https://github.com/kikihayashi/OCR_App_Android/raw/master/OCR_App_Android.apk

執行畫面
---
![image](https://github.com/kikihayashi/OCR_App_Android/blob/master/test0.png) 

設計概念
---
以下將介紹專案中每個程式的功能 \
 [目前有CameraActivity.class 和 UserActivity.class](https://github.com/kikihayashi/OCR_App_Android/blob/master/Flow%20Chart.pdf) (Flow Chart 簡易流程圖) 

**【Class】**\
CameraActivity：程式最初進入點，負責處理"開啟照相機"的動作(內部有權限檢查，結束後將會切換到UserActivity) \
UserActivity：取得CameraActivity傳遞過來的資料，並使用Ocr物件得到照片文字 \
MyAdapter：負責處理"對話框"的顯示資料(會使用到ToolLayoutModel物件) \
ToolDialog：負責顯示對話框(會用到UserActivity、MyAdapter) \
ToolLayoutModel：對話框項目，內部設置圖片、文字、背景顏色 \
Ocr：負責處理圖片轉文字的相關動作(使用Mobile Vision Text API)

**【XML】**\
activity_user：使用者畫面(UserActivity的layout) \
tool_dialog：對話框(ToolDialog的layout) \
tool_item：對話框項目(tool_dialog中RecyclerView上的layout)

build.grade(Project:OCR_Android)
---
    buildscript {
      repositories {
        google()
        jcenter()
        maven {
            url "http://maven.google.com"
        }    
        
        ...
        
    allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://jitpack.io"
        }
    }

build.grade(Module.app)
---
    dependencies {
        ...  
    implementation "androidx.cardview:cardview:1.0.0"
    implementation 'com.google.android.gms:play-services-vision:20.0.0'
    implementation "androidx.recyclerview:recyclerview-selection:1.1.0-rc01"
    

AndroidManifest
---
    package="com.wood.ocr_android">

    <uses-feature android:name="android.hardware.camera" android:required="true"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.INTERNET"/>

    <application
    
      ...
      
        android:theme="@style/Theme.AppCompat.NoActionBar">
        <activity android:name="com.wood.ocr_android.CameraActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity android:name="com.wood.ocr_android.UserActivity"
                   android:screenOrientation="portrait">
        </activity>     
          
+ 設置`android:screenOrientation="portrait"`時，可能會出現錯誤。\
需到File的Settings，左上角搜尋"Chrome OS"，將`Activity is locked to an orientation` (兩個)取消勾選。



Source
---
**客製Dialog：**\
https://medium.com/@makkenasrinivasarao1/android-custom-dialog-with-list-of-items-ba1ab0e78e16 \
https://mrraybox.blogspot.com/2015/02/android-dialog-layout.html

**GridLayoutManager使用：**\
https://www.journaldev.com/13792/android-gridlayoutmanager-example

**Mobile Vision Text API使用：**\
https://codelabs.developers.google.com/codelabs/mobile-vision-ocr/#0 \
https://www.luoow.com/dc_tw/100377642 \
https://www.youtube.com/watch?v=rXvtNlX_5E0 \
https://www.youtube.com/watch?v=xoTKpstv9f0
