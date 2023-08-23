//package com.op.eschool.activities.staff.teacher;
//
//import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
//import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
//import static com.op.eschool.util.Utility.createImageFile;
//import static com.op.eschool.util.Utility.fromJson;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//
//import android.Manifest;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.text.method.PasswordTransformationMethod;
//import android.util.Base64;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.webkit.MimeTypeMap;
//import android.widget.AdapterView;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.material.tabs.TabLayout;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.leo.searchablespinner.SearchableSpinner;
//import com.leo.searchablespinner.interfaces.OnItemSelectListener;
//import com.op.eschool.R;
//import com.op.eschool.activities.staff.register.CollageRegisterActivity;
////import com.op.eschool.activities.staff.student.StudentRegisterActivity;
//import com.op.eschool.adapters.SpiCasteAdapter;
//import com.op.eschool.adapters.SpiRelignAdapter;
//import com.op.eschool.adapters.SpiSubCasteAdapter;
//import com.op.eschool.adapters.SpiVillageAdapter;
//import com.op.eschool.adapters.ViewPagerAdapter;
//import com.op.eschool.base.BaseActivity;
//import com.op.eschool.databinding.ActivityCollageRegisterBinding;
//import com.op.eschool.databinding.ActivityStaffRegisterBinding;
//import com.op.eschool.fragments.college.CollegeOneFragment;
//import com.op.eschool.fragments.college.CollegeTwoFragment;
//import com.op.eschool.fragments.staff.StaffOneFragment;
//import com.op.eschool.fragments.staff.StaffThreeFragment;
//import com.op.eschool.fragments.staff.StaffTwoFragment;
//import com.op.eschool.interfaces.BgRemoveInterface;
//import com.op.eschool.models.CasteModel;
//import com.op.eschool.models.CommonResponse;
//import com.op.eschool.models.DialogModel;
//import com.op.eschool.models.ReligionModel;
//import com.op.eschool.models.SubCasteModel;
//import com.op.eschool.models.bankNames.BankNameModel;
//import com.op.eschool.models.bankNames.Datum;
//import com.op.eschool.models.pincode_api_model.PincodeModel;
//import com.op.eschool.models.pincode_api_model.PostOffice;
//import com.op.eschool.models.register_all_data_model.AllInOneDtModel;
//import com.op.eschool.models.register_all_data_model.CasteArray;
//import com.op.eschool.models.register_all_data_model.ReligionArray;
//import com.op.eschool.models.school_models.GetSchoolModel;
//import com.op.eschool.models.school_models.SchoolModel;
//import com.op.eschool.models.staff.StaffModel;
//import com.op.eschool.models.student.StudentModel;
//import com.op.eschool.retrofit.RetrofitClient;
//import com.op.eschool.util.Constants;
//import com.op.eschool.util.FLog;
//import com.op.eschool.util.FToast;
//import com.op.eschool.util.FileUtils;
//import com.op.eschool.util.GlobalLoader;
//import com.op.eschool.util.ImageConvert;
//import com.op.eschool.util.PermissionUtil;
//import com.op.eschool.util.Utility;
//import com.op.eschool.util.Verhoeff;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class StaffRegisterActivity extends BaseActivity {
//    ActivityStaffRegisterBinding binding ;
//    private static final int REQUEST_PERMISSSION = 99;
//    String GENDER ="" ;
//    File uploadFile ;
//    List<TabLayout.Tab> tabList = new ArrayList<>() ;
//    private Uri imgUri;
//    Calendar startCal = Calendar.getInstance() ;
//    GetSchoolModel  getSchoolModel ;
//    SchoolModel schoolModel ;
//    GlobalLoader globalLoader ;
//    StaffModel editModel ;
//    AllInOneDtModel allInOneDtModel ;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this ,R.layout.activity_staff_register);
//        globalLoader = new GlobalLoader(StaffRegisterActivity.this) ;
//        binding.back.setOnClickListener(v->{onBackPressed();});
//        allInOneDtModel = new Gson().fromJson(commonDB.getString("AllInOneDt") ,AllInOneDtModel.class) ;
//
//        StrictMode.VmPolicy.Builder builders = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builders.build());
//        getSchoolModel = Utility.ChkSchlCode(commonDB.getString("ChkSchlCode")) ;
//        if (!commonDB.getString("STAFF_DETAIL").equalsIgnoreCase("")) {
//            editModel = new Gson().fromJson(commonDB.getString("STAFF_DETAIL"), StaffModel.class);
//        }
//        requestPermissions(Utility.getPermissionsArray(), 1222);
//        initTabLayout() ;
//        initSchool() ;
//
//    }
//
//    private void initSchool() {
//        try {
//            String res = commonDB.getString("GetCollageDetail");
//            ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) fromJson(res,
//                    new TypeToken<ArrayList<SchoolModel>>() {
//                    }.getType());
//            schoolModel = list.get(0) ;
//            commonDB.putString("SCHOOL_DETAILS" , new Gson().toJson(schoolModel)) ;
//            binding.schoolName.setText("" +schoolModel.getCollageName()) ;
//            Glide.with(getApplicationContext())
//                    .load(Utility.convertBase64ToBitmap(schoolModel.getImage()))
//                    .apply(new RequestOptions().placeholder(R.drawable.logo))
//                    .into(binding.collageLogo)   ;
//
//        }catch (Exception e){e.printStackTrace();}
//
//
//    }
//    private void initTabLayout() {
//        TabLayout.Tab firstTab = binding.tabLayout.newTab();
//        firstTab.setText("Information");
//        binding.tabLayout.addTab(firstTab);
//        tabList.add(firstTab) ;
//        checkAllPermission("onCreate");
//
//        TabLayout.Tab tab2 = binding.tabLayout.newTab();
//        tab2.setText("Address");
//        binding.tabLayout.addTab(tab2);
//        tabList.add(tab2) ;
//        TabLayout.Tab tab3 = binding.tabLayout.newTab();
//        tab3.setText("Bank Detail");
//        binding.tabLayout.addTab(tab3);
//        tabList.add(tab3) ;
//
//        List<Fragment> fragmentList = new ArrayList<>() ;
//        fragmentList.add(new StaffOneFragment(()->{
//            manageOneClicks() ;
//        })) ;
//        fragmentList.add(new StaffTwoFragment(()->{
//            manageTwoClicks() ;
//        })) ;
//        fragmentList.add(new StaffThreeFragment(()->{
//            manageThreeClicks() ;
//        })) ;
//
//        binding.viewPager.setUserInputEnabled(false);
//        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                binding.viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(StaffRegisterActivity.this , fragmentList);
//        binding.viewPager.setAdapter(viewPagerAdapter) ;
//    }
//
//    private void manageThreeClicks() {
//        getBankList() ;
//        if (editModel!=null) {
//            bindingStaffThree.etAcc.setText("" + editModel.getAccountNumber());
//            bindingStaffThree.confirmAcc.setText("" + editModel.getConfrAccountNumber());
//            bindingStaffThree.etBank.setText("" + editModel.getBankName());
//            bindingStaffThree.etIfsc.setText("" + editModel.getIfscCode());
//            bindingStaffThree.etHolder.setText("" + editModel.getAccountHldr());
//        }
//        bindingStaffThree.etAcc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean passwordVisible) {
//                if (!passwordVisible) {
//                    bindingStaffThree.etAcc.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                } else {
//                    bindingStaffThree.etAcc.setTransformationMethod(null);
//                }
//            }
//        });
//        bindingStaffThree.confirmAcc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean passwordVisible) {
//                if (!passwordVisible) {
//                    bindingStaffThree.confirmAcc.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                } else {
//                    bindingStaffThree.confirmAcc.setTransformationMethod(null);
//                }
//            }
//        });
//
//        bindingStaffThree.confirmAcc.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!bindingStaffThree.etAcc.getText().toString().equalsIgnoreCase(bindingStaffThree.confirmAcc.getText().toString())){
//                    bindingStaffThree.confirmAcc.setError("Invalid Confirm Account") ;
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        bindingStaffThree.btnNext.setOnClickListener(v->{
//            if (editModel == null && uploadFile==null){
//                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StaffRegisterActivity.this , "Upload Profile Image" ,()->{
//                    changePosition(0) ;
//                }) ;
//            }else{
//                checkValidation();
//            }
//        });
//    }
//
//    private void manageOneClicks() {
//        try {
//            if (editModel!=null){
//                Glide.with(getApplicationContext())
//                        .load(Utility.convertBase64ToBitmap(editModel.Image))
//                        .apply(new RequestOptions().placeholder(R.drawable.logo))
//                        .into(bindingStaffOne.image)   ;
//                bindingStaffOne.onwer.setText(""+ editModel.getFullName());
//                bindingStaffOne.dob.setText(""+ editModel.getDob());
//                if (editModel.getGender().equalsIgnoreCase("MALE")){
//                    bindingStaffOne.male.setChecked(true);
//                }else if (editModel.getGender().equalsIgnoreCase("FEMALE")){
//                    bindingStaffOne.female.setChecked(true);
//                }else if (editModel.getGender().equalsIgnoreCase("OTHER")){
//                    bindingStaffOne.other.setChecked(true);
//                }
//                bindingStaffOne.fname.setText(""+ editModel.getFatherName());
//                bindingStaffOne.qualification.setText(""+ editModel.getQualification());
//                bindingStaffOne.aadhaar.setText(""+ editModel.getAadharNo());
//                bindingStaffOne.email.setText(""+ editModel.getEmailId());
//
//            }
//        }catch (Exception e){e.printStackTrace();}
//        bindingStaffOne.image.setOnClickListener(v->{
//            checkAllPermission("OnClickListener");
//        });
//        bindingStaffOne.aadhaar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (bindingStaffOne.aadhaar.getText().toString().length() ==12){
//                    if (!Verhoeff.validateVerhoeff(bindingStaffOne.aadhaar.getText().toString())){
//                        bindingStaffOne.aadhaar.setError("Enter correct Aadhaar Number");
//                    }
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        bindingStaffOne.email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    changePosition(1);
//                }
//                return false;
//            }
//        });
//
//        bindingStaffOne.dob.setOnClickListener(v->{
//            datePickerWithType();
//        });
//
//
//
//
//
//
//    }
//
//    void datePickerWithType(){
//        DatePickerDialog dpd = DatePickerDialog.newInstance(
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePickerDialog view,
//                                          int year, int monthOfYear, int dayOfMonth) {
//                        //bindingStaffOne.dob.setText(dayOfMonth + "-"+ Utility.getMonthList().get(monthOfYear) + "-" + year);
//                        int m = monthOfYear+ 1 ;
//
//                        bindingStaffOne.dob.setText(year + "-"+ m + "-" + dayOfMonth);
//
//                        try {
//                            startCal.set(Calendar.DATE ,dayOfMonth);
//                            startCal.set(Calendar.MONTH ,monthOfYear);
//                            startCal.set(Calendar.YEAR ,year);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//
//                startCal.get(Calendar.YEAR), // Initial year selection
//                startCal.get(Calendar.MONTH), // Initial year selection
//                startCal.get(Calendar.DAY_OF_MONTH) // Initial year selection
//        );
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            dpd.setAccentColor(getColor(R.color.primary_color)) ;
//        }
//        Calendar now = Calendar.getInstance();
//        dpd.setMaxDate(now) ;
//        dpd.show(getSupportFragmentManager() , "dob");
//    }
//
//
//    private void manageTwoClicks() {
//        setReligionAdapter() ;
//        setCasteAdapter() ;
//        if (editModel!=null) {
//            bindingStaffTwo.etMobile.setText("" + editModel.getMobileNumber());
//            bindingStaffTwo.etAltMobile.setText("" + editModel.getAlternateNumber());
//            bindingStaffTwo.etPincode.setText("" + editModel.getPinCode());
//            bindingStaffTwo.etState.setText("" + editModel.getState());
//            bindingStaffTwo.etDistrict.setText("" + editModel.getDistrict());
//            bindingStaffTwo.etTehsil.setText("" + editModel.getTahsil());
//            bindingStaffTwo.etVillageMohalla.setText("" + editModel.getVillaMohalla());
//            bindingStaffTwo.etRelegion.setText("" + editModel.getRelegionData());
//            bindingStaffTwo.etCaste.setText("" + editModel.getCasteData());
//            bindingStaffTwo.etSubCaste.setText("" + editModel.getSubCasteData());
//        }
//
//
//        bindingStaffTwo.etDesignation.setOnClickListener(v->{
//            bindingStaffTwo.spiDesignation.performClick() ;
//        });
//        bindingStaffTwo.etPincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(bindingStaffTwo.etPincode.length() == 6){
//                    PincodeData() ;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }
//    void changePosition(int pos){
//        if (binding.viewPager.getCurrentItem() != pos){
//            binding.viewPager.setCurrentItem(pos) ;
//            binding.tabLayout.selectTab(tabList.get(pos)) ;
//        }
//    }
//    private void checkValidation() {
//        GENDER="" ;
//        if (bindingStaffOne.male.isChecked()){
//            GENDER ="MALE" ;
//        }
//        if (bindingStaffOne.female.isChecked()){
//            GENDER ="FEMALE" ;
//        }
//        if (bindingStaffOne.other.isChecked()){
//            GENDER ="OTHER" ;
//        }
//        if (!Utility.isNetworkConnectedMainThred(getApplication())){
//            FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
//        }else if (bindingStaffOne.onwer.getText().toString().equalsIgnoreCase("")){
//            bindingStaffOne.onwer.setError("Invalid Onwer Name");
//            changePosition(0);
//        }else if (bindingStaffOne.dob.getText().toString().equalsIgnoreCase("")){
//            bindingStaffOne.dob.setError("Invalid Date of Birth");
//            changePosition(0);
//        }else if (GENDER.equalsIgnoreCase("")){
//            FToast.makeText(getApplication(), "Please Select Gender", FToast.LENGTH_SHORT).show();
//            changePosition(0);
//        }else if (bindingStaffOne.fname.getText().toString().equalsIgnoreCase("")){
//            bindingStaffOne.fname.setError("Invalid Father Name");
//            changePosition(0);
//        }else if (bindingStaffOne.qualification.getText().toString().equalsIgnoreCase("")){
//            bindingStaffOne.qualification.setError("Invalid Qualification");
//            changePosition(0);
//        }else if (bindingStaffOne.aadhaar.getText().toString().length() != 12){
//            bindingStaffOne.aadhaar.setError("Invalid Aadhaar Number");
//            changePosition(0);
//        }else if (!Verhoeff.validateVerhoeff(bindingStaffOne.aadhaar.getText().toString())){
//            bindingStaffOne.aadhaar.setError("Enter a valid aadhaar number");
//            changePosition(0);
//        }else if (!bindingStaffOne.email.getText().toString().toLowerCase().contains("@gmail.com")){
//            bindingStaffOne.email.setError("Invalid Email Address");
//            changePosition(0);
//        }else if (bindingStaffTwo.etMobile.getText().toString().length() != 10){
//            bindingStaffTwo.etMobile.setError("Invalid Mobile Number");
//            changePosition(1);
//        }else if (bindingStaffTwo.etAltMobile.getText().toString().length() != 10){
//            bindingStaffTwo.etAltMobile.setError("Invalid Alternate Mobile Number");
//            changePosition(1);
//        }else if (bindingStaffTwo.etMobile.getText().toString().equalsIgnoreCase(bindingStaffTwo.etAltMobile.getText().toString())){
//            bindingStaffTwo.etAltMobile.setError("Enter Different Alternate Mobile Number");
//            changePosition(1);
//        }else if (bindingStaffTwo.etPincode.getText().toString().length() != 6){
//            bindingStaffTwo.etPincode.setError("Invalid Pincode");
//            changePosition(1);
//        }else if (bindingStaffTwo.etTehsil.getText().toString().equalsIgnoreCase("")){
//            bindingStaffTwo.etTehsil.setError("Invalid Tehsil");
//            changePosition(1);
//        }else if (bindingStaffTwo.etVillageMohalla.getText().toString().equalsIgnoreCase("")){
//            bindingStaffTwo.etVillageMohalla.setError("Invalid Village Mohalla");
//            changePosition(1);
//        }else if (bindingStaffTwo.etRelegion.getText().toString().equalsIgnoreCase("")){
//            bindingStaffTwo.etRelegion.setError("Invalid Relegion");
//            changePosition(1);
//        }else if (bindingStaffTwo.etCaste.getText().toString().equalsIgnoreCase("")){
//            bindingStaffTwo.etCaste.setError("Invalid Caste");
//            changePosition(1);
//        }else if (bindingStaffTwo.etSubCaste.getText().toString().equalsIgnoreCase("")){
//            bindingStaffTwo.etSubCaste.setError("Invalid Sub Caste");
//            changePosition(1);
//        }else if (bindingStaffThree.etAcc.getText().toString().length() < 6){
//            bindingStaffThree.etAcc.setError("Invalid Account Number");
//            changePosition(2);
//        } else if (!bindingStaffThree.etAcc.getText().toString().equalsIgnoreCase(bindingStaffThree.confirmAcc.getText().toString())){
//            bindingStaffThree.confirmAcc.setError("Invalid Confirm Account Number");
//            changePosition(2);
//        }else if (bindingStaffThree.etBank.getText().toString().equalsIgnoreCase("")){
//            bindingStaffThree.etBank.setError("Invalid Bank Name");
//            changePosition(2);
//        } else if (bindingStaffThree.etIfsc.getText().toString().equalsIgnoreCase("")){
//            bindingStaffThree.etIfsc.setError("Invalid IFSC");
//            changePosition(2);
//        } else if (!Utility.isValidIFSCCode(bindingStaffThree.etIfsc.getText().toString())){
//            bindingStaffThree.etIfsc.setError("Invalid IFSC");
//            changePosition(2);
//        } else if (bindingStaffThree.etHolder.getText().toString().equalsIgnoreCase("")){
//            bindingStaffThree.etHolder.setError("Invalid Holder Name");
//            changePosition(2);
//        } else if (!bindingStaffThree.checkBox.isChecked()) {
//            FToast.makeText(getApplicationContext(),"Apply Terms & Conditions",FToast.LENGTH_SHORT).show();
//            changePosition(2);
//        }else{
//            AdStaffReg() ;
//        }
//    }
//    public  String getBase64FromFile(String path) {
//        Bitmap bmp = null;
//        ByteArrayOutputStream baos = null;
//        byte[] baat = null;
//        String encodeString = null;
//        try{
//            bmp = BitmapFactory.decodeFile(path);
//            baos = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            baat = baos.toByteArray();
//            encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return encodeString;
//    }
//
//
//    private void AdStaffReg() {
//
//        String ImageExt ="" ,Image="" ;
//        if (uploadFile != null){
//            ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
//            Image = getBase64FromFile(uploadFile.getPath()) ;
//        }else if (editModel != null){
//            ImageExt = editModel.ImageExt ;
//            Image = editModel.Image ;
//        }
//
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"AdStaffReg") ;
//        if (editModel != null) {
//            map.put("type" ,"UpStaffEdLs") ;
//        }
//        map.put("CollageCode" ,""+getSchoolModel.getCollageCode() ) ;
//        map.put("CollageName" ,""+getSchoolModel.getCollageName()) ;
//        map.put("FullName" , bindingStaffOne.onwer.getText().toString().toUpperCase()) ;
//        map.put("DOB" , bindingStaffOne.dob.getText().toString().toUpperCase()) ;
//        map.put("Gender" ,GENDER.toUpperCase() ) ;
//        map.put("FatherName" , bindingStaffOne.fname.getText().toString().toUpperCase()) ;
//        map.put("Qualification" , bindingStaffOne.qualification.getText().toString().toUpperCase()) ;
//        map.put("AadharNo" , bindingStaffOne.aadhaar.getText().toString()) ;
//        map.put("EmailId" , bindingStaffOne.email.getText().toString().toUpperCase()) ;
//        map.put("MobileNumber" , bindingStaffTwo.etMobile.getText().toString()) ;
//        map.put("AlternateNumber" , bindingStaffTwo.etAltMobile.getText().toString()) ;
//        map.put("PinCode" , bindingStaffTwo.etPincode.getText().toString()) ;
//        map.put("State" , bindingStaffTwo.etState.getText().toString().toUpperCase()) ;
//        map.put("District" , bindingStaffTwo.etDistrict.getText().toString().toUpperCase()) ;
//        map.put("Tahsil" , bindingStaffTwo.etTehsil.getText().toString().toUpperCase()) ;
//        map.put("Villa_Mohalla" , bindingStaffTwo.etVillageMohalla.getText().toString().toUpperCase()) ;
//        map.put("ImageExt" ,"data:image/" + ImageExt + ";") ;
//        map.put("RelegionData" , ""+bindingStaffTwo.etRelegion.getText().toString().toUpperCase()) ;
//        map.put("CasteData" , "" + bindingStaffTwo.etCaste.getText().toString().toUpperCase()) ;
//        map.put("SubCasteData" , ""+bindingStaffTwo.etSubCaste.getText().toString().toUpperCase()) ;
//        map.put("AccountNumber" ,bindingStaffThree.etAcc.getText().toString()) ;
//        map.put("ConfrAccountNumber" ,bindingStaffThree.confirmAcc.getText().toString()) ;
//        map.put("BankName" ,bindingStaffThree.etBank.getText().toString().toUpperCase()) ;
//        map.put("IfscCode" ,bindingStaffThree.etIfsc.getText().toString().toUpperCase()) ;
//        map.put("AccountHldr" ,bindingStaffThree.etHolder.getText().toString().toUpperCase()) ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//
//        FLog.w("AdStaffReg" , "map>>" + new Gson().toJson(map)) ;
//        map.put("Image" ,"" + Image) ;
//        String json = new Gson().toJson(map) ;
//        globalLoader.showLoader();
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                try {
//                    globalLoader.dismissLoader();
//                    CommonResponse commonResponse = Utility.convertResponse(res) ;
//                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
//                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , StaffRegisterActivity.this , "You has been successfully registered please wait for approvale" ,()->{
//                            finish() ;
//                        }) ;
//                    }else {
//                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StaffRegisterActivity.this , ""+commonResponse.getMsg() ,()->{
//                        }) ;
//                    }
//                }catch (Exception e){e.printStackTrace();}
//            }) ;
//
//        });
//
//    }
//
//    private void PincodeData() {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"PincodeData") ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        map.put("Pincode" , "" + bindingStaffTwo.etPincode.getText().toString()) ;
//        String json = new Gson().toJson(map) ;
//        globalLoader.showLoader();
//        String url ="https://api.postalpincode.in/pincode/"+ bindingStaffTwo.etPincode.getText().toString() ;
//        RetrofitClient.getRetrofitInstance().pincodeApi(url).enqueue(new Callback<List<PincodeModel>>() {
//            @Override
//            public void onResponse(Call<List<PincodeModel>> call, Response<List<PincodeModel>> response) {
//                try {
//                    globalLoader.dismissLoader();
//                    if (response.body().size() > 0){
//                        if (response.body().get(0).getStatus().equalsIgnoreCase("Success")){
//                            bindingStaffTwo.etState.setText("" + response.body().get(0).getPostOffice().get(0).getState()) ;
//                            bindingStaffTwo.etDistrict.setText("" + response.body().get(0).getPostOffice().get(0).getDistrict()) ;
//                            setTehsilAdapter(response.body().get(0).getPostOffice()) ;
//                        }else {
//                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StaffRegisterActivity.this , "No records found Please enter valid Pincode" ,()->{
//
//                            }) ;
//                        }
//
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<PincodeModel>> call, Throwable t) {
//                globalLoader.dismissLoader();
//            }
//        });
//
//    }
//    List<PostOffice> filterTehsilList(List<PostOffice> list){
//        List<PostOffice> rtnList = new ArrayList<>() ;
//        List<String> tehsilList = new ArrayList<>() ;
//        for (PostOffice postOffice : list){
//            if (!tehsilList.contains(postOffice.getBlock())){
//                tehsilList.add(postOffice.getBlock()) ;
//            }
//        }
//        for (String s :tehsilList ){
//            PostOffice p =  new PostOffice() ;
//            p.setBlock(s) ;
//            rtnList.add(p);
//        }
//        return rtnList ;
//    }
//    private void setTehsilAdapter(List<PostOffice> postOffices) {
//        List<PostOffice> list = filterTehsilList(postOffices) ;
//        SearchableSpinner tehsil = new SearchableSpinner(this);
//        tehsil.setWindowTitle("Select Tehsil") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , PostOffice> classMap = new HashMap<>() ;
//        for (PostOffice s : list){
//            strList.add(s.getBlock()) ;
//            classMap.put(""+s.getBlock() ,  s) ;
//        }
//        tehsil.setSpinnerListItems(strList);
//        tehsil.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStaffTwo.etTehsil.setText(selectedString) ;
//            }
//        });
//        bindingStaffTwo.etTehsil.setOnClickListener(v->{
//            tehsil.setHighlightSelectedItem(true);
//            tehsil.show();
//        });
//
//
//    }
//
//    private void setVillageAdapter(List<PostOffice> list) {
//        SpiVillageAdapter spiVillageAdapter = new SpiVillageAdapter("VILLAGE" , StaffRegisterActivity.this ,R.layout.operater_spinner_adapter_item  , list) ;
//        bindingStaffTwo.spiVillage.setAdapter(spiVillageAdapter);
//
//        bindingStaffTwo.etVillageMohalla.setOnClickListener(v->{
//            bindingStaffTwo.spiVillage.performClick();
//        });
//        bindingStaffTwo.spiVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                bindingStaffTwo.etVillageMohalla.setText(list.get(position).getName());
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//
//    }
//
//
//
//    private void checkAllPermission(String type) {
//        List<String> list = new ArrayList<>() ;
//        if (!PermissionUtil.checkPermisiion(getApplicationContext() , "android.permission.CAMERA")){
//            list.add(Manifest.permission.CAMERA) ;
//        }
//        if (!PermissionUtil.checkPermisiion(getApplicationContext() , Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU?"android.permission.READ_MEDIA_IMAGES":"android.permission.WRITE_EXTERNAL_STORAGE")){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                list.add(Manifest.permission.READ_MEDIA_IMAGES) ;
//            }else{
//                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
//            }
//        }
//        if (list.size()>0){
//            String[] permissions = new String[list.size()];
//            permissions = list.toArray(permissions);
//            PermissionUtil.requestPermission(StaffRegisterActivity.this ,permissions , REQUEST_PERMISSSION) ;
//        }else if (type.equalsIgnoreCase("OnClickListener")){
//
//
//
//            DialogModel dialogModel = new DialogModel(StaffRegisterActivity.this ,"Take Image","Take photo from Gallery or take new picture using Camera !","Camera","Gallery", t->{
//                if (t.equalsIgnoreCase("POSTIVE")){
//                    //open camera
//                    File image = createImageFile();
//                    imgUri = Uri.fromFile(image);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
//                    cameraIntent.launch(intent);
//                }else{
//                    //open gallery
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    galleryIntent.launch(pickPhoto);
//
//                }
//            } ) ;
//            Utility.wantTOSureDialog(dialogModel) ;
//
//        }
//
//    }
//
//    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    CropImage.activity(imgUri)
//
//                                .start(StaffRegisterActivity.this);
//                }
//            });
//
//    ActivityResultLauncher<Intent> galleryIntent = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    try {
//                        Intent data = result.getData() ;
//                        Uri selectedImage = data.getData();
//                        CropImage.activity(selectedImage)
//
//                                .start(StaffRegisterActivity.this);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//                    removeBG(bitmap) ;
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        }
//    }
//    private void setReligionAdapter() {
//
//        SearchableSpinner spinner = new SearchableSpinner(this);
//        spinner.setWindowTitle("Select Relegion") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , ReligionArray> classMap = new HashMap<>() ;
//        for (ReligionArray s : allInOneDtModel.getData().get(0).getReligionArray()){
//            strList.add(s.getReligionName()) ;
//            classMap.put(""+s.getReligionName() ,  s) ;
//        }
//        spinner.setSpinnerListItems(strList);
//        spinner.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStaffTwo.etRelegion.setText(selectedString) ;
//            }
//        });
//        bindingStaffTwo.etRelegion.setOnClickListener(v->{
//            spinner.setHighlightSelectedItem(true);
//            spinner.show();
//        });
//
//
//    }
//
//
//    void removeBG(Bitmap bitmap){
//        bindingStaffOne.progressBar.setVisibility(View.VISIBLE) ;
//        new ImageConvert().ImageConvertLocal(bitmap, new BgRemoveInterface() {
//            @Override
//            public void onSuccess(@NonNull Bitmap bitmap) {
//                try {
//                    bindingStaffOne.progressBar.setVisibility(View.GONE) ;
//                    FLog.w("removeBG" , "onSuccess");
//
//                    uploadFile = Utility.bitmapToFile(getApplicationContext() , bitmap ,"img_"+System.currentTimeMillis()) ;
//                    Glide.with(getApplicationContext())
//                            .load(bitmap)
//                            .apply(new RequestOptions().placeholder(R.drawable.placeholder_upload))
//                            .into(bindingStaffOne.image)   ;
//                }catch (Exception e){e.printStackTrace();}
//            }
//
//            @Override
//            public void onFailed(@NonNull Exception exception) {
//                FLog.w("removeBG" , "onFailed" + exception);
//                bindingStaffOne.progressBar.setVisibility(View.GONE) ;
//                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StaffRegisterActivity.this , ""+exception.getMessage() ,()->{
//                }) ;
//            }
//        });
//    }
//    void getBankList(){
//        String url ="http://rupeeepay.com/Android/getBankInstant" ;
//        RetrofitClient.getRetrofitInstance().getBankInstantAeps(url ,new HashMap<>()).enqueue(new Callback<BankNameModel>() {
//            @Override
//            public void onResponse(Call<BankNameModel> call, Response<BankNameModel> response) {
//                FLog.w("getBankList" , "onResponse" + new Gson().toJson(response.body())) ;
//                setBankSpinner(response.body().getData()) ;
//            }
//            @Override
//            public void onFailure(Call<BankNameModel> call, Throwable t) {
//                FLog.w("getBankList" , "onFailure" + new Gson().toJson(t.toString())) ;
//
//            }
//        });
//    }
//
//    private void setBankSpinner(List<Datum> list) {
//        SearchableSpinner caste = new SearchableSpinner(this);
//        caste.setWindowTitle("Select Bank") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , Datum> classMap = new HashMap<>() ;
//        for (Datum s : list){
//            strList.add(s.getBankName()) ;
//            classMap.put(""+s.getBankName() ,  s) ;
//        }
//        caste.setSpinnerListItems(strList);
//        caste.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStaffThree.etBank.setText(selectedString) ;
//            }
//        });
//        bindingStaffThree.etBank.setOnClickListener(v->{
//            caste.setHighlightSelectedItem(true);
//            caste.show();
//        });
//    }
//
//    private void setCasteAdapter() {
//
//        SearchableSpinner spinner = new SearchableSpinner(this);
//        spinner.setWindowTitle("Select Caste") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , CasteArray> map = new HashMap<>() ;
//        for (CasteArray s : allInOneDtModel.getData().get(0).getCasteArray()){
//            strList.add(s.getCasteName()) ;
//            map.put(""+s.getCasteName() ,  s) ;
//        }
//        spinner.setSpinnerListItems(strList);
//        spinner.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStaffTwo.etCaste.setText(selectedString) ;
//                bindingStaffTwo.etSubCaste.setText("") ;
//                SubCasteData(map.get(selectedString).getCasteId()) ;
//            }
//        });
//        bindingStaffTwo.etCaste.setOnClickListener(v->{
//            spinner.setHighlightSelectedItem(true);
//            spinner.show();
//        });
//
//
//
//    }
//    private void SubCasteData(String caste_id) {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"SubCasteData") ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        map.put("caste_id" ,caste_id) ;
//        globalLoader.showLoader();
//
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                try {
//                    globalLoader.dismissLoader();
//                    setSubCasteAdapter(Utility.convertSubCasteList(res));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }) ;
//    }
//    private void setSubCasteAdapter(List<SubCasteModel> list) {
//        SearchableSpinner caste = new SearchableSpinner(this);
//        caste.setWindowTitle("Select Sub Caste") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , SubCasteModel> classMap = new HashMap<>() ;
//        for (SubCasteModel s : list){
//            strList.add(s.getSubcasteName()) ;
//            classMap.put(""+s.getSubcasteName() ,  s) ;
//        }
//        caste.setSpinnerListItems(strList);
//        caste.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStaffTwo.etSubCaste.setText(selectedString) ;
//            }
//        });
//        bindingStaffTwo.etSubCaste.setOnClickListener(v->{
//            caste.setHighlightSelectedItem(true);
//            caste.show();
//        });
//
//    }
//}