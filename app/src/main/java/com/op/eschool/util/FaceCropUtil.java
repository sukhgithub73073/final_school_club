//package com.op.eschool.util;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.util.SparseArray;
//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.face.Face;
//import com.google.android.gms.vision.face.FaceDetector;
//import java.io.File;
//
//public class FaceCropUtil {
//    Context context ;
//    public FaceCropUtil(Context context) {
//        this.context = context;
//    }
//
//    public static Bitmap cropFace(File imageFile) {
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inMutable = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
//        if (bitmap == null) {
//            return null;
//        }
//
//        FaceDetector faceDetector = new FaceDetector.Builder(context)
//                .setTrackingEnabled(false)
//                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
//                .build();
//
//        if (!faceDetector.isOperational()) {
//            // Handle the case where face detection is not available
//            return null;
//        }
//
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        SparseArray<Face> faces = faceDetector.detect(frame);
//
//        if (faces.size() == 0) {
//            // No faces detected
//            return null;
//        }
//
//        // Crop around the first detected face
//        Face firstFace = faces.valueAt(0);
//        PointF faceRect = firstFace.getPosition();
//
////        int left =
////        int top =
////        int right = faceRect.
////        int bottom = faceRect.
//        int left =   10;
//        int top  =   10;
//        int right =  10;
//        int bottom = 10;
//
//        // Ensure the coordinates are within bounds
//        left = Math.max(left, 0);
//        top = Math.max(top, 0);
//        right = Math.min(right, bitmap.getWidth());
//        bottom = Math.min(bottom, bitmap.getHeight());
//
//        // Crop the image
//        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top);
//
//
//
//    }
//}
