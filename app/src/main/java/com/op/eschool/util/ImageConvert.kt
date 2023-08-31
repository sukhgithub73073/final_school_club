package com.op.eschool.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Paint
import com.op.eschool.R
import com.op.eschool.interfaces.BgRemoveInterface
import java.io.File


class ImageConvert {

    lateinit var bitmap:Bitmap ;
    fun changeBitmapColor(sourceBitmap: Bitmap, color: Int): Bitmap {
        val resultBitmap = sourceBitmap.copy(sourceBitmap.config, true)
        val paint = Paint()
        val filter: ColorFilter = LightingColorFilter(color, 1)
        paint.colorFilter = filter
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
        return resultBitmap
    }
}
