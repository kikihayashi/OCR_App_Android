
package com.wood.ocr_android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToolDialog extends Dialog {

    private Activity userActivity;
    private RecyclerView.Adapter myAdapter;

    private TextView dialog_t;
    private RecyclerView dialog_RV;
    private RecyclerView.LayoutManager dialog_LM;

    public ToolDialog(Context context, int themeResId)
    {
        super(context, themeResId);
    }

    public ToolDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }

    public ToolDialog(Activity act, RecyclerView.Adapter adp)
    {
        super(act);
        userActivity = act;
        myAdapter = adp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tool_dialog);

        dialog_t = findViewById(R.id.dialog_title);
        dialog_RV = findViewById(R.id.dialog_RecyclerView);

        dialog_LM = new GridLayoutManager(userActivity, 2);
        dialog_RV.setLayoutManager(dialog_LM);

        dialog_RV.setAdapter(myAdapter);
    }
}
