package com.op.eschool.activities.staff.register;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.SpiVillageAdapter;
import com.op.eschool.adapters.ViewPagerAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityCollageRegisterBinding;
import com.op.eschool.fragments.college.CollegeOneFragment;
import com.op.eschool.fragments.college.CollegeTwoFragment;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.pincode_api_model.PincodeModel;
import com.op.eschool.models.pincode_api_model.PostOffice;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.FileUtils;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.PermissionUtil;
import com.op.eschool.util.Utility;
import com.op.eschool.util.Verhoeff;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollageRegisterActivity extends BaseActivity {
    ActivityCollageRegisterBinding binding ;
    private static final int REQUEST_PERMISSSION = 99;
    File uploadFile ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_collage_register) ;
        globalLoader = new GlobalLoader(CollageRegisterActivity.this) ;

        TabLayout.Tab firstTab = binding.tabLayout.newTab();
        firstTab.setText("Information");
        binding.tabLayout.addTab(firstTab);
        checkAllPermission("onCreate");


        TabLayout.Tab wrong = binding.tabLayout.newTab();
        wrong.setText("Address");
        binding.tabLayout.addTab(wrong);
        List<Fragment> fragmentList = new ArrayList<>() ;
        fragmentList.add(new CollegeOneFragment(()->{
            manageOneClicks() ;
        })) ;
        fragmentList.add(new CollegeTwoFragment(()->{
            manageTwoClicks() ;
        })) ;
        binding.image.setOnClickListener(v->{
            checkAllPermission("OnClickListener");
        });
        binding.viewPager.setUserInputEnabled(false);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(CollageRegisterActivity.this , fragmentList);
        binding.viewPager.setAdapter(viewPagerAdapter) ;
       // manageClickes() ;
    }
    private void manageOneClicks() {
        bindingOne.verify.setOnClickListener(v->{
        if (!Utility.isNetworkConnectedMainThred(getApplicationContext())){
            FToast.makeText(getApplicationContext(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
        }else if (bindingOne.etCode.getText().toString().equalsIgnoreCase("")){
            FToast.makeText(getApplicationContext(), "Please enter valid School Code", FToast.LENGTH_SHORT).show();
        }else{
        }
    });}
    private void manageTwoClicks() {

        bindingTwo.etDesignation.setOnClickListener(v->{
            bindingTwo.spiDesignation.performClick() ;
        });
        bindingTwo.etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(bindingTwo.etPincode.length() == 6){
                    PincodeData() ;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bindingTwo.btnNext.setOnClickListener(v->{
            if (uploadFile==null){
                Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , CollageRegisterActivity.this , "Upload School Logo" ,()->{
                }) ;
            }else{
                checkValidation();
            }

        });
    }
    void changePosition(int pos){
        if (binding.viewPager.getCurrentItem() != pos){
            binding.viewPager.setCurrentItem(pos) ;
        }

    }
    private void checkValidation() {
        if (!Utility.isNetworkConnectedMainThred(getApplication())){
            FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
        }else if (bindingOne.etCode.getText().toString().equalsIgnoreCase("")){
            bindingOne.etCode.setError("Invalid School ID");
            changePosition(0);
        }else if (bindingOne.schoolName.getText().toString().equalsIgnoreCase("")){
            bindingOne.schoolName.setError("Invalid School Name");
            changePosition(0);
        }else if (bindingOne.onwer.getText().toString().equalsIgnoreCase("")){
            bindingOne.onwer.setError("Invalid Onwer Name");
            changePosition(0);
        }else if (bindingOne.dob.getText().toString().equalsIgnoreCase("")){
            bindingOne.dob.setError("Invalid Date of Birth");
            changePosition(0);
        }else if (bindingOne.fname.getText().toString().equalsIgnoreCase("")){
            bindingOne.fname.setError("Invalid Father Name");
            changePosition(0);
        }else if (bindingOne.qualification.getText().toString().equalsIgnoreCase("")){
            bindingOne.qualification.setError("Invalid Qualification");
            changePosition(0);
        }else if (bindingOne.aadhaar.getText().toString().length() != 12){
            bindingOne.aadhaar.setError("Invalid Aadhaar Number");
            changePosition(0);
        }else if (!Verhoeff.validateVerhoeff(bindingOne.aadhaar.getText().toString())){
            bindingOne.aadhaar.setError("Enter a valid aadhaar number");
            changePosition(0);
        }else if (!bindingOne.email.getText().toString().toLowerCase().contains("@gmail.com")){
            bindingOne.email.setError("Invalid Email Address");
            changePosition(0);
        }else if (bindingTwo.etMobile.getText().toString().length() != 10){
            bindingTwo.etMobile.setError("Invalid Mobile Number");
            changePosition(1);
        }else if (bindingTwo.etAltMobile.getText().toString().length() != 10){
            bindingTwo.etAltMobile.setError("Invalid Alternate Mobile Number");
            changePosition(1);
        }else if (bindingTwo.etPincode.getText().toString().length() != 6){
            bindingTwo.etPincode.setError("Invalid Pincode");
            changePosition(1);
        } else if (!bindingTwo.checkBox.isChecked()) {
            FToast.makeText(getApplicationContext(),"Apply Terms & Conditions",FToast.LENGTH_SHORT).show();
            changePosition(1);
        }else{
            CollageReg() ;
        }

    }
    public  String getBase64FromFile(String path) {
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
    private void CollageReg() {
        String ImageExt = MimeTypeMap.getFileExtensionFromUrl(uploadFile.toString());
        String baseImage = getBase64FromFile(uploadFile.getPath()) ;

        Map<String , String> map = new HashMap<>() ;
        map.put("CollageCode" ,""+bindingOne.etCode.getText().toString()) ;
        map.put("CollageName" ,""+bindingOne.schoolName.getText().toString()) ;
        map.put("OwnerName" , bindingOne.onwer.getText().toString()) ;
        map.put("DOB" , bindingOne.dob.getText().toString()) ;
        map.put("FatherName" , bindingOne.fname.getText().toString()) ;
        map.put("Qulification" , bindingOne.qualification.getText().toString()) ;
        map.put("AadharCard" , bindingOne.aadhaar.getText().toString()) ;
        map.put("EmailId" , bindingOne.email.getText().toString()) ;
        map.put("MobileNumber" , bindingTwo.etMobile.getText().toString()) ;
        map.put("AlternateNumber" , bindingTwo.etAltMobile.getText().toString()) ;
        map.put("Pincode" , bindingTwo.etPincode.getText().toString()) ;
        map.put("State" , bindingTwo.etState.getText().toString()) ;
        map.put("District" , bindingTwo.etDistrict.getText().toString()) ;
        map.put("Thesil" , bindingTwo.etTehsil.getText().toString()) ;
        map.put("Village_Mohalla" , bindingTwo.etVillageMohalla.getText().toString()) ;
        map.put("ImageExt" ,"data:image/" + ImageExt + ";") ;
        map.put("Designation" ,bindingTwo.spiDesignation.getSelectedItem().toString()) ;
        FLog.w("CollageReg" , "map>>" + new Gson().toJson(map)) ;

        map.put("Image" ,"" + baseImage) ;
        globalLoader.showLoader();
        String url ="CollageReg" ;
    }

    private void PincodeData() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"PincodeData") ;
        map.put("Unqid" , "") ;
        map.put("Pincode" , "" + bindingTwo.etPincode.getText().toString()) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        String url ="https://api.postalpincode.in/pincode/"+ bindingTwo.etPincode.getText().toString() ;
        RetrofitClient.getRetrofitInstance().pincodeApi(url).enqueue(new Callback<List<PincodeModel>>() {
            @Override
            public void onResponse(Call<List<PincodeModel>> call, Response<List<PincodeModel>> response) {
                try {
                    globalLoader.dismissLoader();
                    if (response.body().size() > 0){
                        if (response.body().get(0).getStatus().equalsIgnoreCase("Success")){
                            bindingTwo.etState.setText("" + response.body().get(0).getPostOffice().get(0).getState()) ;
                            bindingTwo.etDistrict.setText("" + response.body().get(0).getPostOffice().get(0).getDistrict()) ;
                            setTehsilAdapter(response.body().get(0).getPostOffice()) ;
                            setVillageAdapter(response.body().get(0).getPostOffice()) ;
                        }else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , CollageRegisterActivity.this , "No records found Please enter valid Pincode" ,()->{

                            }) ;
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<PincodeModel>> call, Throwable t) {
                globalLoader.dismissLoader();
            }
        });

    }
    List<PostOffice> filterTehsilList(List<PostOffice> list){
        List<PostOffice> rtnList = new ArrayList<>() ;
        List<String> tehsilList = new ArrayList<>() ;
        for (PostOffice postOffice : list){
            if (!tehsilList.contains(postOffice.getBlock())){
                tehsilList.add(postOffice.getBlock()) ;
            }
        }
        for (String s :tehsilList ){
            PostOffice p =  new PostOffice() ;
            p.setBlock(s) ;
            rtnList.add(p);
        }
        return rtnList ;
    }
    private void setTehsilAdapter(List<PostOffice> postOffices) {
        List<PostOffice> list = filterTehsilList(postOffices) ;

        SpiVillageAdapter classAdapter = new SpiVillageAdapter("TEHSIL" , CollageRegisterActivity.this  ,R.layout.operater_spinner_adapter_item  , list) ;
        bindingTwo.spiTehsil.setAdapter(classAdapter);

        bindingTwo.etTehsil.setOnClickListener(v->{
            bindingTwo.spiTehsil.performClick();
        });
        bindingTwo.spiTehsil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PostOffice postOffice = list.get(position)  ;
                bindingTwo.etTehsil.setText(postOffice.getBlock()) ;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setVillageAdapter(List<PostOffice> list) {
        SpiVillageAdapter classAdapter = new SpiVillageAdapter("VILLAGE" , CollageRegisterActivity.this ,R.layout.operater_spinner_adapter_item  , list) ;
        bindingTwo.spiVillage.setAdapter(classAdapter);

        bindingTwo.etVillageMohalla.setOnClickListener(v->{
            bindingTwo.spiVillage.performClick();
        });
        bindingTwo.spiVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindingTwo.etVillageMohalla.setText(list.get(position).getName());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
            PermissionUtil.requestPermission(CollageRegisterActivity.this ,permissions , REQUEST_PERMISSSION) ;
        }else if (type.equalsIgnoreCase("OnClickListener")){

            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.launch(pickPhoto);

        }

    }


    ActivityResultLauncher<Intent> galleryIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Intent data = result.getData() ;
                        Uri selectedImage = data.getData();
                        CropImage.activity(selectedImage)

                                .start(CollageRegisterActivity.this);
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
                    binding.image.setPadding(5,5,5,5) ;
                    uploadFile = FileUtils.getFileFromUri(getApplicationContext(), resultUri) ;
                    Glide.with(getApplicationContext())
                            .load(uploadFile)
                            .apply(new RequestOptions().placeholder(R.drawable.plus))
                            .into(binding.image)   ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}