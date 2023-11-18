package com.op.eschool.activities.staff.teacher;

import static com.op.eschool.base.MyApplication.staffRegisterList;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.createImageFile;
import static com.op.eschool.util.Utility.fromJson;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.adapters.TeacherAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityTeacherListBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.student.StudentFilterModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.services.StaffRegisterService;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.FileUtils;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.PermissionUtil;
import com.op.eschool.util.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherListActivity extends BaseActivity {
    ActivityTeacherListBinding binding ;
    List<StaffModel> list = new ArrayList<>() ;
    StaffModel selectStaff ;
    ArrayList<StaffModel> allList = new ArrayList<>() ;

    GlobalLoader globalLoader ;
    StudentFilterModel studentFilterModel ;
    private static final int REQUEST_PERMISSSION = 99;
    private Uri imgUri;
    File uploadFile;
    ImageView image;

    //StaffTbl
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_teacher_list) ;
        globalLoader = new GlobalLoader(TeacherListActivity.this) ;
        studentFilterModel = new StudentFilterModel("","","","","") ;
        binding.back.setOnClickListener(v->{onBackPressed();});

        StaffTbl();
        manageClicks() ;
    }

    private void manageClicks() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list = allList.stream()
                        .filter(studentModel -> charSequence.toString().equalsIgnoreCase("") || studentModel.getFullName().contains(charSequence.toString().toUpperCase()))
                        .collect(Collectors.toList());
                setStaffADapter();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.addStaff.setOnClickListener(v->{
            commonDB.putString("STAFF_DETAIL" , "") ;
            startActivity(new Intent(getApplicationContext() , SignUpStaffActivity.class).putExtra("TYPE" ,"ADD_STAFF")) ;
        });
        binding.filter.setOnClickListener(v->{
            filterDialog() ;
        });
        binding.txtStatus.setOnClickListener(v->{
            statusSelection(binding.txtStatus , pos->{
                filterApply();
            }) ;
        });
    }
    private void StaffTbl() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"StaffTbl") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;

        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            globalLoader.dismissLoader();
            list = (ArrayList<StaffModel>) fromJson(res,
                    new TypeToken<ArrayList<StaffModel>>() {
                    }.getType());
            allList.addAll(list) ;
