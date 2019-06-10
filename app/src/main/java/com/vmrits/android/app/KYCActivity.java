package com.vmrits.android.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class KYCActivity extends AppCompatActivity {
    private Button button_submit;
    private RadioGroup radioGroup;
    private EditText editText_aadhaar_number, editText_pan_number;
    private ImageView imageView_aadhaar_front, imageView_aadhaar_back, imageView_pan, imageView_camera;
    private LinearLayout linearLayout_front, linearLayout_back, linearLayout_pan, linearLayout_capture;
    private Context context = KYCActivity.this;
    private Dialog dialog;
    private Uri uri_aadhaar_front, uri_aadhaar_back, uri_pan, uri_camera, uri_gallery_aadhaar_front, uri_gallery_aadhaar_back, uri_gallery_pan, uri_gallery_camera;
    private String string_aadhaar_number, string_pan_nunmber, imageFilePath, string_aadhaar_front, string_aadhaar_back, string_pan, string_camera, string_gallery_aadhaar_front, string_gallery_aadhaar_back, string_gallery_pan, string_gallery_camera, intent_mobile_number, path_external;
    private File file_aadhaar_front, file_aadhaar_back, file_pan, file_camera, file_gallery_adhhar_front, file_gallery_adhhar_back, file_gallery_pan, file_gallery_camera;
    private Bitmap bitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
//    private String path_external = Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg";

    private final static int AADHAAR_FRONT_CODE = 101;
    private final static int AADHAAR_BACK_CODE = 102;
    private final static int PAN_CODE = 103;
    private final static int IMAGE_CAPTURE_CODE = 104;

    private final static int GALLERY_AADHAAR_FRONT_CODE = 105;
    private final static int GALLERY_AADHAAR_BACK_CODE = 106;
    private final static int GALLERY_PAN_CODE = 107;
    private final static int GALLERY_IMAGE_CODE = 108;

    private final static int MY_CAMERA_PERMISSION_CODE = 109;
    private DialogProgressBar dialogProgressBar;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kyc);
        dialogProgressBar = new DialogProgressBar(context);
        dialogProgressBar.dialogInit();

        Intent intent = getIntent();
        intent_mobile_number = intent.getStringExtra("mobile_number");

        initializeViews();
    }

    private void initializeViews() {
        relativeLayout = findViewById(R.id.id_kyc_layout);
        button_submit = findViewById(R.id.id_kyc_submit_btn);
        linearLayout_front = findViewById(R.id.id_kyc_aadhaar_front_LL);
        linearLayout_back = findViewById(R.id.id_kyc_aadhaar_back_LL);
        linearLayout_pan = findViewById(R.id.id_kyc_aadhaar_image_pan_LL);
        linearLayout_capture = findViewById(R.id.id_kyc_aadhaar_image_capture_LL);
        editText_aadhaar_number = findViewById(R.id.id_kyc_aadhaar_number_edt);
        editText_pan_number = findViewById(R.id.id_kyc_pan_number_edt);

        imageView_aadhaar_front = findViewById(R.id.id_kyc_aadhaar_front_imageView);
        imageView_aadhaar_back = findViewById(R.id.id_kyc_aadhaar_back_imageView);
        imageView_pan = findViewById(R.id.id_kyc_pan_imageView);
        imageView_camera = findViewById(R.id.id_kyc_camera_imageView);

        onClickViews();
    }

    private void onClickViews() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProgressBar.showDialog();
                string_aadhaar_number = editText_aadhaar_number.getText().toString();
                string_pan_nunmber = editText_pan_number.getText().toString();
                if (!TextUtils.isEmpty(string_aadhaar_number) && !TextUtils.isEmpty(string_pan_nunmber)) {

                    retrofitKYCRequest();

//                request();

                    dialogProgressBar.showDialog();
//                    volleyKYCRequest();
/*
                Intent intent=new Intent(context,ReferenceActivity.class);
                startActivity(intent);
*/
                } else {
                    dialogProgressBar.hideDialog();
                    Toast.makeText(context, "Please Provide All Details", Toast.LENGTH_LONG).show();
                }
            }
        });
        linearLayout_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLoadDialog();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.id_custom_dialog_image_load_camera_RB:
                                checkCameraPermissions(AADHAAR_FRONT_CODE);
                                dialog.dismiss();
                                break;
                            case R.id.id_custom_dialog_image_load_gallery_RB:
                                checkCameraPermissions(GALLERY_AADHAAR_FRONT_CODE);
                                dialog.dismiss();
                                break;
                        }
                    }
                });

            }
        });
        linearLayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLoadDialog();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.id_custom_dialog_image_load_camera_RB:
                                checkCameraPermissions(AADHAAR_BACK_CODE);
                                dialog.dismiss();
                                break;
                            case R.id.id_custom_dialog_image_load_gallery_RB:
                                checkCameraPermissions(GALLERY_AADHAAR_BACK_CODE);
                                dialog.dismiss();
                                break;
                        }
                    }
                });

            }
        });
        linearLayout_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLoadDialog();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.id_custom_dialog_image_load_camera_RB:
                                checkCameraPermissions(PAN_CODE);
                                dialog.dismiss();
                                break;
                            case R.id.id_custom_dialog_image_load_gallery_RB:
                                checkCameraPermissions(GALLERY_PAN_CODE);
                                dialog.dismiss();
                                break;
                        }
                    }
                });

            }
        });

        linearLayout_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLoadDialog();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.id_custom_dialog_image_load_camera_RB:
                                checkCameraPermissions(IMAGE_CAPTURE_CODE);
                                dialog.dismiss();
                                break;
                            case R.id.id_custom_dialog_image_load_gallery_RB:
                                checkCameraPermissions(GALLERY_IMAGE_CODE);
                                dialog.dismiss();
                                break;
                        }
                    }
                });

            }
        });
    }

    private File convertBitmapToFile(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            FileOutputStream fo = new FileOutputStream(image);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void volleyKYCRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.KYC_DETAILS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("KYC_details" + response);
                dialogProgressBar.hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String response_message = jsonObject.getString("message");
                    if (response_message.equalsIgnoreCase("Successfully Submitted")) {
                        Toast.makeText(context, "Successful! KYC details Submitted!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, BankDetailsActivity.class);
                        intent.putExtra("mobile_number", intent_mobile_number);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
//                        volleyCheckDetailsRequest();

                    } else {
                        Toast.makeText(context, "Failed! KYC details not Submitted!!", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                System.out.println("KYC_error" + error.getLocalizedMessage());
                Toast.makeText(context, "Sorry!Server Error!!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("aadhaar_no", string_aadhaar_number);
                params.put("pan_no", string_pan_nunmber);
                params.put("phone", intent_mobile_number);
                System.out.println("phoneNu:" + intent_mobile_number);

                if (string_aadhaar_front != null) {
                    System.out.println("called01" + string_aadhaar_front);
                    params.put("aadhaar_1", string_aadhaar_front);
                } else {
                    System.out.println("called02");
                    params.put("aadhaar_1", string_gallery_aadhaar_front);
                }

                if (string_aadhaar_back != null) {
                    System.out.println("called11");
                    params.put("aadhaar_2", string_aadhaar_back);
                } else {
                    System.out.println("called12");
                    params.put("aadhaar_2", string_gallery_aadhaar_back);

                }
                if (string_pan != null) {
                    System.out.println("called21");
                    params.put("pan_image", string_pan);
                } else {
                    System.out.println("called22");
                    params.put("pan_image", string_gallery_pan);

                }
                if (string_camera != null) {
                    System.out.println("called31");
                    params.put("selfie", string_camera);
                } else {
                    System.out.println("called32");
                    params.put("selfie", string_gallery_camera);
                }
                System.out.println("params" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "Kyc_request");
    }

    private void loadResponseFragment(Fragment fragment) {
        relativeLayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("mobile_number", intent_mobile_number);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_kyc_container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    private void volleyCheckDetailsRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.CHECK_DETAILS_SUBMITTED + "?phone=" + intent_mobile_number, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialogProgressBar.hideDialog();

                System.out.println("check_details_response:" + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String string_kyc = jsonObject.getString("kyc");
                        String string_reference = jsonObject.getString("reference");
                        String string_account = jsonObject.getString("account");

                        if (!string_kyc.contains("KYC Details Submitted")) {
                            Intent intent = new Intent(context, KYCActivity.class);
                            intent.putExtra("mobile_number", intent_mobile_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (!string_account.contains("Account Details Submitted")) {
                            Intent intent = new Intent(context, BankDetailsActivity.class);
                            intent.putExtra("mobile_number", intent_mobile_number);
                            startActivity(intent);
                        } else if (!string_reference.contains("Reference Details Submitted")) {
                            Intent intent = new Intent(context, ReferenceActivity.class);
                            intent.putExtra("mobile_number", intent_mobile_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Fragment fragment = new SuccessFulFragment();
                            loadResponseFragment(fragment);

/*
                            Intent intent=new Intent(context,KYCActivity.class);
                            intent.putExtra("mobile_number",intent_mobile_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
*/
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                System.out.println("check_details_error" + error.getLocalizedMessage());
                Toast.makeText(context, "Sorry!Server Error!", Toast.LENGTH_LONG).show();

            }
        })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("phone",intent_mobile_number);
                return params;
            }
        }*/;
        AppController.getInstance().addToRequestQueue(stringRequest, "check_Details_Submitted");
    }


    private void retrofitKYCRequest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URLUtility.URL).build();
        ApiInterface apiInterface = APIClient.getRetrofitInstance().create(ApiInterface.class);
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

// add the params part within the multipart request
        RequestBody param1 = RequestBody.create(MediaType.parse("multipart/form-data"), string_aadhaar_number);
        RequestBody param2 = RequestBody.create(MediaType.parse("multipart/form-data"), string_pan_nunmber);
        RequestBody param3 = RequestBody.create(MediaType.parse("multipart/form-data"), intent_mobile_number);

// create list of file parts
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (file_aadhaar_front != null) {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_aadhaar_front);
            parts.add(MultipartBody.Part.createFormData("aadhaar_1", file_aadhaar_front.getName(), requestImageFile));

        } else {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_gallery_adhhar_front);
            parts.add(MultipartBody.Part.createFormData("aadhaar_1", file_gallery_adhhar_front.getName(), requestImageFile));

        }
        if (file_aadhaar_back != null) {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_aadhaar_back);
            parts.add(MultipartBody.Part.createFormData("aadhaar_2", file_aadhaar_back.getName(), requestImageFile));

        } else {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_gallery_adhhar_back);
            parts.add(MultipartBody.Part.createFormData("aadhaar_2", file_gallery_adhhar_back.getName(), requestImageFile));

        }

        if (file_pan != null) {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_pan);
            parts.add(MultipartBody.Part.createFormData("pan_image", file_pan.getName(), requestImageFile));

        } else {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_gallery_pan);
            parts.add(MultipartBody.Part.createFormData("pan_image", file_gallery_pan.getName(), requestImageFile));

        }
        if (file_camera != null) {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_camera);
            parts.add(MultipartBody.Part.createFormData("selfie", file_camera.getName(), requestImageFile));

        } else {
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file_gallery_camera);
            parts.add(MultipartBody.Part.createFormData("selfie", file_gallery_camera.getName(), requestImageFile));

        }

