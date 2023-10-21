package com.op.eschool.activities.staff.student;

import static com.op.eschool.base.MyApplication.studentRegisterList;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Constants.DB_SELECTED_GROUP_CLASS;
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
import androidx.lifecycle.ViewModelProviders;

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
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.register.CollageRegisterActivity;
import com.op.eschool.activities.staff.settings.class_setting.ClassByGrpActivity;
import com.op.eschool.adapters.ParentNexusAdapter;
import com.op.eschool.adapters.ParentsAdapter;
import com.op.eschool.adapters.StudentAdapter;
import com.op.eschool.adapters.StudentNexusAdapter;
import com.op.eschool.adapters.TeacherAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityParentsListBinding;
import com.op.eschool.databinding.ActivityStudentListBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.bankNames.Datum;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.class_models.SelectGrpClass;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentFilterModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.retrofit.RetrofitClient;
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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends BaseActivity {

    List<StudentModel> list = new ArrayList<>();
    StudentModel selectStudent;
    ArrayList<StudentModel> allList = new ArrayList<>();
    ActivityStudentListBinding binding;
    String DB_STUDENT_KEY = "";
    SchoolModel schoolModel;
    GlobalLoader globalLoader;
    StudentFilterModel studentFilterModel;
    Map<String, String> map = new HashMap<>();
    private static final int REQUEST_PERMISSSION = 99;
    private Uri imgUri;
    File uploadFile;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        map.put("type", "StdntTblByClss");
        map.put("Unqid", loginUserModel.collageUnqid);

        //binding = DataBindingUtil.setContentView(this , R.layout.activity_student_list) ;
        globalLoader = new GlobalLoader(StudentListActivity.this);
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
        schoolModel = new Gson().fromJson(commonDB.getString("SCHOOL_DETAILS"), SchoolModel.class);
        studentFilterModel = new StudentFilterModel("", "", "", "", "");
        manageClicks();
        if (getIntent().getStringExtra("API_FLAG") != null){
            map = commonDB.getMap("MAP_StdntTblByClss");
            binding.txtGroup.setText(map.get("GROUP_NAME"));
            binding.txtClass.setText(map.get("CLASS_NAME"));
            StdntTblByClss() ;
        }
    }

    private void manageClicks() {
        Utility.setGroupAdapter(StudentListActivity.this, getApplicationContext(), binding.txtGroup, model -> {
            studentFilterModel.group = model.getGroupName();
            binding.txtClass.setText("");
            binding.noData.setVisibility(View.VISIBLE);
            binding.rvStudents.setVisibility(View.GONE);
            map.put("GROUP_NAME" , model.getGroupName()) ;
            FLog.e("DSdsdf" ,"getClassList>>>>>>>>>" + model.getClassList().size()) ;
            Utility.setClassesAdapter(StudentListActivity.this, model.getClassList(), getApplicationContext(), binding.txtClass, classModel -> {
                studentFilterModel.className = classModel.getClassName();
                map.put("ClassId", classModel.getClassId() + "") ;

                map.put("CLASS_NAME" , classModel.getClassName()) ;
                // filterApply() ;
                StdntTblByClss();
            });
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                list = allList.stream()
                        .filter(studentModel -> studentModel._class.equalsIgnoreCase(studentFilterModel.className))
                        .filter(studentModel -> studentFilterModel.gender.equalsIgnoreCase("") || studentModel.gender.equalsIgnoreCase(studentFilterModel.gender))
                        .filter(studentModel -> charSequence.toString().equalsIgnoreCase("") || studentModel.fullName.contains(charSequence.toString().toUpperCase()))
                        .filter(studentModel -> studentFilterModel.caste.equalsIgnoreCase("") || studentModel.casteData.equalsIgnoreCase(studentFilterModel.caste))
                        .collect(Collectors.toList());


                setStudentADapter();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.addStudent.setOnClickListener(v -> {
            commonDB.putString("STUDENT_DETAIL", "");
            startActivity(new Intent(getApplicationContext(), SignUpStudentActivity.class).putExtra("TYPE", "ADD_STUDENT"));
        });
    }
    private void StdntTblByClss() {
        globalLoader.showLoader();
        commonDB.putMap("MAP_StdntTblByClss" , map) ;
        webSocketManager.sendMessage(map, res -> {
            try {
                runOnUiThread(() -> {
                    list.clear();
                    allList.clear();
                    globalLoader.dismissLoader();
                    list = (ArrayList<StudentModel>) fromJson(res,
                            new TypeToken<ArrayList<StudentModel>>() {
                            }.getType());
                    allList.addAll(list);
                    setStudentADapter();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        if (commonDB.getString("NOTIFY_API").equalsIgnoreCase("NOTIFY")){
//            StdntTblByClss();
//        }
    }

    //    private void StdntTblSocket(String type) {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"StdntTbl") ;
//        map.put("Unqid" , loginUserModel.collageUnqid) ;
//        String json = new Gson().toJson(map) ;
//        try {
//            globalLoader.showLoader();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        webSocketManager.sendMessage(map , res->{
//            try {
//                runOnUiThread(()->{
//
//                    globalLoader.dismissLoader();
//                    list = (ArrayList<StudentModel>) fromJson(res,
//                            new TypeToken<ArrayList<StudentModel>>() {
//                            }.getType());
//                    allList.addAll(list) ;
//                });
//            } catch (Exception e) {
//                e.printStackTrace() ;
//            }
//        });
//    }
    private void setStudentADapter() {
        if (list.size() > 0) {
            binding.noData.setVisibility(View.GONE);
            binding.rvStudents.setVisibility(View.VISIBLE);
        } else {
            binding.noData.setVisibility(View.VISIBLE);
            binding.rvStudents.setVisibility(View.GONE);
        }
        binding.setStudentAdapter(new StudentAdapter(getApplicationContext(), list, (p, type) -> {
            if (type.equalsIgnoreCase("CALL")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).mobileNumber, null));
                startActivity(intent);
            } else if (type.equalsIgnoreCase("APPROVE")) {
                DialogModel dialogModel = new DialogModel(StudentListActivity.this, "Approve", "Want to sure to Approve this user ?", "Yeah, sure", "Cancel", t -> {
                    if (t.equalsIgnoreCase("POSTIVE")) {
                        UpStuTY(p, list.get(p), Constants.USER_APPROVE);
                    }
                });
                Utility.wantTOSureDialog(dialogModel);
            } else if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(StudentListActivity.this, "Delete", "Want to sure to Delete this user ?", "Yeah, sure", "Cancel", t -> {
                    if (t.equalsIgnoreCase("POSTIVE")) {
                        UpStuTY(p, list.get(p), Constants.USER_DELETE);
                    }
                });
                Utility.wantTOSureDialog(dialogModel);
            } else if (type.equalsIgnoreCase("VIEW")) {
                commonDB.putString("STUDENT_DETAIL", new Gson().toJson(list.get(p)));
                startActivity(new Intent(getApplicationContext(), StudentProfileActivity.class));
            } else if (type.equalsIgnoreCase("IMAGE_VIEW")) {
                selectStudent = list.get(p);
                imageDailog();

            }
        }));
    }


    public void filterApply() {
        list.clear();
        String sts = studentFilterModel.status;
        if (sts.equalsIgnoreCase("ALL")) {
            sts = "";
        }
        String finalSts = sts;
        list = allList.stream()
                .filter(studentModel -> studentModel._class.equalsIgnoreCase(studentFilterModel.className))
                .filter(studentModel -> studentFilterModel.gender.equalsIgnoreCase("") || studentModel.gender.equalsIgnoreCase(studentFilterModel.gender))
                .filter(studentModel -> finalSts.equalsIgnoreCase("") || studentModel.actionStatus.equalsIgnoreCase(finalSts))
                .filter(studentModel -> studentFilterModel.caste.equalsIgnoreCase("") || studentModel.casteData.equalsIgnoreCase(studentFilterModel.caste))
                .collect(Collectors.toList());
        FLog.w("Dfg", "DFg>>>" + new Gson().toJson(list.size()));
        setStudentADapter();
    }

    private void UpStuTY(int pos, StudentModel model, String UpDtType) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "UpStuTY");
        map.put("Unqid", loginUserModel.collageUnqid);
        map.put("StudentId", "" + model.studentId);
        map.put("UpDtType", "" + UpDtType);
        String json = new Gson().toJson(map);
        globalLoader.showLoader();
        webSocketManager.sendMessage(map, res -> {
            globalLoader.dismissLoader();
            runOnUiThread(() -> {
                try {

                    CommonResponse commonResponse = Utility.convertResponse(res);
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                        String msg = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE) ? "Approved" : "Deleted";
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS, StudentListActivity.this, model.fullName + " has been successfully " + msg, () -> {
                            model.actionStatus = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE) ? "APPROVED" : "DELETE";
                            if (UpDtType.equalsIgnoreCase(Constants.USER_DELETE)) {
                                list.remove(pos);
                            }
                            binding.getStudentAdapter().notifyDataSetChanged();
                        });
                    } else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, StudentListActivity.this, "" + commonResponse.Msg, () -> {
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, StudentListActivity.this, "" + e.getMessage(), () -> {
                    });
                }
            });
        });
    }

    private void imageDailog() {
        uploadFile = null;
        androidx.appcompat.app.AlertDialog.Builder alertDialogbd = new androidx.appcompat.app.AlertDialog.Builder(StudentListActivity.this, R.style.AlertDailogueTheme);
        View view1 = LayoutInflater.from(StudentListActivity.this).inflate(R.layout.image_dailog, null);
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
                .load((selectStudent.Image))
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
                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, StudentListActivity.this, "Upload Profile Image", () -> {
                });
            } else {
                AdStuReg(pcc->{
                    alertDialog.dismiss();
                    StdntTblByClss();
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
            PermissionUtil.requestPermission(StudentListActivity.this, permissions, REQUEST_PERMISSSION);
        } else if (type.equalsIgnoreCase("OnClickListener")) {
            Utility.imageNoteDialog(StudentListActivity.this, t -> {
                if (t == 0) {
                    File image = createImageFile();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imgUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", image);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    cameraIntent.launch(intent);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.launch(pickPhoto);
                }

            });

        }
    }

    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    CropImage.activity(imgUri).start(StudentListActivity.this);
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
                        CropImage.activity(imgUri).start(StudentListActivity.this);
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


//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//                    removeBG(bitmap) ;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
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

    private void AdStuReg(CommonInterface commonInterface) {
        Map<String,String> map = new HashMap<>() ;
        String ImageExt = "", Image = "";

        if (uploadFile != null) {
            ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
            Image = getBase64FromFile(uploadFile.getPath());
        }
        if (ImageExt.equalsIgnoreCase("")) {
            ImageExt = Utility.getFileExtension(uploadFile);
        }
        map.put("type", "UpStuEdLs");
        map.put("StudentId", "" + selectStudent.studentId);
        map.put("Subjects", "[]");
        map.put("Class", selectStudent._class);
        map.put("ClassId", selectStudent.ClassId);
        map.put("FullName", selectStudent.fullName.toUpperCase());
        map.put("DOB", "" + selectStudent.dob);
        map.put("Gender", "" + selectStudent.gender);
        map.put("FatherName", selectStudent.fatherName.toUpperCase());
        map.put("FatherOccupation", "" + selectStudent.fatherOccupation);
        map.put("MotherName", selectStudent.motherName.toUpperCase());
        map.put("MotherOccupation", "" + selectStudent.motherOccupation);
        map.put("PreviousSchool", "" + selectStudent.previousSchool);
        map.put("AadharNo", "" + selectStudent.aadharNo);
        map.put("EmailId", "" + selectStudent.emailId);
        map.put("MobileNumber", selectStudent.mobileNumber.toString());
        map.put("AlternateNumber", "" + selectStudent.alternateNumber);
        map.put("PinCode", "" + selectStudent.pinCode);
        map.put("State", "" + selectStudent.state);
        map.put("District", "" + selectStudent.district);
        map.put("Tahsil", "" + selectStudent.tahsil);
        map.put("Villa_Mohalla", "" + selectStudent.villaMohalla);
        map.put("ImageExt", "data:image/" + ImageExt + ";");
        map.put("RelegionData", "");
        map.put("CasteData", "");
        map.put("SubCasteData", "");
        map.put("Image", "" + Image);

        FLog.w("AdStuReg", "map>>" + new Gson().toJson(map));

        globalLoader.showLoader();
        webSocketManager.sendMessage(map, res -> {
            runOnUiThread(() -> {
                try {
                    globalLoader.dismissLoader();
                    CommonResponse commonResponse = Utility.convertResponse(res);
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                        String des = "You has been successfully update your profile";
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS, StudentListActivity.this, "" + des, () -> {
                            commonInterface.onItemClicked(0);
                        });
                    } else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, StudentListActivity.this, "" + commonResponse.getMsg(), () -> {
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });


    }

}