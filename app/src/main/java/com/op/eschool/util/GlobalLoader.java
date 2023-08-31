package com.op.eschool.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.op.eschool.R;


public class GlobalLoader {
    Activity activity ;
    Dialog dialog ;
    public GlobalLoader(Activity activity) {
        this.activity = activity;
    }
    public void showLoader(){
        try {
            dialog = new Dialog(activity) ;
            dialog.setContentView(R.layout.loader) ;
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false) ;
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            dialog.show() ;
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                    }
                    return true;
                }
            });
            new Handler().postDelayed(()->{
              try {
                  if (dialog != null){
                      if (dialog.isShowing()){
                          dialog.dismiss() ;
                      }
                  }
              }catch (Exception e){e.printStackTrace();}
            } ,120000) ;
        }catch (Exception d){
            d.printStackTrace();
        }

    }
    public void dismissLoader(){
        try {
            dialog.dismiss() ;
        }catch (Exception e){e.printStackTrace();}
    }
}