/*
        if (uri_aadhaar_front != null) {
            File imageFile = new File(uri_aadhaar_front.getPath());
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            parts.add(MultipartBody.Part.createFormData("aadhaar_1", imageFile.getName(), requestImageFile));
        }

        if (uri_aadhaar_back != null) {
            File videoFile = new File(uri_aadhaar_back.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("aadhaar_2", videoFile.getName(), requestVideoFile));
        }
        if (uri_pan != null) {
            File videoFile = new File(uri_pan.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("pan_image", videoFile.getName(), requestVideoFile));
        }
        if (uri_camera != null) {
            File videoFile = new File(uri_camera.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("selfie", videoFile.getName(), requestVideoFile));
        }
        if (uri_gallery_aadhaar_front != null) {
            File videoFile = new File(uri_gallery_aadhaar_front.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("aadhaar_1", videoFile.getName(), requestVideoFile));
        }
        if (uri_gallery_aadhaar_back != null) {
            File videoFile = new File(uri_gallery_aadhaar_back.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("aadhaar_2", videoFile.getName(), requestVideoFile));
        }
        if (uri_gallery_pan != null) {
            File videoFile = new File(uri_gallery_pan.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("pan_image", videoFile.getName(), requestVideoFile));
        }
        if (uri_gallery_camera != null) {
            File videoFile = new File(uri_gallery_camera.getPath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("image/*"), videoFile);
            parts.add(MultipartBody.Part.createFormData("selfie", videoFile.getName(), requestVideoFile));
        }
*/

