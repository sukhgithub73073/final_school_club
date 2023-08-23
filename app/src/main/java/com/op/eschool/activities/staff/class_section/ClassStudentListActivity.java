package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.staff.student.SignUpStudentActivity;
import com.op.eschool.activities.staff.student.StudentProfileActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
import com.op.eschool.adapters.StudentAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassStudentListBinding;
import com.op.eschool.databinding.ActivityStudentListBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CasteModel;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentFilterModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassStudentListActivity extends BaseActivity {
    List<StudentModel> list =new ArrayList<>() ;
    ArrayList<StudentModel> allList =new ArrayList<>() ;
    ActivityClassStudentListBinding binding ;
    String DB_STUDENT_KEY="" ;

    SchoolModel schoolModel ;
    ArrayList<ClassGroupModel> groupList = new ArrayList<>() ;
    ClassGroupModel groupModel ;
    List<ClassModel> classList = new ArrayList<>() ;
    List<CasteModel> casteList = new ArrayList<>() ;
    GlobalLoader globalLoader ;
    StudentFilterModel studentFilterModel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_class_student_list) ;
        globalLoader= new GlobalLoader(ClassStudentListActivity.this) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        DB_STUDENT_KEY = "DB_STUDENT_KEY_" + commonDB.getString("Unqid") ;
        schoolModel = new Gson().fromJson(commonDB.getString("SCHOOL_DETAILS") , SchoolModel.class) ;
        StdntTblSocket("onCreate") ;
        studentFilterModel = new StudentFilterModel("","","","","") ;

        manageClicks() ;

        binding.filter.setOnClickListener(v->{
            filterDialog() ;
        });
        binding.addStudent.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , SignUpStudentActivity.class).putExtra("TYPE" ,"ADD_STUDENT")) ;
        });
    }
    private void manageClicks() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list = allList.stream()
                        .filter(studentModel -> charSequence.toString().equalsIgnoreCase("") || studentModel.fullName.contains(charSequence.toString().toUpperCase()))
                        .collect(Collectors.toList());
                setStudentADapter() ;
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Utility.setGroupAdapter(ClassStudentListActivity.this , getApplicationContext() , binding.txtGroup , model->{
            studentFilterModel.group = binding.txtGroup.getText().toString();
            binding.txtClass.setText("") ;
            Utility.setClassesAdapter(ClassStudentListActivity.this ,model.getClassList() , getApplicationContext() , binding.txtClass ,classModel->{
                studentFilterModel.className = binding.txtClass.getText().toString();
                filterApply();
            });
        });


        binding.txtGender.setOnClickListener(v->{
            genderSelection(binding.txtGender) ;
        });
        binding.txtCategory.setOnClickListener(v->{
            categorySelection(binding.txtCategory) ;
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void StdntTblSocket(String type) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"StdntTbl") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        String json = new Gson().toJson(map) ;
        try {
            globalLoader.showLoader();
        }catch (Exception e){
            e.printStackTrace();
        }
        webSocketManager.sendMessage(json , res->{
            try {
                runOnUiThread(()->{
                    ClassGrpAdded() ;
                    globalLoader.dismissLoader();
                    list = (ArrayList<StudentModel>) fromJson(res,
                            new TypeToken<ArrayList<StudentModel>>() {
                            }.getType());
                    allList.addAll(list) ;
                   // setStudentADapter() ;
                });
            } catch (Exception e) {
                e.printStackTrace() ;
            }

        });
    }

    private void setStudentADapter() {
        if (list.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvStudents.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;
        }
        binding.setStudentAdapter(new StudentAdapter(getApplicationContext() ,list , (p, type)->{
            if (type.equalsIgnoreCase("CALL")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).mobileNumber, null));
                startActivity(intent);
            } else if (type.equalsIgnoreCase("APPROVE")) {
                DialogModel dialogModel = new DialogModel(ClassStudentListActivity.this ,"Approve","Want to sure to Approve this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p ,list.get(p) , Constants.USER_APPROVE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(ClassStudentListActivity.this ,"Delete","Want to sure to Delete this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p,list.get(p) , Constants.USER_DELETE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("VIEW")) {
                commonDB.putString("STUDENT_DETAIL" , new Gson().toJson(list.get(p))) ;
                startActivity(new Intent(getApplicationContext() , StudentProfileActivity.class)) ;
            }
        })) ;
    }
    private void filterDialog() {
        Dialog dialog = Utility.openAnyDialog(R.layout.student_filter , ClassStudentListActivity.this) ;
        dialog.show() ;
        TextView txt_gender = dialog.findViewById(R.id.txt_gender) ;
        TextView txt_category = dialog.findViewById(R.id.txt_category) ;
        TextView class_grp = dialog.findViewById(R.id.class_grp) ;
        TextView txt_class = dialog.findViewById(R.id.txt_class) ;
        TextView txt_status = dialog.findViewById(R.id.txt_status) ;

        class_grp.setText("" + studentFilterModel.group) ;
        txt_class.setText("" + studentFilterModel.className) ;
        txt_gender.setText("" + studentFilterModel.gender) ;
        txt_category.setText("" + studentFilterModel.caste) ;
        txt_status.setText("" + studentFilterModel.status) ;
        dialog.findViewById(R.id.gender_category).setVisibility(View.VISIBLE) ;

        class_grp.setOnClickListener(v->{
            classGroupSelection(class_grp , pos -> {
                txt_class.setText("") ;
                GetClsWisGrpDt();
            }) ;
        });
        txt_class.setOnClickListener(v->{
            classSelection(txt_class , (pos , classsName)->{
            }) ;
        });
        txt_status.setOnClickListener(v->{
            statusSelection(txt_status) ;
        });
        txt_gender.setOnClickListener(v->{
            genderSelection(txt_gender) ;
        });
        txt_category.setOnClickListener(v->{
            categorySelection(txt_category) ;
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btn_apply).setOnClickListener(v->{
            dialog.dismiss();
            if(class_grp.getText().toString().equalsIgnoreCase("")){
                FToast.makeText(getApplication(), "Please Select a class", FToast.LENGTH_SHORT).show();
            }else{
                studentFilterModel = new StudentFilterModel("" + class_grp.getText().toString() ,
                        ""+txt_class.getText().toString(),""+txt_gender.getText().toString(),
                        ""+txt_category.getText().toString() ,""+txt_status.getText().toString()) ;
                filterApply() ;
            }
        });
    }

    public void filterApply(){        list.clear();
        String sts = studentFilterModel.status ;
        if (sts.equalsIgnoreCase("ALL")){
            sts = "" ;

        }
        String finalSts = sts;
        list = allList.stream()
                .filter(studentModel -> studentModel._class.equalsIgnoreCase(studentFilterModel.className))
                .filter(studentModel -> studentFilterModel.gender.equalsIgnoreCase("") || studentModel.gender.equalsIgnoreCase(studentFilterModel.gender))
                .filter(studentModel -> finalSts.equalsIgnoreCase("") || studentModel.actionStatus.equalsIgnoreCase(finalSts))
                .filter(studentModel -> studentFilterModel.caste.equalsIgnoreCase("") || studentModel.casteData.equalsIgnoreCase(studentFilterModel.caste))
                .collect(Collectors.toList());
        FLog.w("Dfg" ,"DFg>>>" + new Gson().toJson(list.size())) ;

        setStudentADapter();

    }
    private void UpStuTY(int pos ,StudentModel model, String UpDtType) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "UpStuTY") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("StudentId" , ""+model.studentId) ;
        map.put("UpDtType" , ""+UpDtType) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            globalLoader.dismissLoader();
            runOnUiThread(()->{
                try {

                    CommonResponse commonResponse =Utility.convertResponse(res) ;
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        String msg = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"Approved":"Deleted" ;
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , ClassStudentListActivity.this , model.fullName + " has been successfully " + msg  ,()->{
                            model.actionStatus =UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"APPROVED":"DELETE" ;
                            if (UpDtType.equalsIgnoreCase(Constants.USER_DELETE)){
                                list.remove(pos);
                            }
                            binding.getStudentAdapter().notifyDataSetChanged() ;
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , ClassStudentListActivity.this , ""+commonResponse.Msg ,()->{
                        }) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , ClassStudentListActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }
    private void classGroupSelection(TextView class_grp , CommonInterface commonInterface) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Class Group") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassGroupModel> classMap = new HashMap<>() ;
        for (ClassGroupModel s : groupList){
            strList.add(s.getGroupName()) ;
            classMap.put(""+s.getGroupName() ,  s) ;
        }
        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                class_grp.setText(selectedString) ;
                groupModel = classMap.get(selectedString) ;
                commonInterface.onItemClicked(0) ;

            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }



    private void classSelection(TextView txt_class , CommoTypenInterface commonInterface) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Class") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassModel> classMap = new HashMap<>() ;
        for (ClassModel s : classList){
            strList.add(s.getClassName()) ;
            classMap.put(""+s.getClassName() ,  s) ;
        }
        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                txt_class.setText(selectedString) ;
                commonInterface.onItemClicked(0 ,""+ classMap.get(selectedString).getClassId() ) ;

            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }


    private void categorySelection(TextView txtView) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Category") ;
        ArrayList<String> strList = new ArrayList<>() ;
        strList.add("GENRAL") ;
        strList.add("OBC") ;
        strList.add("SC") ;
        strList.add("ST") ;


        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                txtView.setText(selectedString) ;
                studentFilterModel.caste = selectedString;
                filterApply();
            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }

    private void genderSelection(TextView txtView) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Gender") ;
        ArrayList<String> strList = new ArrayList<>() ;
        strList.add("MALE") ;
        strList.add("FEMALE") ;
        strList.add("OTHER") ;

        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                txtView.setText(selectedString) ;
                studentFilterModel.gender = selectedString;
                filterApply();
            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }
    private void statusSelection(TextView txtView) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Status") ;
        ArrayList<String> strList = new ArrayList<>() ;
        strList.add("All") ;
        strList.add("Verified") ;
        strList.add("Non Verified") ;

        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                txtView.setText(selectedString) ;
            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }

    private void ClassGrpAdded() {

        try {
            String res = commonDB.getString("ClassGrpAdded");
            groupList = (ArrayList<ClassGroupModel>) fromJson(res,
                    new TypeToken<ArrayList<ClassGroupModel>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"ClassGrpAdded") ;
//        map.put("Unqid" , commonDB.getString("Unqid")) ;
//        //  globalLoader.showLoader();
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                // globalLoader.dismissLoader();
//                try {
//                    groupList = (ArrayList<ClassGroupModel>) fromJson(res,
//                            new TypeToken<ArrayList<ClassGroupModel>>() {
//                            }.getType());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }) ;

    }
    private void GetClsWisGrpDt() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type","GetClsWisGrpDt") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        //globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    //globalLoader.dismissLoader();
                    classList.clear();
                    classList =  Utility.convertClassList(res) ;

                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , ClassStudentListActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }


}