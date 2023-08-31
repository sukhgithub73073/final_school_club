package com.op.eschool.util;

import static com.op.eschool.base.BaseActivity.commonDB;
import static com.op.eschool.base.BaseActivity.webSocketManager;
import static com.op.eschool.util.Constants.DB_SELECTED_GROUP_CLASS;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.LoginActivity;
import com.op.eschool.activities.LoginTypeActivity;
import com.op.eschool.activities.MainActivity;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.principle.PrincipleMainActivity;
import com.op.eschool.interfaces.ClassSelectionInterface;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.interfaces.DialogDismissInterface;
import com.op.eschool.interfaces.DialogInterface;
import com.op.eschool.interfaces.GroupSelectionInterface;
import com.op.eschool.interfaces.NoInternetinterface;
import com.op.eschool.models.CasteModel;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.SubCasteModel;
import com.op.eschool.models.class_models.AllClassModel;
import com.op.eschool.models.class_models.AllSubjectModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.class_models.SubjectModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.pincode_api_model.PostOffice;
import com.op.eschool.models.school_models.GetSchoolModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentModel;
import com.varunjohn1990.iosdialogs4android.IOSDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.echodev.resizer.Resizer;

public class Utility {
    public static String getFileExtension(File uploadFile) {
        String url = uploadFile.toString();
        int lastDotIndex = url.lastIndexOf(".");
        if (lastDotIndex >= 0 && lastDotIndex < url.length() - 1) {
            String extension = url.substring(lastDotIndex + 1);
            return  extension ;
        } else {
            return "jpeg" ;
        }
    }