/*
        apiInterface.kycDetails(param1, param2,param3, parts).enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
//                ResponsePOJO responsePOJO = null;
*/
/*
                try {
                  ResponsePOJO  responsePOJOo = response.body();
                    String reponse=responsePOJOo.getResponse();
                    if(reponse.contains("Successfully Submitted")) {
                        Toast.makeText(context, "Successful! KYC details Submitted!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, BankDetailsActivity.class);
                        intent.putExtra("mobile_number", intent_mobile_number);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
*//*


            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(context, "Sorry!Server Error!!", Toast.LENGTH_LONG).show();

            }
        });
*/

        Call<ResponsePOJO> responsePOJOCall = apiInterface.kycDetails(param1, param2, param3, parts);
        responsePOJOCall.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
                ResponsePOJO responsePOJOo = null;
                try {
                    responsePOJOo = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert responsePOJOo != null;
                String reponse = responsePOJOo.getResponse();
                System.out.println("response" + reponse);
                if (reponse.contains("Successfully Submitted")) {
                    Toast.makeText(context, "Successful! KYC details Submitted!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, BankDetailsActivity.class);
                    intent.putExtra("mobile_number", intent_mobile_number);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {

            }
        });


    }
/*
    private void request(){
        String charset = "UTF-8";

        if(uri_aadhaar_back!=null) {
            file_aadhaar_back = new File(uri_aadhaar_back.getPath());
        }
        if(uri_pan!=null) {
            file_pan = new File(uri_pan.getPath());
        }

        if(uri_camera!=null) {
            file_camera = new File(uri_camera.getPath());
        }

        String requestURL = "http://www.pesservices.org/api/user/kyc_details.php";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("aadhaar_no", string_aadhaar_number);
            multipart.addFormField("pan_no", string_pan_nunmber);

            if(uri_aadhaar_back==null) {
                file_aadhaar_front = new File(uri_aadhaar_front.getPath());
                multipart.addFilePart("aadhaar_1", file_aadhaar_front);
            }else{

                file_gallery_adhhar_front=new File(uri_gallery_aadhaar_front.getPath());
                multipart.addFilePart("aadhaar_1", file_gallery_adhhar_front);
            }
            if(uri_aadhaar_back==null){
                file_aadhaar_back = new File(uri_aadhaar_back.getPath());
                multipart.addFilePart("aadhaar_2", file_aadhaar_back);
            }else{
                file_gallery_adhhar_back=new File(uri_gallery_aadhaar_back.getPath());
                multipart.addFilePart("aadhaar_2", file_gallery_adhhar_back);
            }

            if(uri_pan==null){
                file_pan = new File(uri_pan.getPath());
                multipart.addFilePart("pan_image", file_pan);
            }else{
                file_gallery_pan=new File(uri_gallery_pan.getPath());
                multipart.addFilePart("pan_image", file_gallery_pan);
            }
            if(uri_camera==null){
                file_camera = new File(uri_camera.getPath());
                multipart.addFilePart("selfie", file_pan);
            }else{
                file_gallery_camera=new File(uri_gallery_camera.getPath());
                multipart.addFilePart("selfie", file_gallery_camera);
            }

*/
/*
            multipart.addFilePart("aadhaar_1", uploadFile1);
            multipart.addFilePart("aadhaar_2", uploadFile2);
            multipart.addFilePart("pan_image", uploadFile2);
            multipart.addFilePart("selfie", uploadFile2);
*//*


            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println("responses:"+line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
*/

    private void imageLoadDialog() {
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialoge_image_load);
        radioGroup = dialog.findViewById(R.id.id_custom_dialog_image_load_RG);
        RadioButton radioButton_Camera = dialog.findViewById(R.id.id_custom_dialog_image_load_camera_RB);
        RadioButton radioButton_Gallery = dialog.findViewById(R.id.id_custom_dialog_image_load_gallery_RB);


        Button dialogButton = (Button) dialog.findViewById(R.id.id_custom_dialog_image_load_cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void checkCameraPermissions(int cameraRequest) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                switch (cameraRequest) {
                    case AADHAAR_FRONT_CODE:
                        Intent cameraIntent_Front = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent_Front, AADHAAR_FRONT_CODE);
/*
                            }
                        }
*/

//                        startActivityForResult(cameraIntent_Front, AADHAAR_FRONT_CODE);

                        break;
                    case AADHAAR_BACK_CODE:
                        Intent cameraIntent_back = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                        if(cameraIntent_back.resolveActivity(getPackageManager()) != null) {
                            //Create a file to store the image
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                                cameraIntent_back.putExtra(MediaStore.EXTRA_OUTPUT,
                                        photoURI);

                                startActivityForResult(cameraIntent_back, AADHAAR_BACK_CODE);
                            }
                        }
