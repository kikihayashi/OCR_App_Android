OCR_App_Android
===
利用Mobile Vision Text API實現OCR功能

About APP
---
**最低需求：** API 19 (Android 4.4)\
**測試機種：** SAMSUNG GALAXY A5 (Android 6.0.1)\
**可識別文字：** https://developers.google.com/vision/android/text-overview#recognized_languages

build.grade(Project:OCR_Android)
---
    buildscript {
      repositories {
        google()
        jcenter()
        maven {
            url "http://maven.google.com"
        }
        
        .
        .
        .
        
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
        .
        .
        .
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
        .
        .
        .
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
          
+ 設置`android:screenOrientation="portrait"`時，可能會出現錯誤。需到File的Settings，左上角搜尋"Chrome OS"，將`Activity is locked to an orientation` (兩個)取消勾選。

Class
---
**Activity：** CameraActivity、UserActivity\
**Adapter：** MyAdapter\
**Dialog：** ToolDialog、ToolLayoutModel\
**Mobile Vision Text API：** Ocr

XML
---
**activity_camera：** CameraActivity的layout\
**activity_user：** UserActivity的layout\
**tool_dialog：** ToolDialog的layout\
**tool_item：** 在MyAdapter會使用到的layout


Source
---
**客製Dialog：**\
https://medium.com/@makkenasrinivasarao1/android-custom-dialog-with-list-of-items-ba1ab0e78e16

**GridLayoutManager使用：**\
https://www.journaldev.com/13792/android-gridlayoutmanager-example

**Mobile Vision Text API使用：**\
https://codelabs.developers.google.com/codelabs/mobile-vision-ocr/#0 \
https://www.youtube.com/watch?v=rXvtNlX_5E0 \
https://www.youtube.com/watch?v=xoTKpstv9f0



    
