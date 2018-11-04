package example.com.ballidaku.frontScreens;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;

import java.io.IOException;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonDialogs;
import example.com.ballidaku.commonClasses.CommonInterfaces;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.commonClasses.MySharedPreference;
import example.com.ballidaku.databinding.ActivityLoginBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity
{
    String TAG = LoginActivity.class.getSimpleName();

    Context context;

    ActivityLoginBinding activityLoginBinding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setViewModel(this);
        view = activityLoginBinding.getRoot();
        activityLoginBinding.imageViewPasswordShowHide.setTag(false);

    }


    public void onSignInClicked(String email, String password)
    {
        CommonMethods.getInstance().hideKeypad(this);

        if (email.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            showSnackbarMsg(context.getString(R.string.please_enter_valid_email));
        }
        else if (password.isEmpty())
        {
            showSnackbarMsg(context.getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            showSnackbarMsg(context.getString(R.string.password_limit));
        }
        else
        {
            loginApiHit(email, password);
        }

//     startActivity(new Intent(context,MainActivity.class));
    }

    public void onPasswordShowHideClicked()
    {
        boolean isVisible = (boolean) activityLoginBinding.imageViewPasswordShowHide.getTag();
        if (isVisible)
        {
            activityLoginBinding.editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            activityLoginBinding.imageViewPasswordShowHide.setTag(false);
            activityLoginBinding.imageViewPasswordShowHide.setImageResource(R.drawable.ic_visibility_off);

        }
        else
        {
            activityLoginBinding.editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            activityLoginBinding.imageViewPasswordShowHide.setTag(true);
            activityLoginBinding.imageViewPasswordShowHide.setImageResource(R.drawable.ic_visibility_on);
        }
        activityLoginBinding.editTextPassword.setTextAppearance(context, R.style.EditTextTheme);

    }

    public void onSignUpClicked()
    {
        startActivity(new Intent(context, SignUpActivity.class));
    }

    public void onForgotClicked()
    {
        CommonDialogs.getInstance().showForgotPasswordDialog(context, new CommonInterfaces()
        {
            @Override
            public void onResponse(String response)
            {

            }
        });
    }


    public void showSnackbarMsg(String string)
    {
        CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, string);
    }


    void loginApiHit(String email, String password)
    {

        CommonDialogs.getInstance().showProgressDialog(context);

        String json = "grant_type=password&username=" + email + "&password=" + password;
        CommonMethods.getInstance().post("http://ticketing.hpwildlife.gov.in/ValidateCredentials", json,new Callback()
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
                    String responseStr = response.body().string();
                    Log.e(TAG, responseStr);
                    MySharedPreference.getInstance().saveUser(context, responseStr);

                    startActivity(new Intent(context,MainActivity.class));
                    finish();

                }
                else
                {
                    String errorStr = response.body().string();
                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(errorStr);
                    CommonMethods.getInstance().showSnackbar(view, context, jsonObject.get(MyConstants.ERROR_DESCRIPTION).getAsString());
                }
            }
        });


    }




}