*/
                        startActivityForResult(cameraIntent_back, AADHAAR_BACK_CODE);

                        break;
                    case PAN_CODE:
                        Intent cameraIntent_pan = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                        if(cameraIntent_pan.resolveActivity(getPackageManager()) != null) {
                            //Create a file to store the image
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                                cameraIntent_pan.putExtra(MediaStore.EXTRA_OUTPUT,
                                        photoURI);

                                startActivityForResult(cameraIntent_pan, PAN_CODE);
                            }
                        }
*/
                        startActivityForResult(cameraIntent_pan, PAN_CODE);

                        break;
                    case IMAGE_CAPTURE_CODE:
                        Intent cameraIntent_capture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                        if(cameraIntent_capture.resolveActivity(getPackageManager()) != null) {
                            //Create a file to store the image
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                                cameraIntent_capture.putExtra(MediaStore.EXTRA_OUTPUT,
                                        photoURI);

                                startActivityForResult(cameraIntent_capture, IMAGE_CAPTURE_CODE);
                            }
                        }
*/
                        startActivityForResult(cameraIntent_capture, IMAGE_CAPTURE_CODE);

                        break;
                    case GALLERY_AADHAAR_FRONT_CODE:
                        Intent galleryIntent_Front = new Intent();
                        galleryIntent_Front.setType("image/*");
                        galleryIntent_Front.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(galleryIntent_Front, GALLERY_AADHAAR_FRONT_CODE);
                        break;
                    case GALLERY_AADHAAR_BACK_CODE:
                        Intent galleryIntent_back = new Intent();
                        galleryIntent_back.setType("image/*");
                        galleryIntent_back.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(galleryIntent_back, GALLERY_AADHAAR_BACK_CODE);
                        break;
                    case GALLERY_PAN_CODE:
                        Intent galleryIntent_pan = new Intent();
                        galleryIntent_pan.setType("image/*");
                        galleryIntent_pan.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(galleryIntent_pan, GALLERY_PAN_CODE);
                        break;
                    case GALLERY_IMAGE_CODE:
                        Intent galleryIntent_capture = new Intent();
                        galleryIntent_capture.setType("image/*");
                        galleryIntent_capture.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(galleryIntent_capture, GALLERY_IMAGE_CODE);
                        break;

                }

            }
        } else {
            switch (cameraRequest) {
                case AADHAAR_FRONT_CODE:
                    Intent cameraIntent_Front = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent_Front, AADHAAR_FRONT_CODE);
//                    startActivityForResult(cameraIntent_Front, AADHAAR_FRONT_CODE);

//                    startActivityForResult(cameraIntent_Front, AADHAAR_FRONT_CODE);

                    break;
                case AADHAAR_BACK_CODE:
                    Intent cameraIntent_back = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                    if(cameraIntent_back.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                            cameraIntent_back.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);

                            startActivityForResult(cameraIntent_back, AADHAAR_BACK_CODE);
                        }
                    }
