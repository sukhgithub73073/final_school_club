package com.op.eschool.interfaces

import android.graphics.Bitmap
import java.lang.Exception

interface BgRemoveInterface {
    fun onSuccess(bitmap: Bitmap)
    fun onFailed(exception: Exception)
}