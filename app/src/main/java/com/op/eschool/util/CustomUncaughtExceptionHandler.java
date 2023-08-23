package com.op.eschool.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    Context applicationContext ;

    public CustomUncaughtExceptionHandler(Context applicationContext) {
        this.applicationContext = applicationContext ;

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        ClipboardManager clipboardManager = (ClipboardManager) applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
        String textToCopy = throwable.getMessage() ;
        ClipData clipData = ClipData.newPlainText("label", textToCopy);
        clipboardManager.setPrimaryClip(clipData);
    }
}