*/
                    startActivityForResult(cameraIntent_back, AADHAAR_BACK_CODE);

                    break;
                case PAN_CODE:
                    Intent cameraIntent_pan = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                    if(cameraIntent_pan.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                            cameraIntent_pan.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);

                            startActivityForResult(cameraIntent_pan, PAN_CODE);
                        }
                    }
*/
                    startActivityForResult(cameraIntent_pan, PAN_CODE);

                    break;
                case IMAGE_CAPTURE_CODE:
                    Intent cameraIntent_capture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                    if(cameraIntent_capture.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                            cameraIntent_capture.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);

                            startActivityForResult(cameraIntent_capture, IMAGE_CAPTURE_CODE);
                        }
                    }
*/
                    startActivityForResult(cameraIntent_capture, IMAGE_CAPTURE_CODE);

                    break;
                case GALLERY_AADHAAR_FRONT_CODE:
                    Intent galleryIntent_Front = new Intent();
                    galleryIntent_Front.setType("image/*");
                    galleryIntent_Front.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_Front, GALLERY_AADHAAR_FRONT_CODE);
                    break;
                case GALLERY_AADHAAR_BACK_CODE:
                    Intent galleryIntent_back = new Intent();
                    galleryIntent_back.setType("image/*");
                    galleryIntent_back.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_back, GALLERY_AADHAAR_BACK_CODE);
                    break;
                case GALLERY_PAN_CODE:
                    Intent galleryIntent_pan = new Intent();
                    galleryIntent_pan.setType("image/*");
                    galleryIntent_pan.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(galleryIntent_pan, GALLERY_PAN_CODE);
                    break;
                case GALLERY_IMAGE_CODE:
                    Intent galleryIntent_capture = new Intent();
                    galleryIntent_capture.setType("image/*");
                    galleryIntent_capture.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_capture, GALLERY_IMAGE_CODE);
                    break;

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case AADHAAR_FRONT_CODE:
                    Intent cameraIntent_Front = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent_Front, AADHAAR_FRONT_CODE);
                    break;
                case AADHAAR_BACK_CODE:
                    Intent cameraIntent_back = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                    if(cameraIntent_back.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                            cameraIntent_back.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);

                            startActivityForResult(cameraIntent_back, AADHAAR_BACK_CODE);
                        }
                    }
