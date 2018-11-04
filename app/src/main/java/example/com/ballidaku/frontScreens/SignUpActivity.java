package example.com.ballidaku.frontScreens;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonDialogs;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.commonClasses.MySharedPreference;
import example.com.ballidaku.databinding.ActivitySignUpBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity
{
    String TAG = SignUpActivity.class.getSimpleName();

    Context context;

    ActivitySignUpBinding activitySignUpBinding;
    View view;

    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        context = this;
        view = activitySignUpBinding.getRoot();
        activitySignUpBinding.setViewModel(this);

    }

    public void onSignInClicked()
    {
        finish();
    }


    public void onSignUpClicked(String firstName, String lastName, String email, String phoneNumber, String password, String confirmPassword)
    {
        CommonMethods.getInstance().hideKeypad(this);

        if (firstName.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_first_name));
        }
        else if (lastName.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_last_name));
        }
        else if (email.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            showSnackbarMsg(context.getString(R.string.please_enter_valid_email));
        }
        else if (phoneNumber.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_phone_number));
        }
        else if (!CommonMethods.getInstance().isValidMobile(phoneNumber))
        {
            showSnackbarMsg(context.getString(R.string.number_limit));
        }
        else if (password.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            showSnackbarMsg(context.getString(R.string.password_limit));
        }
        else if (confirmPassword.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_confirm_password));
        }
        else if (confirmPassword.length() < 6)
        {
            showSnackbarMsg(context.getString(R.string.confirm_password_limit));
        }
        else if (!password.equals(confirmPassword))
        {
            showSnackbarMsg(context.getString(R.string.password_confirm_not_matched));
        }
        else
        {
            signUpApiHit(firstName, lastName, email, phoneNumber, password);
        }
    }

    public void onImageClick()
    {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener()
                {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if (report.areAllPermissionsGranted())
                        {
                            CommonDialogs.getInstance().selectImageDialog(context);
                        }
                        else if (report.isAnyPermissionPermanentlyDenied())
                        {
                            CommonMethods.getInstance().showSnackbar(view, context, getString(R.string.on_permission_decline));
                            CommonMethods.getInstance().openSettings(context);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public void showSnackbarMsg(String string)
    {
        CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, string);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode)
        {
            case MyConstants.CAMERA_REQUEST:

                String imagePathTemp = CommonMethods.getInstance().getTempraryImageFile(context).toString();
                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(Uri.parse("file://" + imagePathTemp))
                        .setAspectRatio(1, 1)
                        .start(this);
                break;


            case MyConstants.PICK_IMAGE_GALLERY:

                Uri selectedImage = data.getData();
                // CropImage.activity(Uri.parse("file://" + imagePathTemp))
                CropImage.activity(selectedImage)
                        .setAspectRatio(1, 1)
                        .start(this);

                break;


            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                Uri resultUri = result.getUri();

                try
                {
                    imagePath = CommonMethods.getInstance().getRealPathFromURI(context, resultUri);
                    CommonMethods.getInstance().showImageGlide(context, activitySignUpBinding.imageViewProfile, imagePath);
                }
                catch (URISyntaxException e)
                {
                    e.printStackTrace();
                }


                break;

        }
    }


    void signUpApiHit(String firstName, String lastName, String email, String phoneNumber, String password)
    {
        CommonDialogs.getInstance().showProgressDialog(context);

        String json = "{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phoneNumber\":\"" + phoneNumber + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        CommonMethods.getInstance().postMediaData("http://ticketing.hpwildlife.gov.in/Registration", imagePath, json, new Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                Log.e(TAG, "onFailure  " + e.toString());
                CommonDialogs.getInstance().dismissDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                CommonDialogs.getInstance().dismissDialog();

                if (response.isSuccessful())
                {
                    //{"Status":400,"Message":"Try with other email as 'a@gmail.com' already exists."}
                    //{"Status":200,"Message":"User has been created successfully.","userId":9,"firstName":"A","lastName":"A","phoneNumber":"1234564890","email":"z@gmail.com","password":null,"profileImageName":"ticketing.hpwildlife.gov.in\\ProfileImage\\d0e9ebd0-17aa-4118-9f50-0124949af775.jpg"}

                    String responseStr = response.body().string();
                    Log.e(TAG, responseStr);
                    JsonObject jsonObject=CommonMethods.getInstance().convertStringToJson(responseStr);
                    CommonMethods.getInstance().showSnackbar(view,context,jsonObject.get(MyConstants.MESSAGE).getAsString());
                    if(jsonObject.get(MyConstants.STATUS).getAsInt()==200)
                    {
                        MySharedPreference.getInstance().saveUser(context, responseStr);
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }

                }
                else
                {
                    String errorStr = response.body().string();
//                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(errorStr);
//                    CommonMethods.getInstance().showSnackbar(view, context, jsonObject.get(MyConstants.ERROR_DESCRIPTION).getAsString());
                }
            }
        });
    }


}