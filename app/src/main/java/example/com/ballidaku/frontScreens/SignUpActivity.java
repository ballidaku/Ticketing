package example.com.ballidaku.frontScreens;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URISyntaxException;
import java.util.List;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonDialogs;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity
{
    String TAG = SignUpActivity.class.getSimpleName();

    Context context;

    ActivitySignUpBinding activitySignUpBinding;
    View view;

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


    public void onSignUpClicked(String firstName,String lastName, String email, String phoneNumber, String password, String confirmPassword)
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

          /*  file = new File(imagePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("profile_pic", file.getName(), mFile);

            RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email);


            @retrofit2.http.Multipart
            @POST("users/action.php?action=Register")
            Call<SignUpHuttson> getSignUpInsurance(@Part("email") RequestBody email,
                @Part("password") RequestBody password,
                @Part("firstname") RequestBody firstName,
                @Part("lastname") RequestBody lastName,
                @Part("gender") RequestBody gender,
                @Part("profile_type") RequestBody user,
                @Part("profile_address") RequestBody address,
                @Part("about_us") RequestBody aboutUs,
                @Part("apiKey") RequestBody apiKey,
                @Part("secretKey") RequestBody secretKey,
                @Part("version") RequestBody vesrion,
                @Part("city") RequestBody city,
                @Part("state") RequestBody state,
                @Part("zip") RequestBody zip,
                @Part("dob") RequestBody dob,
                @Part("coverage_amount") RequestBody coverage_amount,
                @Part("coverage_type") RequestBody coverage_type,
                @Part("firstAnswer") RequestBody firstAnswer,
                @Part("secondAnswer") RequestBody secondAnswer,
                @Part("thirdAnswer") RequestBody thirdAnswer,
                @Part("fourthAnswer") RequestBody fourthAnswer,
                @Part("fifthAnswer") RequestBody fifthAnswer,
                @Part("sixthAnswer") RequestBody sixthAnswer,
                @Part("nominee_email") RequestBody nominee_email,
                @Part("nominee_phone") RequestBody nominee_phone,
                @Part("nominee_first_name") RequestBody nominee_firstName,
                @Part("nominee_last_name") RequestBody nominee_lastName,
                @Part("nominee_relationship") RequestBody nominee_relationship,
                @Part MultipartBody.Part profile_pic);*/

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
                    String imagePath = CommonMethods.getInstance().getRealPathFromURI(context, resultUri);
                    CommonMethods.getInstance().showImageGlide(context, activitySignUpBinding.imageViewProfile, imagePath);
                }
                catch (URISyntaxException e)
                {
                    e.printStackTrace();
                }


                break;

        }
    }


}