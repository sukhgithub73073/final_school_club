package com.op.eschool.util.remove_bg;
import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageUtils {

    // Basic threshold value to distinguish between foreground and background
    private static final int THRESHOLD = 200;

    public static Bitmap removeBackground(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, originalBitmap.getConfig());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = originalBitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                // Calculate grayscale value
                int grayscale = (red + green + blue) / 3;

                if (grayscale > THRESHOLD) {
                    // Set the pixel as transparent
                    resultBitmap.setPixel(x, y, Color.TRANSPARENT);
                } else {
                    // Keep the original pixel color
                    resultBitmap.setPixel(x, y, pixel);
                }
            }
        }

        return resultBitmap;
    }
}