    public static File getCompressFile(Context context , File file){
        File resizedImage = null;
        try {
            resizedImage = new Resizer(context)
                    .setTargetLength(1080)
                    .setQuality(80)
                    .setOutputFormat("JPEG")
                    .setOutputFilename("resized_image")
                    //.setOutputDirPath(storagePath)
                    .setSourceImage(file)
                    .getResizedFile();
        } catch (IOException e) {
            return resizedImage ;
        }

        return resizedImage ;
    }
    public static String getFileSizeString(long fileSizeInBytes) {
        final long kiloBytes = 1024;
        final long megaBytes = kiloBytes * 1024;
        final long gigaBytes = megaBytes * 1024;

        if (fileSizeInBytes > gigaBytes) {
            return String.format("%.2f GB", (float) fileSizeInBytes / gigaBytes);
        } else if (fileSizeInBytes > megaBytes) {
            return String.format("%.2f MB", (float) fileSizeInBytes / megaBytes);
        } else if (fileSizeInBytes > kiloBytes) {
            return String.format("%.2f KB", (float) fileSizeInBytes / kiloBytes);
        } else {
            return String.format("%d B", fileSizeInBytes);
        }
    }
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_BLUETOOTH = 3;
    private static final int TYPE_NOT_CONNECTED = 0;
    public static List<String> getMonthList(){
        List<String> monthList = new ArrayList<>();
        monthList.add("Jan") ;
        monthList.add("Feb") ;
        monthList.add("Mar") ;
        monthList.add("Apr") ;
        monthList.add("May") ;
        monthList.add("Jun") ;
        monthList.add("Jul") ;
        monthList.add("Aug") ;
        monthList.add("Sep") ;
        monthList.add("Oct") ;
        monthList.add("Nov") ;
        monthList.add("Dec") ;
        return  monthList ;
    }
    public static String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime ;
    }
    public static Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    public static LoginUserModel getLoginUser(Context context){
        try {
            LoginUserModel commonResponse = new Gson().fromJson(new CommonDB(context).getString(Constants.LOGIN_RESPONSE) ,LoginUserModel.class ) ;
            return  commonResponse ;
        }catch (Exception e){
            e.printStackTrace();
            return new LoginUserModel() ;
        }
    }
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null)
            activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return TYPE_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return TYPE_MOBILE;
                case ConnectivityManager.TYPE_BLUETOOTH:
                    return TYPE_BLUETOOTH;
            }
        }
        return TYPE_NOT_CONNECTED;
    }
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
    public static boolean isNetworkConnectedMainThred(Context ctx) {
        try {
            return  true ;
            // return getConnectivityStatus(ctx) != 0 ?true:false ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  true ;
    }
    public static Dialog openAnyDialog(int layout , Activity activity) {

        BottomSheetDialog dialog = new BottomSheetDialog(activity ) ;
        dialog.setContentView(layout) ;
        return dialog;
    }

    public static void commonSpinnerClicks(EditText editText , Spinner spinner){
        editText.setOnClickListener(v->{
            spinner.performClick();
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity= (String) parent.getAdapter().getItem(position);
                editText.setText(selectedCity);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    public static void userLogout(Context context) {
        new CommonDB(context).clearAll();
        context.startActivity(new Intent(context , LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
    }

    public static void gotoHome(Context context) {
        CommonResponse commonResponse = new Gson().fromJson(new CommonDB(context).getString(Constants.LOGIN_RESPONSE) , CommonResponse.class) ;
        commonDB.putString(DB_SELECTED_GROUP_CLASS ,"") ;
        if (commonResponse.Type.equalsIgnoreCase("Principle")){
            context.startActivity(new Intent(context , PrincipleMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
        }else  if (commonResponse.Type.equalsIgnoreCase("Staff")){
            context.startActivity(new Intent(context, StaffMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
        }else if (commonResponse.Type.equalsIgnoreCase("Student")){
            context.startActivity(new Intent(context , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
        }
    }

    public static void wantTOSureDialog(DialogModel dialogModel) {

        new IOSDialog.Builder(dialogModel.activity)
                .title(dialogModel.title)              // String or String Resource ID
                .message(dialogModel.des)  // String or String Resource ID
                .positiveButtonText(dialogModel.postiveBtn)  // String or String Resource ID
                .negativeButtonText(dialogModel.negtiveBtn)   // String or String Resource ID
                .positiveClickListener(new IOSDialog.Listener() {
                    @Override
                    public void onClick(IOSDialog iosDialog) {
                        iosDialog.dismiss();
                        dialogModel.dialogInterface.onButtonClicked("POSTIVE");

                    }
                }).negativeClickListener(new IOSDialog.Listener() {
                    @Override
                    public void onClick(IOSDialog iosDialog) {
                        iosDialog.dismiss();
                        dialogModel.dialogInterface.onButtonClicked("NEGTIVE");
                    }
                })
                .build()
                .show();



    }


    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,

            Manifest.permission.CAMERA,
    };
    public static String[] getPermissionsArray() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    public static File createImageFile() {
        File mediaStorageDir = new File(  Environment .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"test");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File  mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }
    public static Object fromJson(String jsonString, Type type) {
        try {
            return new Gson().fromJson(jsonString, type);
        }catch (Exception e){
            e.printStackTrace() ;
            return new Object() ;
        }
    }

    public static List<ClassModel> convertClassList(String res){
        FLog.w("convertClassList" , "res>>>" + res);
        List<ClassModel> list = new ArrayList<>() ;
        try {
            ArrayList<AllClassModel> classList = (ArrayList<AllClassModel>) fromJson(res,
                    new TypeToken<ArrayList<AllClassModel>>() {
                    }.getType());
            if (classList.size()>0){
                AllClassModel model = classList.get(0) ;
                if (!model.getClassName1().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId1(),"","" + model.getClassName1(),""+model.getClassId1(),"" + model.getClassName1(),"","")) ;
                }
                if (!model.getClassName2().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId2(),"","" + model.getClassName2(),""+model.getClassId2(),"" + model.getClassName2(),"","")) ;
                }
                if (!model.getClassName3().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId3(),"","" + model.getClassName3(),""+model.getClassId3(),"" + model.getClassName3(),"","")) ;
                }
                if (!model.getClassName4().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId4(),"","" + model.getClassName4(),""+model.getClassId4(),"" + model.getClassName4(),"","")) ;
                }
                if (!model.getClassName5().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId5(),"","" + model.getClassName5(),""+model.getClassId5(),"" + model.getClassName5(),"","")) ;
                }
                if (!model.getClassName6().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId6(),"","" + model.getClassName6(),""+model.getClassId6(),"" + model.getClassName6(),"","")) ;
                }
                if (!model.getClassName7().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId7(),"","" + model.getClassName7(),""+model.getClassId7(),"" + model.getClassName7(),"","")) ;
                }
                if (!model.getClassName8().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId8(),"","" + model.getClassName8(),""+model.getClassId8(),"" + model.getClassName8(),"","")) ;
                }
                if (!model.getClassName9().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId9(),"","" + model.getClassName9(),""+model.getClassId9(),"" + model.getClassName9(),"","")) ;
                }
                if (!model.getClassName10().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId10(),"","" + model.getClassName10(),""+model.getClassId10(),"" + model.getClassName10(),"","")) ;
                }
                if (!model.getClassName11().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId11(),"","" + model.getClassName11(),""+model.getClassId11(),"" + model.getClassName11(),"","")) ;
                }
                if (!model.getClassName12().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId12(),"","" + model.getClassName12(),""+model.getClassId12(),"" + model.getClassName12(),"","")) ;
                }
                if (!model.getClassName13().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId13(),"","" + model.getClassName13(),""+model.getClassId13(),"" + model.getClassName13(),"","")) ;
                }
                if (!model.getClassName14().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId14(),"","" + model.getClassName14(),""+model.getClassId14(),"" + model.getClassName14(),"","")) ;
                }
                if (!model.getClassName15().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId15(),"","" + model.getClassName15(),""+model.getClassId15(),"" + model.getClassName15(),"","")) ;
                }
                if (!model.getClassName16().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId16(),"","" + model.getClassName16(),""+model.getClassId16(),"" + model.getClassName16(),"","")) ;
                }
                if (!model.getClassName17().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId17(),"","" + model.getClassName17(),""+model.getClassId17(),"" + model.getClassName17(),"","")) ;
                }
                if (!model.getClassName18().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId18(),"","" + model.getClassName18(),""+model.getClassId18(),"" + model.getClassName18(),"","")) ;
                }
                if (!model.getClassName19().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId19(),"","" + model.getClassName19(),""+model.getClassId19(),"" + model.getClassName19(),"","")) ;
                }
                if (!model.getClassName20().equalsIgnoreCase("AddClass")){
                    list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId20(),"","" + model.getClassName20(),""+model.getClassId20(),"" + model.getClassName20(),"","")) ;
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return list ;
    }

    public static List<SubjectModel> convertSubjectList(String res){
        FLog.w("convertClassList" , "res>>>" + res);
        List<SubjectModel> list = new ArrayList<>() ;
        try {
            ArrayList<AllSubjectModel> classList = (ArrayList<AllSubjectModel>) fromJson(res,
                    new TypeToken<ArrayList<AllSubjectModel>>() {
                    }.getType());
            if (classList.size()>0){
                AllSubjectModel model = classList.get(0) ;
                if (!model.getSubjectName1().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId1(),""+model.getSubjectName1(),"" + model.getSubjectId1(),""+model.getSubjectName1(),"" ,"")) ;
                }
                if (!model.getSubjectName2().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId2(),""+model.getSubjectName2(),"" + model.getSubjectId2(),""+model.getSubjectName2(),"" ,"")) ;
                }
                if (!model.getSubjectName3().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId3(),""+model.getSubjectName3(),"" + model.getSubjectId3(),""+model.getSubjectName3(),"" ,"")) ;
                }
                if (!model.getSubjectName4().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId4(),""+model.getSubjectName4(),"" + model.getSubjectId4(),""+model.getSubjectName4(),"" ,"")) ;
                }
                if (!model.getSubjectName5().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId5(),""+model.getSubjectName5(),"" + model.getSubjectId5(),""+model.getSubjectName5(),"" ,"")) ;
                }
                if (!model.getSubjectName6().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId6(),""+model.getSubjectName6(),"" + model.getSubjectId6(),""+model.getSubjectName6(),"" ,"")) ;
                }
                if (!model.getSubjectName7().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId7(),""+model.getSubjectName7(),"" + model.getSubjectId7(),""+model.getSubjectName7(),"" ,"")) ;
                }
                if (!model.getSubjectName8().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId8(),""+model.getSubjectName8(),"" + model.getSubjectId8(),""+model.getSubjectName8(),"" ,"")) ;
                }
                if (!model.getSubjectName9().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId9(),""+model.getSubjectName9(),"" + model.getSubjectId9(),""+model.getSubjectName9(),"" ,"")) ;
                }
                if (!model.getSubjectName10().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId10(),""+model.getSubjectName10(),"" + model.getSubjectId10(),""+model.getSubjectName10(),"" ,"")) ;
                }
                if (!model.getSubjectName11().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId11(),""+model.getSubjectName11(),"" + model.getSubjectId11(),""+model.getSubjectName11(),"" ,"")) ;
                }
                if (!model.getSubjectName12().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId12(),""+model.getSubjectName12(),"" + model.getSubjectId12(),""+model.getSubjectName12(),"" ,"")) ;
                }
                if (!model.getSubjectName13().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId13(),""+model.getSubjectName13(),"" + model.getSubjectId13(),""+model.getSubjectName13(),"" ,"")) ;
                }
                if (!model.getSubjectName14().equalsIgnoreCase("AddSubject")){
                    list.add(new SubjectModel(""+model.getGroupName() ,"",""+model.getSubjectId14(),""+model.getSubjectName14(),"" + model.getSubjectId14(),""+model.getSubjectName14(),"" ,"")) ;
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return list ;
    }

    public static void showDialogWithButtons(String type , Activity mActivity , String msg , CommonInterface dialogDismissInterface) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(mActivity,R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.status_show_dailog, null);
        alertDialogbd.setCancelable(false) ;
        alertDialogbd.setView(view1);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbd.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

        TextView title = alertDialog.findViewById(R.id.title)
                , description =alertDialog.findViewById(R.id.description)
                , btn_submit =alertDialog.findViewById(R.id.btn_submit)
                , btn_retry =alertDialog.findViewById(R.id.btn_retry)
                ;
        ImageView image = alertDialog.findViewById(R.id.image) ;
        String titleStr ="" ;
        int bgColor ;
        int icon ;
        // ic_close
        if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_PENDING)){
            titleStr="PENDING";
            bgColor = R.color.pending_color ;
            icon = R.drawable.ic_pending ;
        }else if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_FAILED)){
            titleStr="FAILED";
            bgColor = R.color.failed_color ;
            icon = R.drawable.ic_close ;
        }else{
            titleStr="SUCCESS";
            bgColor = R.color.success_color ;
            icon = R.drawable.success_image ;
        }
        btn_submit.setText("Submit");
        btn_retry.setText("Next");
        title.setText("" + titleStr) ;
        description.setText("" + msg) ;
        Glide.with(mActivity)
                .load(icon)
                .apply(new RequestOptions().placeholder(icon))
                .into(image)   ;
        image.getBackground().setTint(ContextCompat.getColor(mActivity, bgColor)) ;
        description.setTextColor(mActivity.getColor(bgColor)) ;

        alertDialog.findViewById(R.id.btn_submit).setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onItemClicked(0) ;
        });
        alertDialog.findViewById(R.id.btn_retry).setVisibility(View.VISIBLE) ;
        alertDialog.findViewById(R.id.btn_retry).setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onItemClicked(1) ;
        });



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                alertDialog.dismiss();
//                dialogDismissInterface.onDialogDismiss();
//            }
//        }, 3000) ;


    }

    public static void showAnimatedDialog(String type , Activity mActivity , String msg , DialogDismissInterface dialogDismissInterface) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(mActivity,R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.status_show_dailog, null);
        alertDialogbd.setCancelable(false) ;
        alertDialogbd.setView(view1);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbd.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

        TextView title = alertDialog.findViewById(R.id.title)
                , description =alertDialog.findViewById(R.id.description) ;
        ImageView image = alertDialog.findViewById(R.id.image) ;
        String titleStr ="" ;
        int bgColor ;
        int icon ;
        // ic_close
        if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_PENDING)){
            titleStr="PENDING";
            bgColor = R.color.pending_color ;
            icon = R.drawable.ic_pending ;
        }else if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_FAILED)){
            titleStr="FAILED";
            bgColor = R.color.failed_color ;
            icon = R.drawable.ic_close ;
        }else{
            titleStr="SUCCESS";
            bgColor = R.color.success_color ;
            icon = R.drawable.success_image ;
        }

        title.setText("" + titleStr) ;
        description.setText("" + msg) ;
        Glide.with(mActivity)
                .load(icon)
                .apply(new RequestOptions().placeholder(icon))
                .into(image)   ;
        image.getBackground().setTint(ContextCompat.getColor(mActivity, bgColor)) ;
        description.setTextColor(mActivity.getColor(bgColor)) ;

        alertDialog.findViewById(R.id.btn_submit).setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onDialogDismiss();
        });



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                alertDialog.dismiss();
//                dialogDismissInterface.onDialogDismiss();
//            }
//        }, 3000) ;


    }
    public static void imageNoteDialog( Activity mActivity ,CommonInterface dialogDismissInterface) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(mActivity,R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.image_note_dailog, null);
        alertDialogbd.setCancelable(false) ;
        alertDialogbd.setView(view1);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbd.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        alertDialog.findViewById(R.id.btn_camera).setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onItemClicked(0);
        });
        alertDialog.findViewById(R.id.btn_gallery).setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onItemClicked(1);
        });



    }
    public static void showAnimatedDialogButton(String buttonTitle , String type , Activity mActivity , String msg , DialogDismissInterface dialogDismissInterface) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(mActivity,R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.status_show_dailog, null);
        alertDialogbd.setCancelable(false) ;
        alertDialogbd.setView(view1);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbd.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();


        TextView title = alertDialog.findViewById(R.id.title)
                , description =alertDialog.findViewById(R.id.description)
                , btn_submit =alertDialog.findViewById(R.id.btn_submit)
                ;
        btn_submit.setVisibility(View.VISIBLE);
        btn_submit.setText(""+buttonTitle);
        ImageView image = alertDialog.findViewById(R.id.image) ;
        String titleStr ="" ;
        int bgColor ;
        int icon ;
        // ic_close
        if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_PENDING)){
            titleStr="PENDING";
            bgColor = R.color.pending_color ;
            icon = R.drawable.ic_pending ;
        }else if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_FAILED)){
            titleStr="FAILED";
            bgColor = R.color.failed_color ;
            icon = R.drawable.ic_close ;
        }else{
            titleStr="SUCCESS";
            bgColor = R.color.success_color ;
            icon = R.drawable.success_image ;
        }

        title.setText("" + titleStr) ;
        title.setVisibility(View.GONE);


        description.setText("" + msg) ;
        Glide.with(mActivity)
                .load(icon)
                .apply(new RequestOptions().placeholder(icon))
                .into(image)   ;
        image.getBackground().setTint(ContextCompat.getColor(mActivity, bgColor)) ;
        description.setTextColor(mActivity.getColor(bgColor)) ;
        if (msg.equalsIgnoreCase("LOST_NETWORK")){
            description.setText("Check your Wi-Fi or mobile data settings to ensure they are enabled and connected to a network.");
        }
        btn_submit.setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onDialogDismiss();
        });



    }

    public static Bitmap  loadFormatedImage(Context context , ImageView imageView , Bitmap originalBitmap){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        Bitmap modifiedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(modifiedBitmap);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = originalBitmap.getPixel(x, y);


                if ((pixel & 0xFF000000) == 0) {
                    // Replace transparent pixel with red color
                    modifiedBitmap.setPixel(x, y, context.getColor(R.color.sky_blue)) ;
                    //modifiedBitmap.setPixel(x, y, Color.RED);
                } else {
                    // Keep the original pixel
                    modifiedBitmap.setPixel(x, y, pixel);
                }
            }
        }
        imageView.setImageBitmap(modifiedBitmap);
        return modifiedBitmap;
    }




    public static String getBase64FromFile(String path) {
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] baat = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baat = baos.toByteArray();
            encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
        } catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }
    public static boolean isValidIFSCCode(String str)
    {
        str= str.toUpperCase() ;
        String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }
    public static File bitmapToFile(Context context,Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = createImageFile() ;
//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }
    public static GetSchoolModel ChkSchlCode(String res) {
        GetSchoolModel response = new GetSchoolModel() ;
        try {
            ArrayList<GetSchoolModel> list = (ArrayList<GetSchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<GetSchoolModel>>() {
                    }.getType());
            if (list.size()>0){
                return list.get(0) ;
            }else{
                return response ;
            }
        } catch (Exception e) {
            return response ;
        }
    }
    public static CommonResponse convertResponse(String res) {
        CommonResponse response = new CommonResponse() ;
        try {
            ArrayList<CommonResponse> list = (ArrayList<CommonResponse>) fromJson(res,
                    new TypeToken<ArrayList<CommonResponse>>() {
                    }.getType());
            if (list.size()>0){
                return list.get(0) ;
            }else{
                return response ;
            }
        } catch (Exception e) {
            return response ;
        }

    }
    public static ArrayList<SubCasteModel> convertSubCasteList(String res) {
        try {
            ArrayList<SubCasteModel> list = (ArrayList<SubCasteModel>) fromJson(res,
                    new TypeToken<ArrayList<SubCasteModel>>() {
                    }.getType());
          return list ;
        } catch (Exception e) {
            return new ArrayList<>() ;
        }

    }
    public static GetSchoolModel convertChkSchlCode(String res) {
        GetSchoolModel response = new GetSchoolModel() ;
        try {
            ArrayList<GetSchoolModel> list = (ArrayList<GetSchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<GetSchoolModel>>() {
                    }.getType());
            if (list.size()>0){
                return list.get(0) ;
            }else{
                return response ;
            }
        } catch (Exception e) {
            return response ;
        }

    }

    public static SchoolModel GetCollageDetail(String res) {
        SchoolModel response = new SchoolModel() ;
        try {
            ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<SchoolModel>>() {
                    }.getType());
            if (list.size()>0){
                return list.get(0) ;
            }else{
                return response ;
            }
        } catch (Exception e) {
            return response ;
        }

    }

    public static void initSpinner(Context context , String title ,  ArrayList<String> androidVersionList){
        final SearchableSpinner searchableSpinner = new SearchableSpinner(context);
        searchableSpinner.setWindowTitle(""+title);
        searchableSpinner.setSpinnerListItems(androidVersionList);
        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
            }
        });
    }

    public static void copyDataClip(String res , Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("sd", res) ;
        clipboard.setPrimaryClip(clip);
    }
    public static List<ClassGroupModel> getGroupClassList(Context context){
        List<ClassGroupModel> groupModelList  =new ArrayList<>() ;

       try {
           FLog.w("SDfdsf" , "SDFfsd>>>>>>" + new CommonDB(context).getString("GetClsAndGrpDt") );
           String getClsAndGrpDtRes = new CommonDB(context).getString("GetClsAndGrpDt");
           getClsAndGrpDtRes = getClsAndGrpDtRes.replace("\\xa0","") ;
           List<AllClassModel> allList = (List<AllClassModel>) fromJson(getClsAndGrpDtRes,
                   new TypeToken<List<AllClassModel>>() {
                   }.getType());
           for (AllClassModel model : allList){
               List<ClassModel> list= new ArrayList<>() ;
               if (!model.getClassName1().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId1(),"","" + model.getClassName1(),""+model.getClassId1(),"" + model.getClassName1(),"","")) ;
               }
               if (!model.getClassName2().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId2(),"","" + model.getClassName2(),""+model.getClassId2(),"" + model.getClassName2(),"","")) ;
               }
               if (!model.getClassName3().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId3(),"","" + model.getClassName3(),""+model.getClassId3(),"" + model.getClassName3(),"","")) ;
               }
               if (!model.getClassName4().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId4(),"","" + model.getClassName4(),""+model.getClassId4(),"" + model.getClassName4(),"","")) ;
               }
               if (!model.getClassName5().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId5(),"","" + model.getClassName5(),""+model.getClassId5(),"" + model.getClassName5(),"","")) ;
               }
               if (!model.getClassName6().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId6(),"","" + model.getClassName6(),""+model.getClassId6(),"" + model.getClassName6(),"","")) ;
               }
               if (!model.getClassName7().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId7(),"","" + model.getClassName7(),""+model.getClassId7(),"" + model.getClassName7(),"","")) ;
               }
               if (!model.getClassName8().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId8(),"","" + model.getClassName8(),""+model.getClassId8(),"" + model.getClassName8(),"","")) ;
               }
               if (!model.getClassName9().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId9(),"","" + model.getClassName9(),""+model.getClassId9(),"" + model.getClassName9(),"","")) ;
               }
               if (!model.getClassName10().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId10(),"","" + model.getClassName10(),""+model.getClassId10(),"" + model.getClassName10(),"","")) ;
               }
               if (!model.getClassName11().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId11(),"","" + model.getClassName11(),""+model.getClassId11(),"" + model.getClassName11(),"","")) ;
               }
               if (!model.getClassName12().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId12(),"","" + model.getClassName12(),""+model.getClassId12(),"" + model.getClassName12(),"","")) ;
               }
               if (!model.getClassName13().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId13(),"","" + model.getClassName13(),""+model.getClassId13(),"" + model.getClassName13(),"","")) ;
               }
               if (!model.getClassName14().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId14(),"","" + model.getClassName14(),""+model.getClassId14(),"" + model.getClassName14(),"","")) ;
               }
               if (!model.getClassName15().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId15(),"","" + model.getClassName15(),""+model.getClassId15(),"" + model.getClassName15(),"","")) ;
               }
               if (!model.getClassName16().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId16(),"","" + model.getClassName16(),""+model.getClassId16(),"" + model.getClassName16(),"","")) ;
               }
               if (!model.getClassName17().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId17(),"","" + model.getClassName17(),""+model.getClassId17(),"" + model.getClassName17(),"","")) ;
               }
               if (!model.getClassName18().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId18(),"","" + model.getClassName18(),""+model.getClassId18(),"" + model.getClassName18(),"","")) ;
               }
               if (!model.getClassName19().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId19(),"","" + model.getClassName19(),""+model.getClassId19(),"" + model.getClassName19(),"","")) ;
               }
               if (!model.getClassName20().equalsIgnoreCase("AddClass")){
                   list.add(new ClassModel(""+model.getGroupName() ,"",""+model.getClassId20(),"","" + model.getClassName20(),""+model.getClassId20(),"" + model.getClassName20(),"","")) ;
               }
               groupModelList.add(new ClassGroupModel(""+model.getGroupId(),""+model.getGroupName(),""+model.getStatus(),list)) ;
           }
           return groupModelList ;
       }catch (Exception e){e.printStackTrace();
           return groupModelList ;
       }
    }
    public static void setGroupAdapter(Activity activity , Context context , TextView textView ,GroupSelectionInterface selectionInterface) {
        try {
            List<ClassGroupModel> groupModelList  = getGroupClassList(context) ;
            SearchableSpinner groupSpinner = new SearchableSpinner(activity) ;
            groupSpinner.setWindowTitle("Select Class Group") ;
            ArrayList<String> strList = new ArrayList<>() ;
            Map<String , ClassGroupModel> groupMap = new HashMap<>() ;
            for (ClassGroupModel s : groupModelList){
                strList.add(s.getGroupName()) ;
                groupMap.put(""+s.getGroupName() ,  s) ;
            }
            groupSpinner.setSpinnerListItems(strList);

            groupSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                    FLog.w("groupSpinner" , "setOnItemSelectListener" +position );
                    textView.setText(groupMap.get(selectedString).getGroupName()) ;
                    selectionInterface.onSelect(groupMap.get(selectedString)) ;
                }
            });
            textView.setOnClickListener(v->{
                groupSpinner.setHighlightSelectedItem(true);
                groupSpinner.show();
            });
        }catch (Exception e){e.printStackTrace();}

    }
    public static void setClassesAdapter(Activity activity ,List<ClassModel> list, Context context , TextView textView , ClassSelectionInterface classSelectionInterface ) {
        SearchableSpinner classSpinner = new SearchableSpinner(activity);
        classSpinner.setWindowTitle("Select Class") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassModel> classMap = new HashMap<>() ;
        for (ClassModel s : list){
            strList.add(s.getClassName()) ;
            classMap.put(""+s.getClassName() ,  s) ;
        }
        classSpinner.setSpinnerListItems(strList);
        classSpinner.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                textView.setText(list.get(position).getName());
                classSelectionInterface.onSelect(classMap.get(selectedString));
            }
        });
        textView.setOnClickListener(v->{
            classSpinner.setHighlightSelectedItem(true);
            classSpinner.show();
        });
    }
    public static String groupFromList(Context context,String className){
        String grp ="" ;
        List<ClassGroupModel> groupModelList  = Utility.getGroupClassList(context);
        for (ClassGroupModel classGroupModel : groupModelList){
            for (ClassModel classModel :classGroupModel.getClassList()){
                if (classModel.getClassName().equalsIgnoreCase(className)){
                    grp = classGroupModel.getGroupName() ;
                }
            }
        }
        return  grp;
    }

    public static void noInternetDialog(String buttonTitle , String type , Activity mActivity , String msg , NoInternetinterface dialogDismissInterface) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(mActivity,R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.status_show_dailog, null);
        alertDialogbd.setCancelable(false) ;
        alertDialogbd.setView(view1);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbd.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        //alertDialog.show();
        dialogDismissInterface.noNetrok(alertDialog) ;

        TextView title = alertDialog.findViewById(R.id.title)
                , description =alertDialog.findViewById(R.id.description)
                , btn_submit =alertDialog.findViewById(R.id.btn_submit)
                ;
        btn_submit.setVisibility(View.VISIBLE);
        btn_submit.setText(""+buttonTitle);
        ImageView image = alertDialog.findViewById(R.id.image) ;
        String titleStr ="" ;
        int bgColor ;
        int icon ;
        // ic_close
        if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_PENDING)){
            titleStr="PENDING";
            bgColor = R.color.pending_color ;
            icon = R.drawable.ic_pending ;
        }else if (type.equalsIgnoreCase("" + Constants.ANIMATED_DAILOG_TYPE_FAILED)){
            titleStr="FAILED";
            bgColor = R.color.failed_color ;
            icon = R.drawable.ic_close ;
        }else{
            titleStr="SUCCESS";
            bgColor = R.color.success_color ;
            icon = R.drawable.success_image ;
        }

        title.setText("" + titleStr) ;
        title.setVisibility(View.GONE);
        description.setText("" + msg) ;
        Glide.with(mActivity)
                .load(icon)
                .apply(new RequestOptions().placeholder(icon))
                .into(image)   ;
        image.getBackground().setTint(ContextCompat.getColor(mActivity, bgColor)) ;
        description.setTextColor(mActivity.getColor(bgColor)) ;
        if (msg.equalsIgnoreCase("LOST_NETWORK")){
            description.setText("Check your Wi-Fi or mobile data settings to ensure they are enabled and connected to a network.");
        }
        btn_submit.setOnClickListener(v->{
            alertDialog.dismiss();
            dialogDismissInterface.onDialogDismiss();
        });
    }
    public static void GetClsAndGrpDt(String Unqid , CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetClsAndGrpDt") ;
        map.put("Unqid" ,"" + Unqid) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(json , res->{
                try {
                    commonDB.putString("GetClsAndGrpDt" , res) ;
                    commonInterface.onItemClicked(0) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }) ;
    }
}
