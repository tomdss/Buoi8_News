package com.t3h.buoi8_news.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.t3h.buoi8_news.R;

public class DialogDelete extends Dialog implements View.OnClickListener {

    private DeleteCallback callback;

    private Button btnYes;
    private Button btnNo;


    public DialogDelete(Context context) {
        super(context,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.ui_dialog_delete);
        initViews();
    }

    private void initViews() {
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }


    public void setCallback(DeleteCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_yes:
                dismiss();
                if(callback!=null){
                    callback.onYesClick();
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }

    }

    public interface DeleteCallback{
        void onYesClick();
    }

}
