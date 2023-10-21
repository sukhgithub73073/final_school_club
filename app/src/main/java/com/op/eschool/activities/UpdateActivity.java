package com.op.eschool.activities;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityUpdateBinding;
import com.op.eschool.util.FLog;
public class UpdateActivity extends BaseActivity {
    ActivityUpdateBinding binding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_update) ;
        binding.update.setOnClickListener(v->{
            checkUpdate() ;
        });
    }
    void checkUpdate(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            try {
                FLog.w("checkForUpdates>>" , "updateAvailability>>>" +appUpdateInfo.updateAvailability() ) ;
                FLog.w("checkForUpdates>>" , "isUpdateTypeAllowed>>>" +appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE) ) ;
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    try {
                         appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE , UpdateActivity.this ,10 ) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }catch (Exception e){
                e.printStackTrace() ;

            }
        });
        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                try {
                    FLog.w("checkForUpdates>>" , "onFailure>>>" +e.getMessage()) ;
                    e.printStackTrace();

                }catch (Exception ev){
                    ev.printStackTrace();
                }
            }
        }) ;
    }

}