//            setStaffADapter() ;
        });
    }
     void setStaffADapter() {
        if (list.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvData.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvData.setVisibility(View.GONE) ;
        }

        binding.setTeacherAdapter(new TeacherAdapter(getApplicationContext() ,list , (p,type)->{
            if (type.equalsIgnoreCase("CALL")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).getMobileNumber(), null));
                startActivity(intent);
            } else if (type.equalsIgnoreCase("APPROVE")) {
                DialogModel dialogModel = new DialogModel(TeacherListActivity.this ,"Approve","Want to sure to Approve this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p ,list.get(p) , Constants.USER_APPROVE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(TeacherListActivity.this ,"Delete","Want to sure to Delete this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p,list.get(p) , Constants.USER_DELETE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("VIEW")) {
                commonDB.putString("STAFF_DETAIL" , new Gson().toJson(list.get(p))) ;
                startActivity(new Intent(getApplicationContext() , TeacherProfileActivity.class)) ;
            }else if (type.equalsIgnoreCase("IMAGE_VIEW")) {
                selectStaff = list.get(p);
                imageDailog();

            }
        })); ;
    }

    private void filterDialog() {
        Dialog dialog = Utility.openAnyDialog(R.layout.staff_filter , TeacherListActivity.this) ;
        dialog.show() ;
        TextView txt_status = dialog.findViewById(R.id.txt_status) ;

        txt_status.setText("" + studentFilterModel.status) ;


        txt_status.setOnClickListener(v->{
            statusSelection(txt_status ,pos->{}) ;
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btn_apply).setOnClickListener(v->{

            if(txt_status.getText().toString().equalsIgnoreCase("")){
                FToast.makeText(getApplication(), "Please Select a Status", FToast.LENGTH_SHORT).show();
            }else{
                dialog.dismiss();
                studentFilterModel = new StudentFilterModel("" ,
                        "","",
                        "" ,""+txt_status.getText().toString()) ;
                filterApply() ;
            }
        });

    }
    private void statusSelection(TextView txtView , CommonInterface commonInterface) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Status") ;
        ArrayList<String> strList = new ArrayList<>() ;
        strList.add("ALL") ;
        strList.add("PENDING") ;
        strList.add("ACTIVE") ;
        strList.add("DELETE") ;

        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                studentFilterModel.status = selectedString ;
                txtView.setText(selectedString) ;
                commonInterface.onItemClicked(0) ;
            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }

    public void filterApply(){
        list.clear();
        String sts = studentFilterModel.status ;
        if (sts.equalsIgnoreCase("ALL")){
            sts = "" ;

        }
        String finalSts = sts;
        list = allList.stream()
                .filter(studentModel -> finalSts.equalsIgnoreCase("") || studentModel.getActionStatus().equalsIgnoreCase(finalSts))
                .collect(Collectors.toList());
        FLog.w("Dfg" ,"DFg>>>" + new Gson().toJson(list.size())) ;

        setStaffADapter();




    }

    private void UpStuTY(int pos ,StaffModel model, String UpDtType) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "UpStaffTY") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("StaffId" , ""+model.getStaffId()) ;
        map.put("UpDtType" , ""+UpDtType) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();

                    CommonResponse commonResponse =Utility.convertResponse(res) ;
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        String msg = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"Approved":"Deleted" ;
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , TeacherListActivity.this , model.getFullName() + " has been successfully " + msg  ,()->{
                            model.setActionStatus(UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"APPROVED":"DELETE");
                            if (UpDtType.equalsIgnoreCase(Constants.USER_DELETE)){
                                // list.remove(pos);
                            }
                            binding.getTeacherAdapter().notifyDataSetChanged() ;
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TeacherListActivity.this , ""+commonResponse.Msg ,()->{
                        }) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TeacherListActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }

    private void imageDailog() {
        uploadFile = null;
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(TeacherListActivity.this, R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(TeacherListActivity.this).inflate(R.layout.image_dailog, null);
        alertDialogbd.setCancelable(false);
        alertDialogbd.setView(view1);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbd.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        image = alertDialog.findViewById(R.id.image);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.students_placeholder)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getApplicationContext())
                .load((selectStaff.Image))
                .apply(requestOptions)
                .into(image);

        alertDialog.findViewById(R.id.btn_close).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        image.setOnClickListener(v -> {
            checkAllPermission("OnClickListener");
        });
        alertDialog.findViewById(R.id.btn_update).setOnClickListener(v -> {
            if (uploadFile == null) {
                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, TeacherListActivity.this, "Upload Profile Image", () -> {
                });
            } else {
                AdStaffReg(pcc->{
                    alertDialog.dismiss();
                    setStaffADapter();
                });
            }
        });


    }

    private void checkAllPermission(String type) {
        List<String> list = new ArrayList<>();

        if (!PermissionUtil.checkPermisiion(getApplicationContext(), "android.permission.CAMERA")) {
            list.add(Manifest.permission.CAMERA);
        }
        if (!PermissionUtil.checkPermisiion(getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES :
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                list.add(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (list.size() > 0) {
            String[] permissions = new String[list.size()];
            permissions = list.toArray(permissions);
            PermissionUtil.requestPermission(TeacherListActivity.this, permissions, REQUEST_PERMISSSION);
        } else if (type.equalsIgnoreCase("OnClickListener")) {
            Utility.imageNoteDialog(TeacherListActivity.this, t -> {
//                if (t == 0) {
//                    File image = createImageFile();
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    imgUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", image);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
//                    cameraIntent.launch(intent);
//                } else {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    galleryIntent.launch(pickPhoto);
//                }


                if (t == 0) {
                    File image = createImageFile();
                    imgUri = Uri.fromFile(image);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                    startActivityForResult(intent, Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, Constants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                }
            });

        }
    }

    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    CropImage.activity(imgUri).start(TeacherListActivity.this);
                }
            });
    ActivityResultLauncher<Intent> galleryIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Intent data = result.getData();
                        imgUri = data.getData();
                        CropImage.activity(imgUri).start(TeacherListActivity.this);
                    } catch (Exception e) {
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
                    uploadFile = FileUtils.getFileFromUri(getApplicationContext(), resultUri);
                    Glide.with(getApplicationContext())
                            .load(uploadFile)
                            .apply(new RequestOptions().placeholder(R.drawable.placeholder_upload))
                            .into(image);

                    File compressFile = Utility.getCompressFile(getApplicationContext(), uploadFile);
                    if (compressFile != null) {
                        uploadFile = compressFile;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }else if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            loadImage();
            //CropImage.activity(imgUri).start(SignUpStudentActivity.this);
        }else if (requestCode == Constants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
            imgUri = data.getData();
            loadImage() ;

            // Uri selectedImage = data.getData();
            // CropImage.activity(selectedImage).start(SignUpStudentActivity.this);

        }
    }
    private void loadImage() {
        try {
            uploadFile = FileUtils.getFileFromUri(getApplicationContext(), imgUri);
            Glide.with(getApplicationContext())
                    .load(uploadFile)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_upload))
                    .into(image);

            File compressFile = Utility.getCompressFile(getApplicationContext(), uploadFile);
            if (compressFile != null) {
                uploadFile = compressFile;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getBase64FromFile(String path) {
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] baat = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baat = baos.toByteArray();
            encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
//        try {
//            return compressBase64String(encodeString);
//        } catch (IOException e) {
//            return encodeString ;
//        }
    }
    private void AdStaffReg(CommonInterface commonInterface) {
        Map<String,String> map = new HashMap<>() ;
        String ImageExt ="" ,Image="" ;
        if (uploadFile != null){
            ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
            Image = getBase64FromFile(uploadFile.getPath()) ;
        }
            map.put("type" ,"UpStaffEdLs") ;
            map.put("StaffId" ,""+selectStaff.staffId) ;

        if (ImageExt.equalsIgnoreCase("")){
            ImageExt = Utility.getFileExtension(uploadFile) ;
        }
        map.put("FullName" , selectStaff.fullName.toUpperCase()) ;
        map.put("DOB" , selectStaff.dob) ;
        map.put("Gender" ,"" +selectStaff.gender) ;
        map.put("FatherName" , "" + selectStaff.fatherName) ;
        map.put("Qualification" , selectStaff.qualification) ;
        map.put("AadharNo" ,""+selectStaff.aadharNo) ;
        map.put("EmailId" , ""+selectStaff.emailId) ;
        map.put("MobileNumber" , ""+selectStaff.mobileNumber) ;
        map.put("AlternateNumber" ,""+selectStaff.alternateNumber) ;
        map.put("PinCode", "" + selectStaff.pinCode);
        map.put("State", "" + selectStaff.state);
        map.put("District", "" + selectStaff.district);
        map.put("Tahsil", "" + selectStaff.tahsil);
        map.put("Villa_Mohalla", "" + selectStaff.villaMohalla);

        map.put("ImageExt" ,"data:image/" + ImageExt + ";") ;
        map.put("RelegionData" , "") ;
        map.put("CasteData" , "" ) ;
        map.put("SubCasteData" , "") ;
        map.put("AccountNumber" ,""+selectStaff.accountNumber) ;
        map.put("ConfrAccountNumber" ,""+selectStaff.accountNumber) ;
        map.put("BankName" ,""+selectStaff.bankName) ;
        map.put("IfscCode" ,""+selectStaff.ifscCode) ;
        map.put("AccountHldr" ,""+selectStaff.accountHldr) ;
        FLog.w("AdStaffReg" , "map>>" + new Gson().toJson(map)) ;
        map.put("Image" ,"" + Image) ;
            globalLoader.showLoader();
            webSocketManager.sendMessage(map , res->{
                runOnUiThread(()->{
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse = Utility.convertResponse(res) ;
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                            String des = "You has been successfully update your profile" ;
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , TeacherListActivity.this , ""+des ,()->{
                                commonInterface.onItemClicked(0) ;
                            }) ;
                        }else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TeacherListActivity.this , ""+commonResponse.getMsg() ,()->{
                            }) ;
                        }
                    }catch (Exception e){e.printStackTrace();}
                }) ;
            });}


}