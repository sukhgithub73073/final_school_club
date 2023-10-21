package com.op.eschool.activities.staff.leave;


import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.createImageFile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddLeaveBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.FileUtils;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.PermissionUtil;
import com.op.eschool.util.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddLeaveActivity extends BaseActivity {
    ActivityAddLeaveBinding binding ;
    File uploadFile ;
    private Uri imgUri;
    private static final int REQUEST_PERMISSSION = 99;
    GlobalLoader globalLoader ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_add_leave) ;
        globalLoader = new GlobalLoader(AddLeaveActivity.this) ;

        manageClicks() ;
    }


    private void manageClicks() {
        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.etLeaveTo.setOnClickListener(v->{
            SearchableSpinner groupSpinner = new SearchableSpinner(this);
            groupSpinner.setWindowTitle("Select Leave To") ;
            ArrayList<String> strList = new ArrayList<>() ;
            strList.add("Principle") ;
            strList.add("Manager") ;

            groupSpinner.setSpinnerListItems(strList);
            groupSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                    binding.etLeaveTo.setText(selectedString) ;
                }
            });
            groupSpinner.setHighlightSelectedItem(true);
            groupSpinner.show();
        });

        binding.ivImage.setOnClickListener(v->{
            checkAllPermission("OnClickListener");
        });
        binding.submit.setOnClickListener(v->{
            if (!Utility.isNetworkConnectedMainThred(getApplication())){
                FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
            }else if (binding.etLeaveTo.getText().toString().equalsIgnoreCase("")){
                binding.etLeaveTo.setError("Invald Leave to Selection") ;
            }else if (binding.etSubject.getText().toString().equalsIgnoreCase("")){
                binding.etSubject.setError("Invald Subject") ;
            }else if (binding.etMessage.getText().toString().equalsIgnoreCase("")){
                binding.etMessage.setError("Invald Message") ;
            }else{
                AdLeave() ;
            }
        });




    }

    private void AdLeave() {
        String AttachmentStatus = uploadFile==null?"0":"1" ;

        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "AdLeave");
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("AttachmentStatus" ,AttachmentStatus) ;
        map.put("LeaveTo" ,binding.etLeaveTo.getText().toString()) ;
        map.put("LeaveMsg" ,binding.etSubject.getText().toString()) ;
        map.put("LeaveSubMsg" ,binding.etMessage.getText().toString()) ;

        FLog.w("AdCmplnt" , "map>>>" +new Gson().toJson(map)) ;
        if (uploadFile!=null){
            String ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
            String Image = Utility.getBase64FromFile(uploadFile.getPath()) ;
            map.put("Attachment" , Image ) ;
            map.put("AttachmentExt" , ImageExt ) ;
        }else{
            map.put("Attachment" , "" ) ;
            map.put("AttachmentExt" , "" ) ;
        }
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    CommonResponse commonResponse = Utility.convertResponse(res) ;

                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , AddLeaveActivity.this , "You has been successfully upload your Leave Request" ,()->{
                          Utility.gotoHome(getApplicationContext()) ;
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddLeaveActivity.this , ""+commonResponse.getMsg() ,()->{
                        }) ;
                    }
                }catch (Exception e){e.printStackTrace();}
            }) ;
        });



    }


    private void checkAllPermission(String type) {
        List<String> list = new ArrayList<>() ;
        if (!PermissionUtil.checkPermisiion(getApplicationContext() , "android.permission.CAMERA")){
            list.add(Manifest.permission.CAMERA) ;
        }
        if (!PermissionUtil.checkPermisiion(getApplicationContext() , Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU?"android.permission.READ_MEDIA_IMAGES":"android.permission.WRITE_EXTERNAL_STORAGE")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                list.add(Manifest.permission.READ_MEDIA_IMAGES) ;
            }else{
                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
            }
        }
        if (list.size()>0){
            String[] permissions = new String[list.size()];
            permissions = list.toArray(permissions);
            PermissionUtil.requestPermission(AddLeaveActivity.this ,permissions , REQUEST_PERMISSSION) ;
        }else if (type.equalsIgnoreCase("OnClickListener")){

            DialogModel dialogModel = new DialogModel(AddLeaveActivity.this ,"Take Image","Take photo from Gallery or take new picture using Camera !","Camera","Gallery", t->{
                if (t.equalsIgnoreCase("POSTIVE")){
                    //open camera
                    File image = createImageFile();
                    imgUri = Uri.fromFile(image);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    cameraIntent.launch(intent);
                }else{
                    //open gallery
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.launch(pickPhoto);

                }
            } ) ;
            Utility.wantTOSureDialog(dialogModel) ;

        }





    }
    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    CropImage.activity(imgUri)

                                .start(AddLeaveActivity.this);
                }
            });

    ActivityResultLauncher<Intent> galleryIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Intent data = result.getData() ;
                        Uri selectedImage = data.getData();
                        CropImage.activity(selectedImage)

                                .start(AddLeaveActivity.this);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    uploadFile = FileUtils.getFileFromUri(getApplicationContext(), resultUri) ;
                    binding.txtFilename.setText("" + uploadFile.getName()) ;
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        }
    }


}