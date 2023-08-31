package com.op.eschool.activities.staff.school;

import static com.op.eschool.util.Utility.createImageFile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddSchoolBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.FToast;
import com.op.eschool.util.FileUtils;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.PermissionUtil;
import com.op.eschool.util.Utility;
import com.op.eschool.util.Verhoeff;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSchoolActivity extends BaseActivity {
    ActivityAddSchoolBinding binding ;
    private static final int REQUEST_PERMISSSION = 99;
    private Uri imgUri;
    File uploadFile ;
    GlobalLoader globalLoader ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_add_school);
        globalLoader = new GlobalLoader(AddSchoolActivity.this) ;

        StrictMode.VmPolicy.Builder builders = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builders.build());


        checkAllPermission("onCreate") ;
        manageClicks() ;


    }

    private void manageClicks() {
        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.logoImage.setOnClickListener(v->{
          checkAllPermission("OnClickListener");
        });
        binding.btnNext.setOnClickListener(v->{
            if (binding.etCode.getText().toString().equalsIgnoreCase("")){
                binding.etCode.setError("Enter a valid code");
            }else if (binding.etName.getText().toString().equalsIgnoreCase("")){
                binding.etName.setError("Enter a valid name");
            }else if (binding.etOnwer.getText().toString().equalsIgnoreCase("")){
                binding.etOnwer.setError("Enter a valid onwer name");
            }else if (binding.etFname.getText().toString().equalsIgnoreCase("")){
                binding.etFname.setError("Enter a valid onwer father name");
            }else if (binding.etMobile.getText().toString().length() != 10){
                binding.etMobile.setError("Enter a valid mobile");
            }else if (binding.etAlternate.getText().toString().length() != 10){
                binding.etAlternate.setError("Enter a valid Alternate mobile");
            }else if (binding.etPincode.getText().toString().length() != 6){
                binding.etPincode.setError("Enter a valid Pincode");
            }else if (!Verhoeff.validateVerhoeff(binding.etAadhaar.getText().toString())){
                binding.etAadhaar.setError("Enter a valid aadhaar number");
            }else if (!binding.email.getText().toString().toLowerCase().contains("@gmail.com")){
                binding.email.setError("Enter a valid email");
            }else if (binding.etDob.getText().toString().equalsIgnoreCase("")){
                binding.etDob.setError("Enter a valid date of birth");
            }else if (binding.etQualification.getText().toString().equalsIgnoreCase("")){
                binding.etQualification.setError("Enter a valid Qualification");
            }else if (binding.etVillageMohalla.getText().toString().equalsIgnoreCase("")){
                binding.etVillageMohalla.setError("Enter a valid Village Mohalla");
            }else{
                CollageReg() ;
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
            PermissionUtil.requestPermission(AddSchoolActivity.this ,permissions , REQUEST_PERMISSSION) ;
        }else if (type.equalsIgnoreCase("OnClickListener")){

            DialogModel dialogModel = new DialogModel(AddSchoolActivity.this ,"Take Image","Take photo from Gallery or take new picture using Camera !","Camera","Gallery", t->{
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

                                .start(AddSchoolActivity.this);
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

                                .start(AddSchoolActivity.this);
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
                    binding.logoImage.setPadding(5,5,5,5) ;
                    uploadFile = FileUtils.getFileFromUri(getApplicationContext(), resultUri) ;
                    Glide.with(getApplicationContext())
                            .load(uploadFile)
                            .apply(new RequestOptions().placeholder(R.drawable.plus))
                            .into(binding.logoImage)   ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                FToast.makeText(getApplicationContext(),"Permission Denied",FToast.LENGTH_SHORT).show();
            }
        }
    }

    private void CollageReg() {

        String url = "CollageReg" ;
        Map<String , String> map = new HashMap<>() ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("CollageCode" ,binding.etCode.getText().toString()) ;
        map.put("CollageName" ,binding.etName.getText().toString()) ;
        map.put("OwnerName" ,binding.etOnwer.getText().toString()) ;
        map.put("DOB" ,binding.etDob.getText().toString()) ;
        map.put("FatherName" ,binding.etFname.getText().toString()) ;
        map.put("Qulification" ,binding.etQualification.getText().toString()) ;
        map.put("AadharCard" ,binding.etAadhaar.getText().toString()) ;
        map.put("EmailId" ,binding.email.getText().toString()) ;
        map.put("MobileNumber" ,binding.etMobile.getText().toString()) ;
        map.put("AlternateNumber" ,binding.etAlternate.getText().toString()) ;
        map.put("Pincode" ,binding.etPincode.getText().toString()) ;
        map.put("Village_Mohalla" ,binding.etVillageMohalla.getText().toString()) ;
        map.put("Image" ,"") ;
        globalLoader.showLoader();

    }
}