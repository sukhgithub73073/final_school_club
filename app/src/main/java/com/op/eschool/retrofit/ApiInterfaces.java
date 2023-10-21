package com.op.eschool.retrofit;

import com.op.eschool.models.CasteModel;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.PincodeModel;
import com.op.eschool.models.ReligionModel;
import com.op.eschool.models.SubCasteModel;
import com.op.eschool.models.bankNames.BankNameModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.class_models.SectionModel;
import com.op.eschool.models.class_models.SessionModel;
import com.op.eschool.models.parents_model.OccupationModel;
import com.op.eschool.models.parents_model.ParentModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentModel;

import java.util.List;
import java.util.Map;

import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterfaces {

   @Headers("Content-Type: application/x-www-form-urlencoded")
   @GET()
   Call<List<com.op.eschool.models.pincode_api_model.PincodeModel>> pincodeApi (@Url String url);

   @Headers("Content-Type: application/json")
   @POST()
   Call<Object> commonApi(@Url String url , @Body Map<String, String> requestBody);


}