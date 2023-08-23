package com.op.eschool.models;

import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.class_models.SectionModel;
import com.op.eschool.models.parents_model.OccupationModel;
import com.op.eschool.models.pincode_api_model.PostOffice;
import com.op.eschool.models.school_models.GetSchoolModel;

public class RegisterModel {
   public String roll_number , sr_number , name ,dob,gender ,fname ,mname ,privios_school ,aadhaar,email ;
   public GetSchoolModel getSchoolModel ;
   public ClassModel classModel ;
   public SectionModel sectionModel ;
   public OccupationModel f_occupationModel ;
   public OccupationModel m_occupationModel ;
   public ReligionModel religionModel ;
   public CasteModel casteModel;
   public SubCasteModel subCasteModel ;
   public PostOffice postOffice ;


}
