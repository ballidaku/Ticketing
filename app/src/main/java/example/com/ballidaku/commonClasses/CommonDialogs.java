package example.com.ballidaku.commonClasses;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import example.com.ballidaku.R;
import example.com.ballidaku.mPin.IndicatorDots;
import example.com.ballidaku.mPin.PinLockListener;
import example.com.ballidaku.mPin.PinLockView;


/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class CommonDialogs
{
    String TAG = CommonDialogs.class.getSimpleName();

    public Dialog dialog;

    private static CommonDialogs instance = new CommonDialogs();

    public static CommonDialogs getInstance()
    {
        return instance;
    }


    public void dismissDialog()
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    public void showForgotPasswordDialog(Context context, CommonInterfaces commonInterfaces)
    {
        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editTextForgotPassword = dialog.findViewById(R.id.editTextForgotPassword);


        TextInputLayout textInputLayoutEmail = dialog.findViewById(R.id.textInputLayoutEmail);

        dialog.findViewById(R.id.textViewCancel).setOnClickListener(view ->
        {
            CommonMethods.getInstance().hideKeypad(context, editTextForgotPassword);
            dismissDialog();
        });

        dialog.findViewById(R.id.textViewSend).setOnClickListener(view ->
        {
            final String email = editTextForgotPassword.getText().toString().trim();
            if (email.isEmpty())
            {
                textInputLayoutEmail.setError(context.getString(R.string.please_enter_email));
            }
            else if (!CommonMethods.getInstance().isValidEmail(email))
            {
                textInputLayoutEmail.setError(context.getString(R.string.please_enter_valid_email));
            }
            else
            {
                dismissDialog();
                commonInterfaces.onResponse(email);
            }
        });

        editTextForgotPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

                String email = editable.toString();
                if (email.isEmpty())
                {
                    textInputLayoutEmail.setError(context.getString(R.string.please_enter_email));
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                else if (!CommonMethods.getInstance().isValidEmail(email))
                {
                    textInputLayoutEmail.setError(context.getString(R.string.please_enter_valid_email));
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                else
                {
                    textInputLayoutEmail.setErrorEnabled(false);
                }
            }
        });
    }

    public void showChangePasswordDialog(Context context, CommonInterfaces commonInterfaces)
    {
        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editTextOldPassword = dialog.findViewById(R.id.editTextOldPassword);
        EditText editTextNewPassword = dialog.findViewById(R.id.editTextNewPassword);
        EditText editTextConfirmNewPassword = dialog.findViewById(R.id.editTextConfirmNewPassword);


        TextInputLayout textInputLayoutOldPasswod = dialog.findViewById(R.id.textInputLayoutOldPasswod);
        TextInputLayout textInputLayoutNewPassword = dialog.findViewById(R.id.textInputLayoutNewPassword);
        TextInputLayout textInputLayoutConfirmNewPassword = dialog.findViewById(R.id.textInputLayoutConfirmNewPassword);

        dialog.findViewById(R.id.textViewCancel).setOnClickListener(view ->
        {
            CommonMethods.getInstance().hideKeypad(context, editTextOldPassword);
            dismissDialog();
        });

        dialog.findViewById(R.id.textViewSend).setOnClickListener(view ->
        {
            final String oldPassword = editTextOldPassword.getText().toString().trim();
            final String newPassword = editTextNewPassword.getText().toString().trim();
            final String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

            if (oldPassword.isEmpty())
            {
                textInputLayoutOldPasswod.setError(context.getString(R.string.old_passwor_error));
            }
            else if (oldPassword.length() < 6)
            {
                textInputLayoutOldPasswod.setError(context.getString(R.string.password_limit));
            }
            else if (newPassword.isEmpty())
            {
                textInputLayoutNewPassword.setError(context.getString(R.string.new_passwor_error));
            }
            else if (newPassword.length() < 6)
            {
                textInputLayoutNewPassword.setError(context.getString(R.string.password_limit));
            }
            else if (confirmNewPassword.isEmpty())
            {
                textInputLayoutConfirmNewPassword.setError(context.getString(R.string.confirm_new_passwor_error));
            }
            else if (confirmNewPassword.length() < 6)
            {
                textInputLayoutConfirmNewPassword.setError(context.getString(R.string.password_limit));
            }
            else if (!newPassword.equals(confirmNewPassword))
            {
                textInputLayoutNewPassword.setError(context.getString(R.string.match_passwor_error));
                textInputLayoutConfirmNewPassword.setError(context.getString(R.string.match_passwor_error));
            }
            else
            {
                dismissDialog();
                commonInterfaces.onChange(oldPassword,newPassword);
            }
        });
    }
    public void showProgressDialog(Context context)
    {
        dismissDialog();
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

    public void showMessageDialog(Context context, String message)
    {
        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_message);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setText(Html.fromHtml(message));
        dialog.findViewById(R.id.textViewOk).setOnClickListener(view -> dialog.dismiss());
    }

    public void selectImageDialog(final Context context)
    {
        try
        {
            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, context.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED)
            {
                final CharSequence[] options = {context.getString(R.string.take_photo), context.getString(R.string.choose_from_gallery), context.getString(R.string.cancel)};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.select_option));
                builder.setItems(options, (dialog, item) ->
                {
                    if (options[item].equals(context.getString(R.string.take_photo)))
                    {
                        dialog.dismiss();
                        CommonMethods.getInstance().capture(context);
                    }
                    else if (options[item].equals(context.getString(R.string.choose_from_gallery)))
                    {
                        dialog.dismiss();

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ((Activity) context).startActivityForResult(pickPhoto, MyConstants.PICK_IMAGE_GALLERY);

                    }
                    else if (options[item].equals(context.getString(R.string.cancel)))
                    {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
            else
                CommonMethods.getInstance().showToast(context, context.getString(R.string.on_permission_decline));
        }
        catch (Exception e)
        {
            CommonMethods.getInstance().showToast(context, context.getString(R.string.on_permission_decline));
            e.printStackTrace();
        }
    }


    private PinLockView mPinLockView;
    private int savedPasscode ;
    private CommonInterfaces commonInterfaces;
    public void showPasscodeDialog(Context context,int passcode,CommonInterfaces commonInterfaces)
    {
        this.commonInterfaces=commonInterfaces;

        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_passcode);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        mPinLockView = dialog.findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = dialog.findViewById(R.id.indicator_dots);


        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);

        savedPasscode =  passcode;
    }

    private PinLockListener mPinLockListener = new PinLockListener()
    {
        @Override
        public void onComplete(String pin)
        {

            if(savedPasscode==Integer.parseInt(pin))
            {
                dismissDialog();
                commonInterfaces.onSuccess();
            }
            else
            {
                commonInterfaces.onFailure();
            }
        }

        @Override
        public void onEmpty()
        {
//            Log.e(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin)
        {
//            Log.e(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }

        @Override
        public void onReset()
        {
            mPinLockView.resetPinLockView();
        }
    };



   /* public void showMessageDialog(Context context, String fromWhere, View.OnClickListener onClickListener)
    {
        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_message_with_buttons);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);

        TextView textViewNegative = dialog.findViewById(R.id.textViewNegative);
        TextView textViewPositive = dialog.findViewById(R.id.textViewPositive);

        textViewPositive.setOnClickListener(onClickListener);
        textViewNegative.setOnClickListener(view -> dialog.dismiss());


        if(fromWhere.equals(MyConstant.LOGOUT))
        {
            textViewTitle.setText(context.getString(R.string.logout_confirmation));
            textViewMessage.setText(context.getString(R.string.logout_message));
            textViewNegative.setText(context.getString(R.string.cancel));
            textViewPositive.setText(context.getString(R.string.logout));
        }
        else if(fromWhere.equals(MyConstant.CHANGE_PASSWORD))
        {
            textViewTitle.setText(context.getString(R.string.change_password_confirmation));
            textViewMessage.setText(context.getString(R.string.change_password_message));
            textViewNegative.setText(context.getString(R.string.cancel));
            textViewPositive.setText(context.getString(R.string.yes));
        }
    }*/


   /* public void showDeleteAlertDialog(Context context, final CommonInterfaces.deleteDetail deleteDetail)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.delete_entry))
                .setMessage(context.getString(R.string.delete_confirmation))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteDetail.onDelete())
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showImportAlertDialog(Context context, final CommonInterfaces.importData importData)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.import_data))
                .setMessage(context.getString(R.string.import_confirmation))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> importData.onImportConfirmation())
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }*/


}