*/
                    startActivityForResult(cameraIntent_back, AADHAAR_BACK_CODE);

                    break;
                case PAN_CODE:
                    Intent cameraIntent_pan = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                    if(cameraIntent_pan.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                            cameraIntent_pan.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);

                            startActivityForResult(cameraIntent_pan, PAN_CODE);
                        }
                    }
*/
                    startActivityForResult(cameraIntent_pan, PAN_CODE);

                    break;
                case IMAGE_CAPTURE_CODE:
                    Intent cameraIntent_capture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
/*
                    if(cameraIntent_capture.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this, "com.vmrits.android.app.fileprovider", photoFile);
                            cameraIntent_capture.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);

                            startActivityForResult(cameraIntent_capture, IMAGE_CAPTURE_CODE);
                        }
                    }
*/
                    startActivityForResult(cameraIntent_capture, IMAGE_CAPTURE_CODE);

                    break;
                case GALLERY_AADHAAR_FRONT_CODE:
                    Intent galleryIntent_Front = new Intent();
                    galleryIntent_Front.setType("image/*");
                    galleryIntent_Front.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_Front, GALLERY_AADHAAR_FRONT_CODE);
                    break;
                case GALLERY_AADHAAR_BACK_CODE:
                    Intent galleryIntent_back = new Intent();
                    galleryIntent_back.setType("image/*");
                    galleryIntent_back.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_back, GALLERY_AADHAAR_BACK_CODE);
                    break;
                case GALLERY_PAN_CODE:
                    Intent galleryIntent_pan = new Intent();
                    galleryIntent_pan.setType("image/*");
                    galleryIntent_pan.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_pan, GALLERY_PAN_CODE);
                    break;
                case GALLERY_IMAGE_CODE:
                    Intent galleryIntent_capture = new Intent();
                    galleryIntent_capture.setType("image/*");
                    galleryIntent_capture.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent_capture, GALLERY_IMAGE_CODE);
                    break;

            }
        } else {
            Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
        }

    }

    /*
            if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new
                            Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }

            }
    */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AADHAAR_FRONT_CODE:
/*
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    imageView_aadhaar_front.setImageBitmap(photo);
*/
//                    Toast.makeText(context,""+string_aadhaar_front,Toast.LENGTH_LONG).show();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageView_aadhaar_front.setImageBitmap(photo);
                    file_aadhaar_front = convertBitmapToFile(photo);
//                    string_aadhaar_front=bitMapToString(photo);


/*
                    uri_aadhaar_front=Uri.fromFile(new File(imageFilePath));
                    imageView_aadhaar_front.setImageURI(uri_aadhaar_front);
                    try {
                        bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_aadhaar_front);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    bitmap.recycle();
                    string_aadhaar_front=byteArray.toString();
*/
                    break;
                case AADHAAR_BACK_CODE:
