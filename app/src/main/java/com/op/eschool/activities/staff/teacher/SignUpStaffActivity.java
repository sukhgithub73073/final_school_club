package com.op.eschool.activities.staff.teacher;

import static com.op.eschool.base.MyApplication.staffRegisterList;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Constants.DB_STAFF_OFFINE_LIST;
import static com.op.eschool.util.Utility.createImageFile;
import static com.op.eschool.util.Utility.fromJson;
import static com.op.eschool.util.Utility.groupFromList;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.staff.student.SignUpStudentActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySignUpStaffBinding;
import com.op.eschool.databinding.ActivityStudentListBinding;
import com.op.eschool.interfaces.BgRemoveInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.pincode_api_model.PincodeModel;
import com.op.eschool.models.pincode_api_model.PostOffice;
import com.op.eschool.models.register_all_data_model.AllInOneDtModel;
import com.op.eschool.models.school_models.GetSchoolModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.services.StaffRegisterService;
import com.op.eschool.services.StudentRegisterService;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.FileUtils;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.ImageConvert;
import com.op.eschool.util.PermissionUtil;
import com.op.eschool.util.Utility;
import com.op.eschool.util.Verhoeff;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStaffActivity extends BaseActivity {
    private static final int REQUEST_PERMISSSION = 99;
    File uploadFile;
    Map<String, String> map = new HashMap<>();

    ActivitySignUpStaffBinding binding;
    private Uri imgUri;
    Calendar startCal = Calendar.getInstance();
    GlobalLoader globalLoader;
    StaffModel editModel;
    String TYPE = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpStaffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
        globalLoader = new GlobalLoader(SignUpStaffActivity.this);
        StrictMode.VmPolicy.Builder builders = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builders.build());
        TYPE = getIntent().getStringExtra("TYPE");


        initAllData();

        requestPermissions(Utility.getPermissionsArray(), 1222);
        initSchool();
        manageClicks();
    }

    private void initAllData() {
        if (!commonDB.getString("STAFF_DETAIL").equalsIgnoreCase("")) {
            editModel = new Gson().fromJson(commonDB.getString("STAFF_DETAIL"), StaffModel.class);
            binding.onwer.setText("" + editModel.fullName);
            binding.dob.setText("" + editModel.dob);
            binding.etFname.setText("" + editModel.fatherName);
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


            binding.checkBoxAndText.setVisibility(View.GONE);
            binding.btnNext.setText("Update");
        }

        String schoolName, schoolImage;
        if (TYPE.equalsIgnoreCase("REGISTER_STAFF")) {
            SchoolModel schoolModel = Utility.GetCollageDetail(commonDB.getString("GetCollageDetail"));
            map.put("CollageCode", "" + schoolModel.getCollageCode());
            map.put("CollageName", "" + schoolModel.getCollageName());
            map.put("Unqid", "" + schoolModel.getUnqid());
            schoolName = schoolModel.getCollageName();
            schoolImage = schoolModel.getImage();
        } else {
            LoginUserModel loginUserModel = Utility.getLoginUser(getApplicationContext());
            map.put("Unqid", "" + loginUserModel.getCollageUnqid());
            map.put("CollageCode", "" + loginUserModel.collageCode);
            map.put("CollageName", "" + loginUserModel.collageName);
            schoolName = loginUserModel.getCollageName();
            schoolImage = loginUserModel.getImage();
        }

        try {
            binding.schoolName.setText("" + schoolName);
            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(schoolImage))
                    .apply(new RequestOptions().placeholder(R.drawable.logo))
                    .into(binding.collageLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void manageClicks() {
        binding.image.setOnClickListener(v -> {
            checkAllPermission("OnClickListener");
        });
        binding.dob.setOnClickListener(v -> {
            datePickerWithType();
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
        binding.btnNext.setOnClickListener(v -> {
            if (editModel == null && uploadFile == null) {
                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, SignUpStaffActivity.this, "Upload Profile Image", () -> {
                });
            } else {
                checkValidation();
            }
        });

    }

    private void checkValidation() {

        if (!Utility.isNetworkConnectedMainThred(getApplication())) {
            FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
        } else if (binding.onwer.getText().toString().equalsIgnoreCase("")) {
            binding.onwer.setError("Invalid Onwer Name");

        } else if (binding.dob.getText().toString().equalsIgnoreCase("")) {
            binding.dob.setError("Invalid Date of Birth");

        } else if (binding.qualification.getText().toString().equalsIgnoreCase("")) {
            binding.qualification.setError("Invalid Qualification");

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
            AdStaffReg();
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
    }

    private void AdStaffReg() {
        String ImageExt = "", Image = "";
        if (uploadFile != null) {
            ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
            Image = getBase64FromFile(uploadFile.getPath());
        } else if (editModel != null) {
            ImageExt = editModel.ImageExt;
            Image = editModel.Image;
        }
        map.put("type", "AdStaffReg");
        if (editModel != null) {
            map.put("type", "UpStaffEdLs");
            map.put("StaffId", "" + editModel.staffId);
        }
        if (ImageExt.equalsIgnoreCase("")) {
            ImageExt = Utility.getFileExtension(uploadFile);
        }
        map.put("FullName", binding.onwer.getText().toString().toUpperCase());
        map.put("DOB", binding.dob.getText().toString().toUpperCase());
        map.put("Gender", "");
        map.put("FatherName", "MR. " + binding.etFname.getText().toString());
        map.put("Qualification", binding.qualification.getText().toString().toUpperCase());
        map.put("AadharNo", "");
        map.put("EmailId", "");
        map.put("MobileNumber", binding.etMobile.getText().toString());
        map.put("AlternateNumber", "");
        map.put("PinCode", binding.etPincode.getText().toString());
        map.put("State", binding.etState.getText().toString().toUpperCase());
        map.put("District", binding.etDistrict.getText().toString().toUpperCase());
        map.put("Tahsil", binding.etTehsil.getText().toString().toUpperCase());
        map.put("Villa_Mohalla", binding.etVillageMohalla.getText().toString().toUpperCase());
        map.put("ImageExt", "data:image/" + ImageExt + ";");
        map.put("RelegionData", "");
        map.put("CasteData", "");
        map.put("SubCasteData", "");
        map.put("AccountNumber", "");
        map.put("ConfrAccountNumber", "");
        map.put("BankName", "");
        map.put("IfscCode", "");
        map.put("AccountHldr", "");
        FLog.w("AdStaffReg", "map>>" + new Gson().toJson(map));
        map.put("Image", "" + Image);
        String json = new Gson().toJson(map);
        if (editModel == null && binding.checkOffline.isChecked()) {
            staffRegisterList.add(map);
            //add to local list offline register here
//            List<String> stringList = commonDB.getStringList(DB_STAFF_OFFINE_LIST);
//            stringList.add(json) ;
//            commonDB.putStringList(DB_STAFF_OFFINE_LIST ,stringList) ;
            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS, SignUpStaffActivity.this, "You have been successfully registered. Please wait for approval", () -> {
                Intent serviceIntent = new Intent(this, StaffRegisterService.class);
                startService(serviceIntent);
                if (commonDB.getString(Constants.LOGIN_RESPONSE).equalsIgnoreCase("")) {
                    Utility.userLogout(getApplicationContext());
                } else {
                    Utility.gotoHome(getApplicationContext());
                }
            });
        } else {
            globalLoader.showLoader();
            webSocketManager.sendMessage(map, res -> {
                runOnUiThread(() -> {
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse = Utility.convertResponse(res);
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                            String des = editModel != null ? "You has been successfully update your profile" : "You have been successfully registered. Please wait for approval";
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS, SignUpStaffActivity.this, "" + des, () -> {
                                finish();
                            });
                        } else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, SignUpStaffActivity.this, "" + commonResponse.getMsg(), () -> {
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
                            setTehsilAdapter(response.body().get(0).getPostOffice());
                        } else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, SignUpStaffActivity.this, "No records found Please enter valid Pincode", () -> {

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

    private void initSchool() {
        try {
            String res = commonDB.getString("GetCollageDetail");
            ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<SchoolModel>>() {
                    }.getType());
            SchoolModel schoolModel = list.get(0);
            commonDB.putString("SCHOOL_DETAILS", new Gson().toJson(schoolModel));
            binding.schoolName.setText("" + schoolModel.getCollageName());
            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(schoolModel.getImage()))
                    .apply(new RequestOptions().placeholder(R.drawable.logo))
                    .into(binding.collageLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (editModel != null) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.logo)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(getApplicationContext())
                        .load(Utility.convertBase64ToBitmap(editModel.Image))
                        .apply(requestOptions)
                        .into(binding.image);
                binding.onwer.setText("" + editModel.getFullName());
                binding.dob.setText("" + editModel.getDob());
                binding.etFname.setText("" + editModel.getFatherName());
                binding.qualification.setText("" + editModel.getQualification());
                binding.etMobile.setText("" + editModel.getMobileNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void datePickerWithType() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view,
                                          int year, int monthOfYear, int dayOfMonth) {
                        //binding.dob.setText(dayOfMonth + "-"+ Utility.getMonthList().get(monthOfYear) + "-" + year);
                        int m = monthOfYear + 1;

                        binding.dob.setText(year + "-" + m + "-" + dayOfMonth);

                        try {
                            startCal.set(Calendar.DATE, dayOfMonth);
                            startCal.set(Calendar.MONTH, monthOfYear);
                            startCal.set(Calendar.YEAR, year);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },

                startCal.get(Calendar.YEAR), // Initial year selection
                startCal.get(Calendar.MONTH), // Initial year selection
                startCal.get(Calendar.DAY_OF_MONTH) // Initial year selection
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.setAccentColor(getColor(R.color.primary_color));
        }
        Calendar now = Calendar.getInstance();
        dpd.setMaxDate(now);
        dpd.show(getSupportFragmentManager(), "dob");
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
        FLog.w("sdfdsf", ">>>>>>>>>>" + new Gson().toJson(list));
        if (list.size() > 0) {
            String[] permissions = new String[list.size()];
            permissions = list.toArray(permissions);
            PermissionUtil.requestPermission(SignUpStaffActivity.this, permissions, REQUEST_PERMISSSION);
        } else if (type.equalsIgnoreCase("OnClickListener")) {

            Utility.imageNoteDialog(SignUpStaffActivity.this, t -> {
                if (t == 0) {
                    File image = createImageFile();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
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
                    CropImage.activity(imgUri)
                            .start(SignUpStaffActivity.this);
                }
            });

    ActivityResultLauncher<Intent> galleryIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        CropImage.activity(selectedImage)

                                .start(SignUpStaffActivity.this);
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
                            .into(binding.image);
                    File compressFile = Utility.getCompressFile(getApplicationContext(), uploadFile);
                    if (compressFile != null) {
                        uploadFile = compressFile;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

}