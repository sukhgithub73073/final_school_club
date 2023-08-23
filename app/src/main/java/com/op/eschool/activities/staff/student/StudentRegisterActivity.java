//package com.op.eschool.activities.staff.student;
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
//import androidx.annotation.RequiresApi;
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
//import android.text.TextWatcher;
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
//import com.op.eschool.adapters.SubjectSelectionAdapter;
//import com.op.eschool.adapters.ViewPagerAdapter;
//import com.op.eschool.base.BaseActivity;
//import com.op.eschool.databinding.ActivityStudentRegisterBinding;
//import com.op.eschool.fragments.student.StudentOneFragment;
//import com.op.eschool.fragments.student.StudentThreeFragment;
//import com.op.eschool.fragments.student.StudentTwoFragment;
//import com.op.eschool.interfaces.BgRemoveInterface;
//import com.op.eschool.models.CasteModel;
//import com.op.eschool.models.CommonResponse;
//import com.op.eschool.models.DialogModel;
//import com.op.eschool.models.ReligionModel;
//import com.op.eschool.models.SubCasteModel;
//import com.op.eschool.models.class_models.AllClassModel;
//import com.op.eschool.models.class_models.ClassGroupModel;
//import com.op.eschool.models.class_models.ClassModel;
//import com.op.eschool.models.class_models.SubjectModel;
//import com.op.eschool.models.parents_model.OccupationModel;
//import com.op.eschool.models.pincode_api_model.PincodeModel;
//import com.op.eschool.models.pincode_api_model.PostOffice;
//import com.op.eschool.models.register_all_data_model.AllInOneDtModel;
//import com.op.eschool.models.register_all_data_model.CasteArray;
//import com.op.eschool.models.register_all_data_model.OcupationArray;
//import com.op.eschool.models.register_all_data_model.ReligionArray;
//import com.op.eschool.models.school_models.GetSchoolModel;
//import com.op.eschool.models.school_models.SchoolModel;
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
//import org.jetbrains.annotations.NotNull;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//public class StudentRegisterActivity extends BaseActivity {
//    private static final int REQUEST_PERMISSSION = 99;
//    ActivityStudentRegisterBinding binding ;
//    String GENDER ="" ;
//    File uploadFile ;
//    List<TabLayout.Tab> tabList = new ArrayList<>() ;
//    ClassModel selectedClass ;
//    String RESPONSE="";
//    Calendar startCal = Calendar.getInstance() ;
//    private Uri imgUri;
//    GetSchoolModel  getSchoolModel ;
//    List<ClassGroupModel> groupModelList  =new ArrayList<>() ;
//    ClassGroupModel groupModel ;
//    SchoolModel schoolModel ;
//    GlobalLoader globalLoader ;
//
//    boolean  group = false , class_flag = false, fOccupaction = false,mOccupaction = false, tehsil = false ,caste_flag = false , sub_caste = false , relegion = false  ;
//    SearchableSpinner groupSpinner;
//    Map<String ,SubjectModel> subjectMap = new HashMap<>() ;
//    String SELECT_SUBJECT="[]" ;
//    StudentModel editModel ;
//    AllInOneDtModel allInOneDtModel ;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this , R.layout.activity_student_register) ;
//        globalLoader = new GlobalLoader(StudentRegisterActivity.this) ;
//        if (!commonDB.getString("STUDENT_DETAIL").equalsIgnoreCase("")) {
//            editModel = new Gson().fromJson(commonDB.getString("STUDENT_DETAIL"), StudentModel.class);
//        }
//        allInOneDtModel = new Gson().fromJson(commonDB.getString("AllInOneDt") ,AllInOneDtModel.class) ;
//        binding.back.setOnClickListener(v->{onBackPressed();});
//        StrictMode.VmPolicy.Builder builders = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builders.build());
//        getSchoolModel = Utility.ChkSchlCode(commonDB.getString("ChkSchlCode")) ;
//        requestPermissions(Utility.getPermissionsArray(), 12) ;
//        initTabLayout() ;
//        initSchool() ;
//    }
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
//        }catch (Exception e){e.printStackTrace();}
//    }
//    private void initTabLayout() {
//
//        TabLayout.Tab firstTab = binding.tabLayout.newTab();
//        firstTab.setText("School");
//        binding.tabLayout.addTab(firstTab);
//        tabList.add(firstTab) ;
//        // checkAllPermission("onCreate");
//
//        TabLayout.Tab tab2 = binding.tabLayout.newTab();
//        tab2.setText("Information");
//        binding.tabLayout.addTab(tab2);
//        tabList.add(tab2) ;
//        TabLayout.Tab tab3 = binding.tabLayout.newTab();
//        tab3.setText("Address");
//        binding.tabLayout.addTab(tab3);
//        tabList.add(tab3) ;
//        List<Fragment> fragmentList = new ArrayList<>() ;
//
//        fragmentList.add(new StudentOneFragment(()->{
//            manageOneClicks() ;
//        })) ;
//        fragmentList.add(new StudentTwoFragment(()->{
//            manageTwoClicks() ;
//        })) ;
//        fragmentList.add(new StudentThreeFragment(()->{
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
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(StudentRegisterActivity.this , fragmentList);
//        binding.viewPager.setAdapter(viewPagerAdapter) ;
//
//
//    }
//
//
//    private void manageThreeClicks() {
//
//        setRelegionData() ;
//        setCasteAdapter();
//
//
//        if (editModel!=null){
//            bindingStudentThree.etMobile.setText("" + editModel.mobileNumber);
//            bindingStudentThree.etAltMobile.setText("" + editModel.alternateNumber);
//            bindingStudentThree.etPincode.setText("" + editModel.pinCode);
//            bindingStudentThree.etState.setText("" + editModel.state);
//            bindingStudentThree.etDistrict.setText("" + editModel.district);
//            bindingStudentThree.etTehsil.setText("" + editModel.tahsil);
//            bindingStudentThree.etVillageMohalla.setText("" + editModel.villaMohalla);
//            bindingStudentThree.etRelegion.setText("" + editModel.relegionData);
//            bindingStudentThree.etCaste.setText("" + editModel.casteData);
//            bindingStudentThree.etSubCaste.setText("" + editModel.subCasteData);
//        }
//        bindingStudentThree.etPincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(bindingStudentThree.etPincode.length() == 6){
//                    PincodeData() ;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        bindingStudentThree.btnNext.setOnClickListener(v->{
//            if (editModel==null && uploadFile==null){
//                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentRegisterActivity.this , "Upload Profile Image" ,()->{
//                }) ;
//            }else{
//                checkValidation();
//            }
//        });
//    }
//
//    private void setRelegionData() {
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
//                bindingStudentThree.etRelegion.setText(selectedString) ;
//            }
//        });
//        bindingStudentThree.etRelegion.setOnClickListener(v->{
//            spinner.setHighlightSelectedItem(true);
//            spinner.show();
//        });
//    }
//    private void manageOneClicks() {
//
//        try {
//            if (editModel!=null){
//                bindingStudentOne.etGroup.setText("" + editModel.GroupName) ;
//                bindingStudentOne.etClass.setText("" + editModel._class) ;
//                Glide.with(getApplicationContext())
//                        .load(Utility.convertBase64ToBitmap(editModel.Image))
//                        .apply(new RequestOptions().placeholder(R.drawable.logo))
//                        .into(bindingStudentOne.image)   ;
//            }
//        }catch (Exception e){e.printStackTrace();}
//
//        bindingStudentOne.image.setOnClickListener(v->{
//            checkAllPermission("OnClickListener");
//        });
//        ClassGrpAdded() ;
//    }
//    private void setClassesAdapter(List<ClassModel> list) {
//
//        SearchableSpinner classSpinner = new SearchableSpinner(this);
//        classSpinner.setWindowTitle("Select Class Group") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , ClassModel> classMap = new HashMap<>() ;
//        for (ClassModel s : list){
//            strList.add(s.getClassName()) ;
//            classMap.put(""+s.getClassName() ,  s) ;
//        }
//        classSpinner.setSpinnerListItems(strList);
//        classSpinner.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStudentOne.etClass.setText(list.get(position).getName());
//                selectedClass = classMap.get(selectedString) ;
//                bindingStudentOne.linSubject.setVisibility(View.GONE) ;
//                changePosition(1);
//
//
////                if (selectedString.contains("9")||selectedString.contains("10")||selectedString.contains("11")||selectedString.contains("12")){
////                    bindingStudentOne.linSubject.setVisibility(View.VISIBLE) ;
////                    GetSubjctWisGrpDt() ;
////                } else  {
////                    bindingStudentOne.linSubject.setVisibility(View.GONE) ;
////                    changePosition(1);
////                }
//
//            }
//        });
//
//        bindingStudentOne.etClass.setOnClickListener(v->{
//            classSpinner.setHighlightSelectedItem(true);
//            classSpinner.show();
//        });
//
//    }
//
//
//
//    private void manageTwoClicks() {
//
//        setOccupationAdapter(allInOneDtModel.getData().get(0).getOcupationArray()) ;
//        if (editModel!=null){
//            bindingStudentTwo.etName.setText("" + editModel.fullName);
//            bindingStudentTwo.etDob.setText("" + editModel.dob);
//            if (editModel.gender.equalsIgnoreCase("MALE")){
//                bindingStudentTwo.rbMale.setChecked(true);
//            }else if (editModel.gender.equalsIgnoreCase("FEMALE")){
//                bindingStudentTwo.rbFemale.setChecked(true);
//            }else if (editModel.gender.equalsIgnoreCase("OTHER")){
//                bindingStudentTwo.rbOther.setChecked(true);
//            }
//
//            bindingStudentTwo.etFname.setText("" + editModel.fatherName);
//            bindingStudentTwo.etFccupation.setText("" + editModel.fatherOccupation);
//            bindingStudentTwo.etMname.setText("" + editModel.motherName);
//            bindingStudentTwo.etMccupation.setText("" + editModel.motherOccupation);
//            bindingStudentTwo.etPrivios.setText("" + editModel.previousSchool);
//            bindingStudentTwo.etAadhaar.setText("" + editModel.aadharNo);
//        }
//
//        bindingStudentTwo.etAadhaar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (bindingStudentTwo.etAadhaar.getText().toString().length() ==12){
//                    if (!Verhoeff.validateVerhoeff(bindingStudentTwo.etAadhaar.getText().toString())){
//                        bindingStudentTwo.etAadhaar.setError("Enter correct Aadhaar Number");
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
//        bindingStudentTwo.email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    changePosition(2);
//                }
//                return false;
//            }
//        });
//        bindingStudentTwo.etDob.setOnClickListener(v->{
//            datePickerWithType();
//        });
//
//    }
//    void changePosition(int pos){
//        if (binding.viewPager.getCurrentItem() != pos){
//            binding.viewPager.setCurrentItem(pos) ;
//            binding.tabLayout.selectTab(tabList.get(pos)) ;
//        }
//    }
//    private void checkValidation() {
//        List<SubjectModel> subjectModels = new ArrayList<>() ;
//        for (String key: subjectMap.keySet()) {
//            subjectModels.add(subjectMap.get(key)) ;
//        }
//        SELECT_SUBJECT = new Gson().toJson(subjectModels) ;
//        GENDER="" ;
//        if (bindingStudentTwo.rbMale.isChecked()){
//            GENDER ="MALE" ;
//        }
//        if (bindingStudentTwo.rbFemale.isChecked()){
//            GENDER ="FEMALE" ;
//        }
//        if (bindingStudentTwo.rbOther.isChecked()){
//            GENDER ="OTHER" ;
//        }
//
//
//        if (!Utility.isNetworkConnectedMainThred(getApplication())){
//            FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
//        }else if (bindingStudentOne.etGroup.getText().toString().equalsIgnoreCase("")){
//            bindingStudentOne.etGroup.setError("Invalid Class Group");
//            changePosition(0);
//        }else if (bindingStudentOne.etClass.getText().toString().equalsIgnoreCase("")){
//            bindingStudentOne.etClass.setError("Invalid Class");
//            changePosition(0);
//        }else if (bindingStudentOne.linSubject.getVisibility()==View.VISIBLE && subjectModels.size()==0 ){
//            FToast.makeText(getApplication(), "Please Select your Subjects", FToast.LENGTH_SHORT).show();
//            changePosition(0);
//        }else if (bindingStudentTwo.etName.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etName.setError("Invalid Onwer Name");
//            changePosition(1);
//        }else if (bindingStudentTwo.etDob.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etDob.setError("Invalid Date of Birth");
//            changePosition(1);
//        }else if (GENDER.equalsIgnoreCase("")){
//            FToast.makeText(getApplication(), "Please Select Gender", FToast.LENGTH_SHORT).show();
//            changePosition(1);
//        }else if (bindingStudentTwo.etFname.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etFname.setError("Invalid Father Name");
//            changePosition(1);
//        }else if (bindingStudentTwo.etFccupation.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etFccupation.setError("Invalid Father Occupation");
//            changePosition(1);
//        }else if (bindingStudentTwo.etMname.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etMname.setError("Invalid Mother Name");
//            changePosition(1);
//        }else if (bindingStudentTwo.etMccupation.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etMccupation.setError("Invalid Mother Occupation");
//            changePosition(1);
//        }else if (bindingStudentTwo.etPrivios.getText().toString().equalsIgnoreCase("")){
//            bindingStudentTwo.etPrivios.setError("Invalid Previous School");
//            changePosition(1);
//        }else if (bindingStudentTwo.etAadhaar.getText().toString().length() != 12){
//            bindingStudentTwo.etAadhaar.setError("Invalid Aadhaar Number");
//            changePosition(1);
//        }else if (!Verhoeff.validateVerhoeff(bindingStudentTwo.etAadhaar.getText().toString())){
//            bindingStudentTwo.etAadhaar.setError("Enter a valid aadhaar number");
//            changePosition(1);
//        }else if (!bindingStudentTwo.email.getText().toString().toLowerCase().contains("@gmail.com")){
//            bindingStudentTwo.email.setError("Invalid Email Address");
//            changePosition(1);
//        }else if (bindingStudentThree.etMobile.getText().toString().length() != 10){
//            bindingStudentThree.etMobile.setError("Invalid Mobile Number");
//            changePosition(2);
//        }else if (bindingStudentThree.etAltMobile.getText().toString().length() != 10){
//            bindingStudentThree.etAltMobile.setError("Invalid Alternate Mobile Number");
//            changePosition(2);
//        }else if (bindingStudentThree.etMobile.getText().toString().equalsIgnoreCase(bindingStudentThree.etAltMobile.getText().toString())){
//            bindingStudentThree.etAltMobile.setError("Enter Different Alternate Mobile Number");
//            changePosition(1);
//        }else if (bindingStudentThree.etPincode.getText().toString().length() != 6){
//            bindingStudentThree.etPincode.setError("Invalid Pincode");
//            changePosition(1);
//        }else if (bindingStudentThree.etTehsil.getText().toString().equalsIgnoreCase("")){
//            bindingStudentThree.etTehsil.setError("Invalid Tehsil");
//            changePosition(2);
//        }else if (bindingStudentThree.etVillageMohalla.getText().toString().equalsIgnoreCase("")){
//            bindingStudentThree.etVillageMohalla.setError("Invalid Village Mohalla");
//            changePosition(2);
//        }else if (bindingStudentThree.etRelegion.getText().toString().equalsIgnoreCase("")){
//            bindingStudentThree.etRelegion.setError("Invalid Relegion");
//            changePosition(2);
//        }else if (bindingStudentThree.etCaste.getText().toString().equalsIgnoreCase("")){
//            bindingStudentThree.etCaste.setError("Invalid Caste");
//            changePosition(2);
//        }else if (bindingStudentThree.etSubCaste.getText().toString().equalsIgnoreCase("")){
//            bindingStudentThree.etSubCaste.setError("Invalid Sub Caste");
//            changePosition(2);
//        }else if (!bindingStudentThree.checkBox.isChecked()) {
//            FToast.makeText(getApplicationContext(),"Apply Terms & Conditions",FToast.LENGTH_SHORT).show();
//            changePosition(2);
//        }else{
//            AdStaffReg() ;
//        }
//
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
//        return encodeString;
//    }
//
//
//    private void AdStaffReg() {
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
//        map.put("type" ,"AdStuReg") ;
//        if (editModel != null) {
//            map.put("type" ,"UpStuEdLs") ;
//        }
//        map.put("Subjects" ,"" + SELECT_SUBJECT) ;
//        map.put("CollageCode" ,""+getSchoolModel.getCollageCode() ) ;
//        map.put("CollageName" ,""+getSchoolModel.getCollageName()) ;
//        map.put("Class" , bindingStudentOne.etClass.getText().toString()) ;
//        map.put("ClassId" , selectedClass.getClassId()) ;
//
//        map.put("FullName" , bindingStudentTwo.etName.getText().toString().toUpperCase()) ;
//        map.put("DOB" , bindingStudentTwo.etDob.getText().toString().toUpperCase()) ;
//        map.put("Gender" ,GENDER.toUpperCase() ) ;
//        map.put("FatherName" , bindingStudentTwo.etFname.getText().toString().toUpperCase()) ;
//        map.put("FatherOccupation" , bindingStudentTwo.etFccupation.getText().toString().toLowerCase().replace(" ","-")) ;
//        map.put("MotherName" , bindingStudentTwo.etMname.getText().toString().toUpperCase()) ;
//        map.put("MotherOccupation" , bindingStudentTwo.etMccupation.getText().toString().toLowerCase().replace(" ","-")) ;
//        map.put("PreviousSchool" , bindingStudentTwo.etPrivios.getText().toString().toUpperCase()) ;
//        map.put("AadharNo" , bindingStudentTwo.etAadhaar.getText().toString()) ;
//        map.put("EmailId" , bindingStudentTwo.email.getText().toString().toUpperCase()) ;
//        map.put("MobileNumber" , bindingStudentThree.etMobile.getText().toString()) ;
//        map.put("AlternateNumber" , bindingStudentThree.etAltMobile.getText().toString()) ;
//        map.put("PinCode" , bindingStudentThree.etPincode.getText().toString()) ;
//        map.put("State" , bindingStudentThree.etState.getText().toString().toUpperCase()) ;
//        map.put("District" , bindingStudentThree.etDistrict.getText().toString().toUpperCase()) ;
//        map.put("Tahsil" , bindingStudentThree.etTehsil.getText().toString().toUpperCase()) ;
//        map.put("Villa_Mohalla" , bindingStudentThree.etVillageMohalla.getText().toString().toUpperCase()) ;
//        map.put("ImageExt" ,"data:image/" + ImageExt + ";") ;
//        map.put("RelegionData" , ""+bindingStudentThree.etRelegion.getText().toString().toUpperCase()) ;
//        map.put("CasteData" , "" + bindingStudentThree.etCaste.getText().toString().toUpperCase()) ;
//        map.put("SubCasteData" , ""+bindingStudentThree.etSubCaste.getText().toString().toUpperCase()) ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        FLog.w("AdStaffReg" , "map>>" + new Gson().toJson(map)) ;
//        map.put("Image" ,"" + Image) ;
//        String json = new Gson().toJson(map) ;
//        globalLoader.showLoader();
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                try {
//                    globalLoader.dismissLoader();
//
//                    CommonResponse commonResponse = Utility.convertResponse(res);
//                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
//                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , StudentRegisterActivity.this , "You has been successfully registered please wait for approvale" ,()->{
//                            if (commonDB.getString(Constants.LOGIN_RESPONSE).equalsIgnoreCase("")){
//                                Utility.userLogout(getApplicationContext());
//                            }else{
//                                Utility.gotoHome(getApplicationContext());
//                            }
//                        }) ;
//                    }else {
//                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentRegisterActivity.this , ""+commonResponse.getMsg() ,()->{
//                        }) ;
//                    }
//                }catch (Exception e){e.printStackTrace();}
//            }) ;
//        });
//    }
//    private void PincodeData() {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"PincodeData") ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        map.put("Pincode" , "" + bindingStudentThree.etPincode.getText().toString()) ;
//        String json = new Gson().toJson(map) ;
//        globalLoader.showLoader();
//        String url ="https://api.postalpincode.in/pincode/"+ bindingStudentThree.etPincode.getText().toString() ;
//        RetrofitClient.getRetrofitInstance().pincodeApi(url).enqueue(new Callback<List<PincodeModel>>() {
//            @Override
//            public void onResponse(Call<List<PincodeModel>> call, Response<List<PincodeModel>> response) {
//                try {
//                    globalLoader.dismissLoader();
//                    if (response.body().size() > 0){
//                        if (response.body().get(0).getStatus().equalsIgnoreCase("Success")){
//                            bindingStudentThree.etState.setText("" + response.body().get(0).getPostOffice().get(0).getState()) ;
//                            bindingStudentThree.etDistrict.setText("" + response.body().get(0).getPostOffice().get(0).getDistrict()) ;
//                            setTehsilAdapter(response.body().get(0).getPostOffice()) ;
//
//                        }else {
//                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentRegisterActivity.this , "No records found Please enter valid Pincode" ,()->{
//                            }) ;
//                        }
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<List<PincodeModel>> call, Throwable t) {
//                globalLoader.dismissLoader();
//            }
//        });
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
//
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
//                bindingStudentThree.etTehsil.setText(selectedString) ;
//            }
//        });
//        bindingStudentThree.etTehsil.setOnClickListener(v->{
//            tehsil.setHighlightSelectedItem(true);
//            tehsil.show();
//        });
//
//
//    }
//
//
//
//
//    private void checkAllPermission(String type) {
//        List<String> list = new ArrayList<>() ;
//        if (!PermissionUtil.checkPermisiion(getApplicationContext() , "android.permission.CAMERA")){
//            list.add(Manifest.permission.CAMERA) ;
//        }
//        if (!PermissionUtil.checkPermisiion(getApplicationContext() , Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU?
//                Manifest.permission.READ_MEDIA_IMAGES:
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                list.add(Manifest.permission.READ_MEDIA_IMAGES) ;
//            }else{
//                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
//            }
//        }
//        FLog.w("sdfdsf" ,">>>>>>>>>>" + new Gson().toJson(list)) ;
//        if (list.size()>0){
//            String[] permissions = new String[list.size()];
//            permissions = list.toArray(permissions);
//            PermissionUtil.requestPermission(StudentRegisterActivity.this ,permissions , REQUEST_PERMISSSION) ;
//        }else if (type.equalsIgnoreCase("OnClickListener")){
//            DialogModel dialogModel = new DialogModel(StudentRegisterActivity.this ,"Take Image","Take photo from Gallery or take new picture using Camera !","Camera","Gallery", t->{
//                if (t.equalsIgnoreCase("POSTIVE")){
//                    File image = createImageFile();
//                    imgUri = Uri.fromFile(image);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
//                    cameraIntent.launch(intent);
//                }else{
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    galleryIntent.launch(pickPhoto);
//                }
//            } ) ;
//            Utility.wantTOSureDialog(dialogModel) ;
//        }
//    }
//    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    CropImage.activity(imgUri)
//
//                                .start(StudentRegisterActivity.this);
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
//                                .start(StudentRegisterActivity.this);
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
//
//
////                try {
////                    bindingStudentOne.image.setPadding(5,5,5,5) ;
////                    uploadFile = FileUtils.getFileFromUri(getApplicationContext(), resultUri) ;
////                    Glide.with(getApplicationContext())
////                            .load(uploadFile)
////                            .apply(new RequestOptions().placeholder(R.drawable.plus))
////                            .into(bindingStudentOne.image)   ;
////                   // dummy() ;
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//    }
//
//    private void setReligionAdapter(List<ReligionModel> list) {
//
//
//        SearchableSpinner relegion = new SearchableSpinner(this);
//        relegion.setWindowTitle("Select Relegion") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , ReligionModel> classMap = new HashMap<>() ;
//        for (ReligionModel s : list){
//            strList.add(s.getReligionName()) ;
//            classMap.put(""+s.getReligionName() ,  s) ;
//        }
//        relegion.setSpinnerListItems(strList);
//        relegion.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStudentThree.etRelegion.setText(selectedString) ;
//            }
//        });
//        bindingStudentThree.etRelegion.setOnClickListener(v->{
//            relegion.setHighlightSelectedItem(true);
//            relegion.show();
//        });
//
//
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
//                bindingStudentThree.etCaste.setText(selectedString) ;
//                bindingStudentThree.etSubCaste.setText("") ;
//                SubCasteData(map.get(selectedString).getCasteId()) ;
//            }
//        });
//        bindingStudentThree.etCaste.setOnClickListener(v->{
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
////
////        RetrofitClient.getRetrofitInstance().SubCasteData(map).enqueue(new Callback<List<SubCasteModel>>() {
////            @Override
////            public void onResponse(Call<List<SubCasteModel>> call, Response<List<SubCasteModel>> response) {
////                try {
////                    globalLoader.dismissLoader();
////                    setSubCasteAdapter(response.body()) ;
////                }catch (Exception e){e.printStackTrace();}
////            }
////            @Override
////            public void onFailure(Call<List<SubCasteModel>> call, Throwable t) {
////                globalLoader.dismissLoader();
////            }
////        });
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
//                bindingStudentThree.etSubCaste.setText(selectedString) ;
//            }
//        });
//        bindingStudentThree.etSubCaste.setOnClickListener(v->{
//            caste.setHighlightSelectedItem(true);
//            caste.show();
//        });
//
//    }
//    private void OcupationData() {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        //globalLoader.showLoader();
//        RetrofitClient.getRetrofitInstance().OcupationData(map).enqueue(new Callback<List<OccupationModel>>() {
//            @Override
//            public void onResponse(Call<List<OccupationModel>> call, Response<List<OccupationModel>> response) {
//                try {
//                    //globalLoader.dismissLoader();
//
//                }catch (Exception e){e.printStackTrace();}
//            }
//
//            @Override
//            public void onFailure(Call<List<OccupationModel>> call, Throwable t) {
//                //globalLoader.dismissLoader();
//            }
//        });
//
//    }
//    private void setOccupationAdapter(List<OcupationArray> list) {
//
//
//        SearchableSpinner fatherOccSpi = new SearchableSpinner(this);
//        fatherOccSpi.setWindowTitle("Select Father Occupation") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , OcupationArray> classMap = new HashMap<>() ;
//        for (OcupationArray s : list){
//            strList.add(s.getOccupationName()) ;
//            classMap.put(""+s.getOccupationName() ,  s) ;
//        }
//        fatherOccSpi.setSpinnerListItems(strList);
//        fatherOccSpi.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStudentTwo.etFccupation.setText(selectedString) ;
//            }
//        });
//        bindingStudentTwo.etFccupation.setOnClickListener(v->{
//            fatherOccSpi.setHighlightSelectedItem(true);
//            fatherOccSpi.show();
//        });
//
//        SearchableSpinner motherOccSpi = new SearchableSpinner(this);
//        motherOccSpi.setWindowTitle("Select Mother Occupation") ;
//
//        motherOccSpi.setSpinnerListItems(strList);
//        motherOccSpi.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                bindingStudentTwo.etMccupation.setText(selectedString) ;
//            }
//        });
//        bindingStudentTwo.etMccupation.setOnClickListener(v->{
//            motherOccSpi.setHighlightSelectedItem(true);
//            motherOccSpi.show();
//        });
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
//
//                        int m = monthOfYear+ 1 ;
//                        bindingStudentTwo.etDob.setText(year + "-"+ m + "-" + dayOfMonth);
//                        //bindingStudentTwo.etDob.setText(dayOfMonth + "-"+ Utility.getMonthList().get(monthOfYear) + "-" + year);
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
//    void removeBG(Bitmap bitmap){
//        bindingStudentOne.progressBar.setVisibility(View.VISIBLE) ;
//        new ImageConvert().ImageConvertLocal(bitmap, new BgRemoveInterface() {
//            @Override
//            public void onSuccess(@NonNull Bitmap bitmap) {
//                try {
//                    bindingStudentOne.progressBar.setVisibility(View.GONE) ;
//                    FLog.w("removeBG" , "onSuccess");
//                    uploadFile = Utility.bitmapToFile(getApplicationContext() , bitmap ,"img_"+System.currentTimeMillis()) ;
//                    Glide.with(getApplicationContext())
//                            .load(bitmap)
//                            .apply(new RequestOptions().placeholder(R.drawable.placeholder_upload))
//                            .into(bindingStudentOne.image)   ;
//                }catch (Exception e){e.printStackTrace();}
//            }
//            @Override
//            public void onFailed(@NonNull Exception exception) {
//                FLog.w("removeBG" , "onFailed" + exception);
//                bindingStudentOne.progressBar.setVisibility(View.GONE) ;
//                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentRegisterActivity.this , ""+exception.getMessage() ,()->{
//                }) ;
//
//            }
//        });
//    }
//
//    private void ClassGrpAdded() {
//        try {
//            String res = commonDB.getString("ClassGrpAdded");
//            ArrayList<ClassGroupModel> list = (ArrayList<ClassGroupModel>) fromJson(res,
//                    new TypeToken<ArrayList<ClassGroupModel>>() {
//                    }.getType());
//            if (list.size()>0){
//                groupModelList.addAll(list) ;
//                setGroupAdapter();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
////        Map<String , String> map = new HashMap<>() ;
////        map.put("type" ,"ClassGrpAdded") ;
////        map.put("Unqid" ,schoolModel.getUnqid()) ;
////        globalLoader.showLoader();
////        String json = new Gson().toJson(map) ;
////        webSocketManager.sendMessage(json , res->{
////            runOnUiThread(()->{
////                globalLoader.dismissLoader();
////                try {
////                    ArrayList<ClassGroupModel> list = (ArrayList<ClassGroupModel>) fromJson(res,
////                            new TypeToken<ArrayList<ClassGroupModel>>() {
////                            }.getType());
////                    if (list.size()>0){
////                        groupModelList.addAll(list) ;
//////                        groupModel = groupModelList.get(0) ;
////                        setGroupAdapter();
////
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            });
////        }) ;
//
//
//    }
//
//    private void setGroupAdapter() {
//        groupSpinner = new SearchableSpinner(this);
//        groupSpinner.setWindowTitle("Select Class Group") ;
//        ArrayList<String> strList = new ArrayList<>() ;
//        Map<String , ClassGroupModel> groupMap = new HashMap<>() ;
//
//        for (ClassGroupModel s : groupModelList){
//            strList.add(s.getGroupName()) ;
//            groupMap.put(""+s.getGroupName() ,  s) ;
//        }
//        groupSpinner.setSpinnerListItems(strList);
//        groupSpinner.setOnItemSelectListener(new OnItemSelectListener() {
//            @Override
//            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
//                FLog.w("groupSpinner" , "setOnItemSelectListener" +position );
//                groupModel = groupMap.get(selectedString);
//                bindingStudentOne.etGroup.setText(groupModel.getGroupName()) ;
//                SELECT_SUBJECT="[]" ;
//                bindingStudentOne.linSubject.setVisibility(View.GONE) ;
//                GetClsWisGrpDt();
//            }
//        });
//        bindingStudentOne.etGroup.setOnClickListener(v->{
//            groupSpinner.setHighlightSelectedItem(true);
//            groupSpinner.show();
//        });
//
//    }
//    private void GetClsWisGrpDt() {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type","GetClsWisGrpDt") ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        map.put("GroupId" ,"" + groupModel.getGroupId());
//        String json = new Gson().toJson(map) ;
//        globalLoader.showLoader();
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                try {
//                    globalLoader.dismissLoader();
//                    List<ClassModel> list=  Utility.convertClassList(res) ;
//                    class_flag= false ;
//                    bindingStudentOne.etClass.setText("") ;
//                    setClassesAdapter(list) ;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentRegisterActivity.this , ""+e.getMessage() ,()->{
//                    }) ;
//                }
//            });
//        });
//    }
//    private void GetSubjctWisGrpDt() {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type","GetSubjctWisGrpDt") ;
//        map.put("Unqid" ,schoolModel.getUnqid()) ;
//        map.put("GroupId" ,"" + groupModel.getGroupId());
//        String json = new Gson().toJson(map) ;
//        globalLoader.showLoader();
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                try {
//                    globalLoader.dismissLoader();
//                    List<SubjectModel> list =  Utility.convertSubjectList(res) ;
//                    subjectMap.clear();
//                    bindingStudentOne.setSubjectSelectionAdapter(new SubjectSelectionAdapter(list , (pos,type)->{
//                        if (subjectMap.containsKey(list.get(pos).getSubjectName())){
//                            subjectMap.remove(list.get(pos).getSubjectName()) ;
//                        }else{
//                            subjectMap.put("" +list.get(pos).getSubjectName() , list.get(pos)) ;
//                        }
//
//                        FLog.w("subjectMap","dfgdfgdgfg>>>>>>>" + new Gson().toJson(subjectMap)) ;
//
//                    }));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentRegisterActivity.this , ""+e.getMessage() ,()->{
//                    }) ;
//                }
//            });
//        });
//    }
//
//}