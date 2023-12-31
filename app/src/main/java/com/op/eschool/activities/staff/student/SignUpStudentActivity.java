package com.op.eschool.activities.staff.student;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Constants.DB_SELECTED_GROUP_CLASS;
import static com.op.eschool.util.Utility.createImageFile;
import static com.op.eschool.util.Utility.fromJson;
import static com.op.eschool.util.Utility.groupFromList;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySignUpStudentBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.SelectGrpClass;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.pincode_api_model.PincodeModel;
import com.op.eschool.models.pincode_api_model.PostOffice;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.services.StudentRegisterService;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStudentActivity extends BaseActivity {
    ActivitySignUpStudentBinding binding;
    StudentModel editModel;
    GlobalLoader globalLoader;
    private static final int REQUEST_PERMISSSION = 99;
    private Uri imgUri;
    SchoolModel schoolModel;
    File uploadFile;
    String ClassId, Unqid;
    Map<String, String> map = new HashMap<>();
    String TYPE = "";
    String ImageExt = "", Image = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_student);
        StrictMode.VmPolicy.Builder builders = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builders.build());
        requestPermissions(Utility.getPermissionsArray(), 12);
        TYPE = getIntent().getStringExtra("TYPE");

        globalLoader = new GlobalLoader(SignUpStudentActivity.this);
        if (!commonDB.getString("STUDENT_DETAIL").equalsIgnoreCase("")) {
            FLog.w("editModel", ">>>>>>>>>>>>" + commonDB.getString("STUDENT_DETAIL"));
            editModel = new Gson().fromJson(commonDB.getString("STUDENT_DETAIL"), StudentModel.class);
            ClassId = editModel.ClassId;
            binding.linOffline.setVisibility(View.GONE);
        }

        if (TYPE.equalsIgnoreCase("REGISTER_STUDENT")) {
            SchoolModel schoolModel = Utility.GetCollageDetail(commonDB.getString("GetCollageDetail"));
            map.put("CollageCode", "" + schoolModel.getCollageCode());
            map.put("CollageName", "" + schoolModel.getCollageName());
            map.put("Unqid", "" + schoolModel.getUnqid());

        } else {
            LoginUserModel loginUserModel = Utility.getLoginUser(getApplicationContext());
            map.put("CollageCode", "" + loginUserModel.collageCode);
            map.put("CollageName", "" + loginUserModel.collageName);
            map.put("Unqid", "" + loginUserModel.getCollageUnqid());


        }
        FLog.w("AdStuReg", "onCreateonCreate>>" + new Gson().toJson(map));
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
        setAllData();
        initSchool();
        manageClicks();
    }

    private void manageClicks() {
        if (editModel == null) {
            Utility.setGroupAdapter(SignUpStudentActivity.this, getApplicationContext(), binding.etGroup, model -> {
                binding.etClass.setText("");
                Utility.setClassesAdapter(SignUpStudentActivity.this, model.getClassList(), getApplicationContext(), binding.etClass, classModel -> {
                    ClassId = classModel.getClassId();
                    FLog.w("manageClicks", "ClassId>>>" + ClassId);
                });
            });
        }
        binding.image.setOnClickListener(v -> {
            checkAllPermission("OnClickListener");
        });
        binding.btnNext.setOnClickListener(v -> {
//            FLog.e("checkValidation", "Camera capture error: " + binding.etFname.getText().toString());
            if (editModel == null && uploadFile == null) {
                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, SignUpStudentActivity.this, "Upload Profile Image", () -> {
                });
            } else {
                checkValidation();
            }
        });
        binding.etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.etPincode.length() == 6) {
                    PincodeData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkValidation() {

        if (!Utility.isNetworkConnectedMainThred(getApplication())) {
            FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
        } else if (editModel == null && binding.etGroup.getText().toString().equalsIgnoreCase("")) {
            binding.etGroup.setError("Invalid Class Group");

        } else if (editModel == null
                && binding.etClass.getText().toString().equalsIgnoreCase("")
                || ClassId == null
                || ClassId.toLowerCase().equalsIgnoreCase("null")
        ) {
            binding.etClass.setError("Invalid Class");

        } else if (binding.etName.getText().toString().equalsIgnoreCase("")) {
            binding.etName.setError("Invalid Onwer Name");

        } else if (binding.etFname.getText().toString().equalsIgnoreCase("")) {
            binding.etFname.setError("Invalid Father Name");

        } else if (binding.etMname.getText().toString().equalsIgnoreCase("")) {
            binding.etMname.setError("Invalid Mother Name");

        } else if (binding.etMobile.getText().toString().length() != 10) {
            binding.etMobile.setError("Invalid Mobile Number");

        } else if (binding.etPincode.getText().toString().length() != 6) {
            binding.etPincode.setError("Invalid Pincode");

        } else if (binding.etTehsil.getText().toString().equalsIgnoreCase("")) {
            binding.etTehsil.setError("Invalid Tehsil");

        } else if (binding.etVillageMohalla.getText().toString().equalsIgnoreCase("")) {
            binding.etVillageMohalla.setError("Invalid Village Mohalla");

        } else if (editModel == null && !binding.checkBox.isChecked()) {
            FToast.makeText(getApplicationContext(), "Apply Terms & Conditions", FToast.LENGTH_SHORT).show();

        } else {
            AdStuReg();
        }

    }

    public static String compressBase64String(String base64Input) throws IOException {
        byte[] binaryData = Base64.decode(base64Input, Base64.DEFAULT);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, new Deflater(Deflater.BEST_COMPRESSION));

        deflaterOutputStream.write(binaryData);
        deflaterOutputStream.close();

        byte[] compressedData = outputStream.toByteArray();

        return Base64.encodeToString(compressedData, Base64.DEFAULT);
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

    private void AdStuReg() {
        if (uploadFile != null) {
            ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
            Image = getBase64FromFile(uploadFile.getPath());
        } else if (editModel != null) {
            ImageExt = editModel.ImageExt;
            Image = "";
        }
        if (ImageExt.equalsIgnoreCase("")) {
            ImageExt = Utility.getFileExtension(uploadFile);
        }
        map.put("type", "AdStuReg");
        if (editModel != null) {
            map.put("type", "UpStuEdLs");
            map.put("StudentId", "" + editModel.studentId);
        }
        if (Image.equalsIgnoreCase("")) {
            map.put("type", "UpStuEdLs2");
        }
        map.put("Subjects", "[]");
        map.put("Class", binding.etClass.getText().toString());
        map.put("ClassId", ClassId + "");
        // map.put("ClassId", "null") ;
        map.put("FullName", binding.etName.getText().toString().toUpperCase());
        map.put("DOB", "");
        map.put("Gender", "MALE");
        map.put("FatherName", "MR. " + binding.etFname.getText().toString().toUpperCase());
        map.put("FatherOccupation", "");
        map.put("MotherName", "MRS. " + binding.etMname.getText().toString().toUpperCase());
        map.put("MotherOccupation", "");
        map.put("PreviousSchool", "");
        map.put("AadharNo", "");
        map.put("EmailId", "");
        map.put("MobileNumber", binding.etMobile.getText().toString());
        map.put("AlternateNumber", "");
        map.put("PinCode", binding.etPincode.getText().toString());
        map.put("State", binding.etState.getText().toString().toUpperCase());
        map.put("District", binding.etDistrict.getText().toString().toUpperCase());
        map.put("Tahsil", binding.etTehsil.getText().toString().toUpperCase());
        map.put("Villa_Mohalla", binding.etVillageMohalla.getText().toString().toUpperCase());
        map.put("RelegionData", "");
        map.put("CasteData", "");
        map.put("SubCasteData", "");
        FLog.w("AdStuReg", "map>>" + new Gson().toJson(map));
        if (!Image.equalsIgnoreCase("")) {
            map.put("Image", "" + Image);
            map.put("ImageExt", "data:image/" + ImageExt + ";");
        }

//        if (editModel == null && binding.checkOffline.isChecked()) {
//            studentRegisterList.add(map);
//            if (TYPE.equalsIgnoreCase("REGISTER_STUDENT")) {
//                showDialogWithButtons();
//            } else {
//                showAnimatedDialog();
//            }
//        } else

        {
            globalLoader.showLoader();
            webSocketManager.sendMessage(map, res -> {
                runOnUiThread(() -> {
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse = Utility.convertResponse(res);
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                            SelectGrpClass selectGrpClass = new SelectGrpClass("" + binding.etGroup.getText().toString(),
                                    "",
                                    "" + binding.etClass.getText().toString(), "" + ClassId);
                            commonDB.putString(DB_SELECTED_GROUP_CLASS, new Gson().toJson(selectGrpClass));
                            if (TYPE.equalsIgnoreCase("REGISTER_STUDENT")) {
                                showDialogWithButtons();
                            } else {
                                showAnimatedDialog();
                            }
                        } else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, SignUpStudentActivity.this, "" + commonResponse.getMsg(), () -> {
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }

    private void PincodeData() {
        globalLoader.showLoader();
        String url = "https://api.postalpincode.in/pincode/" + binding.etPincode.getText().toString();
        RetrofitClient.getRetrofitInstance().pincodeApi(url).enqueue(new Callback<List<PincodeModel>>() {
            @Override
            public void onResponse(Call<List<PincodeModel>> call, Response<List<PincodeModel>> response) {
                try {
                    globalLoader.dismissLoader();
                    if (response.body().size() > 0) {
                        if (response.body().get(0).getStatus().equalsIgnoreCase("Success")) {
                            binding.etState.setText("" + response.body().get(0).getPostOffice().get(0).getState());
                            binding.etDistrict.setText("" + response.body().get(0).getPostOffice().get(0).getDistrict());
                            binding.etTehsil.setText("");
                            setTehsilAdapter(response.body().get(0).getPostOffice());

                        } else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, SignUpStudentActivity.this, "No records found Please enter valid Pincode", () -> {
                            });
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<PincodeModel>> call, Throwable t) {
                globalLoader.dismissLoader();
            }
        });
    }

    List<PostOffice> filterTehsilList(List<PostOffice> list) {
        List<PostOffice> rtnList = new ArrayList<>();
        List<String> tehsilList = new ArrayList<>();
        for (PostOffice postOffice : list) {
            if (!tehsilList.contains(postOffice.getBlock())) {
                tehsilList.add(postOffice.getBlock());
            }
        }
        for (String s : tehsilList) {
            PostOffice p = new PostOffice();
            p.setBlock(s);
            rtnList.add(p);
        }
        return rtnList;
    }

    private void setTehsilAdapter(List<PostOffice> postOffices) {
        List<PostOffice> list = filterTehsilList(postOffices);

        SearchableSpinner tehsil = new SearchableSpinner(this);
        tehsil.setWindowTitle("Select Tehsil");
        ArrayList<String> strList = new ArrayList<>();
        Map<String, PostOffice> classMap = new HashMap<>();
        for (PostOffice s : list) {
            strList.add(s.getBlock());
            classMap.put("" + s.getBlock(), s);
        }
        tehsil.setSpinnerListItems(strList);
        tehsil.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                binding.etTehsil.setText(selectedString);
            }
        });
        binding.etTehsil.setOnClickListener(v -> {
            tehsil.setHighlightSelectedItem(true);
            tehsil.show();
        });


    }

    private void setAllData() {
        try {
            if (editModel != null) {
                binding.checkBoxAndText.setVisibility(View.GONE);
                binding.btnNext.setText("Update");

                binding.etGroup.setText("" + groupFromList(getApplicationContext(), editModel._class));
                binding.etClass.setText("" + editModel._class);
                binding.etName.setText("" + editModel.fullName);
                binding.etFname.setText("" + editModel.fatherName);
                binding.etMname.setText("" + editModel.motherName);
                binding.etMobile.setText("" + editModel.mobileNumber);
                binding.etPincode.setText("" + editModel.pinCode);
                binding.etDistrict.setText("" + editModel.district);
                binding.etTehsil.setText("" + editModel.tahsil);
                binding.etVillageMohalla.setText("" + editModel.villaMohalla);

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.logo)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(getApplicationContext())
                        .load((editModel.Image))
                        .apply(requestOptions)
                        .into(binding.image);
            }

            if (editModel == null && !commonDB.getString(DB_SELECTED_GROUP_CLASS).equalsIgnoreCase("")) {
                SelectGrpClass selectGrpClass = new Gson().fromJson(commonDB.getString(DB_SELECTED_GROUP_CLASS), SelectGrpClass.class);
                ClassId = selectGrpClass.classId;
                binding.etGroup.setText("" + selectGrpClass.groupName);
                binding.etClass.setText("" + selectGrpClass.className);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 11;
    private static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 12;

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
            PermissionUtil.requestPermission(SignUpStudentActivity.this, permissions, REQUEST_PERMISSSION);
        } else if (type.equalsIgnoreCase("OnClickListener")) {
            Utility.imageNoteDialog(SignUpStudentActivity.this, t -> {

                if (t == 0) {
                    File image = createImageFile();
                    imgUri = Uri.fromFile(image);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                }
//                if (t == 0) {
//                    File image = createImageFile();
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
//                    imgUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", image);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
//                    cameraIntent.launch(intent);
//                } else {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    galleryIntent.launch(pickPhoto);
//                }

            });

        }
    }


    private void initSchool() {
        try {
            String res = commonDB.getString("GetCollageDetail");

            ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<SchoolModel>>() {
                    }.getType());
            schoolModel = list.get(0);
            commonDB.putString("SCHOOL_DETAILS", new Gson().toJson(schoolModel));
            binding.schoolName.setText("" + schoolModel.getCollageName());

            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(schoolModel.getImage()))
                    .apply(new RequestOptions().placeholder(R.drawable.logo))
                    .into(binding.collageLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    loadImage();
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
                        loadImage() ;
                       // CropImage.activity(imgUri).start(SignUpStudentActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    private void loadImage() {
        try {
            uploadFile = FileUtils.getFileFromUri(getApplicationContext(), imgUri);
            Glide.with(getApplicationContext())
                    .load(uploadFile)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_upload))
                    .into(binding.image);

            File compressFile = Utility.getCompressFile(getApplicationContext(), uploadFile);
            if (compressFile != null) {
                uploadFile = compressFile;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
                            .into(binding.image);

                    File compressFile = Utility.getCompressFile(getApplicationContext(), uploadFile);
                    if (compressFile != null) {
                        uploadFile = compressFile;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            loadImage();
            //CropImage.activity(imgUri).start(SignUpStudentActivity.this);
        }else if (requestCode == GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
            imgUri = data.getData();
            loadImage() ;

           // Uri selectedImage = data.getData();
           // CropImage.activity(selectedImage).start(SignUpStudentActivity.this);

        }
    }


    void showDialogWithButtons() {
        Utility.showDialogWithButtons(ANIMATED_DAILOG_TYPE_SUCESS, SignUpStudentActivity.this,
                "You have been successfully registered. Please wait for approval", pos -> {
                    SelectGrpClass selectGrpClass = new SelectGrpClass("" + binding.etGroup.getText().toString(),
                            "",
                            "" + binding.etClass.getText().toString(), "" + ClassId);
                    commonDB.putString(DB_SELECTED_GROUP_CLASS, new Gson().toJson(selectGrpClass));

                    Intent serviceIntent = new Intent(this, StudentRegisterService.class);
                    startService(serviceIntent);
                    if (commonDB.getString(Constants.LOGIN_RESPONSE).equalsIgnoreCase("")) {
                        if (pos == 0) {
                            Utility.userLogout(getApplicationContext());
                        } else {
                            finish();
                            startActivity(new Intent(getApplicationContext(), SignUpStudentActivity.class).putExtra("TYPE", "" + TYPE));
                        }
                    } else {
                        Utility.gotoHome(getApplicationContext());
                    }
                });

    }

    void showAnimatedDialog() {
        String des = editModel != null ? "You has been successfully update your profile" : "You have been successfully registered. Please wait for approval";
        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS, SignUpStudentActivity.this, "" + des, () -> {
            if (commonDB.getString(Constants.LOGIN_RESPONSE).equalsIgnoreCase("")) {
                Utility.userLogout(getApplicationContext());
            } else {

                Intent intent = new Intent(getApplicationContext(), StudentListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("API_FLAG", "call");
                startActivity(intent);

                // Utility.gotoHome(getApplicationContext());
            }
        });
    }

    void faceDetactCrop() {

    }
}

