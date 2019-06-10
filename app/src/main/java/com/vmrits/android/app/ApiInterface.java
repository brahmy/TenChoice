package com.vmrits.android.app;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiInterface {

    @Multipart // annotation used in POST type requests
    @POST("kyc_details.php")     // API's endpoints
    public Call<ResponsePOJO> kycDetails(@Part("aadhaar_no") RequestBody aadhaar_no,
                                         @Part("pan_no") RequestBody pan_no,
                                         @Part("phone") RequestBody mobile_number1,
                                         @Part List<MultipartBody.Part> files);
}