/*
                    uri_aadhaar_back=Uri.fromFile(new File(imageFilePath));
                    imageView_aadhaar_back.setImageURI(uri_aadhaar_back);
                    try {
                        bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_aadhaar_back);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                    bitmap.recycle();
                    string_aadhaar_back=byteArray2.toString();
*/
                    Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                    imageView_aadhaar_back.setImageBitmap(photo2);
//                    string_aadhaar_back=bitMapToString(photo2);
                    file_aadhaar_back = convertBitmapToFile(photo2);

                    break;
                case PAN_CODE:
/*
                    uri_pan=Uri.fromFile(new File(imageFilePath));
                    imageView_pan.setImageURI(uri_pan);
                    try {
                        bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_pan);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray3 = byteArrayOutputStream.toByteArray();
                    bitmap.recycle();
                    string_pan=byteArray3.toString();
*/
                    Bitmap photo3 = (Bitmap) data.getExtras().get("data");
                    imageView_pan.setImageBitmap(photo3);
//                    string_pan=bitMapToString(photo3);
                    file_pan = convertBitmapToFile(photo3);
                    break;
                case IMAGE_CAPTURE_CODE:
                    Bitmap photo4 = (Bitmap) data.getExtras().get("data");
                    imageView_camera.setImageBitmap(photo4);
//                    string_camera=bitMapToString(photo4);
                    file_camera = convertBitmapToFile(photo4);

/*
                    uri_camera=Uri.fromFile(new File(imageFilePath));
                    imageView_camera.setImageURI(uri_camera);
                    try {
                        bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_camera);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray4 = byteArrayOutputStream.toByteArray();
                    bitmap.recycle();
                    string_camera=byteArray4.toString();
*/
                    break;
                case GALLERY_AADHAAR_FRONT_CODE:
                    Bitmap bitmap = null;
                    uri_gallery_aadhaar_front = data.getData();
//                    imageView_aadhaar_front.setImageURI(uri_gallery_aadhaar_front);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_gallery_aadhaar_front);
                        // Log.d(TAG, String.valueOf(bitmap));

                        imageView_aadhaar_front.setImageBitmap(bitmap);
                        file_gallery_adhhar_front = convertBitmapToFile(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

/*
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_gallery_aadhaar_front);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray5 = byteArrayOutputStream.toByteArray();
                    bitmap.recycle();
                    string_gallery_aadhaar_front=byteArray5.toString();
*/
                    break;
                case GALLERY_AADHAAR_BACK_CODE:
                    uri_gallery_aadhaar_back = data.getData();
                    Bitmap bitmap1 = null;
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_gallery_aadhaar_back);
                        // Log.d(TAG, String.valueOf(bitmap));

                        imageView_aadhaar_back.setImageBitmap(bitmap1);
                        file_gallery_adhhar_back = convertBitmapToFile(bitmap1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

/*
                    try {
                        bitmap1= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_gallery_aadhaar_back);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream1);
                    byte[] byteArray6 = byteArrayOutputStream1.toByteArray();
                    bitmap1.recycle();
                    string_gallery_aadhaar_back=byteArray6.toString();
*/
                    break;
                case GALLERY_PAN_CODE:
                    uri_gallery_pan = data.getData();
                    Bitmap bitmap2 = null;

                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_gallery_pan);
                        // Log.d(TAG, String.valueOf(bitmap));

                        imageView_pan.setImageBitmap(bitmap2);
                        file_gallery_pan = convertBitmapToFile(bitmap2);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

/*
                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_gallery_pan);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream2);
                    byte[] byteArray7 = byteArrayOutputStream2.toByteArray();
                    bitmap2.recycle();
                    string_gallery_pan=byteArray7.toString();
*/

                    break;

                case GALLERY_IMAGE_CODE:
                    uri_gallery_camera = data.getData();
                    Bitmap bitmap3 = null;

                    try {
                        bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_gallery_camera);
                        // Log.d(TAG, String.valueOf(bitmap));

                        imageView_camera.setImageBitmap(bitmap3);
                        file_gallery_camera = convertBitmapToFile(bitmap3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

/*
                    try {
                        bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_gallery_camera);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
                    bitmap3.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream3);
                    byte[] byteArray8 = byteArrayOutputStream3.toByteArray();
                    bitmap3.recycle();
                    string_gallery_camera=byteArray8.toString();
*/
                    break;

            }

        }
    }

    public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
//        bitmap.recycle();
//        String temp=b.toString();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }
}



