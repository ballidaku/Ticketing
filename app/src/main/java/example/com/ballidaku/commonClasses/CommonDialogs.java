package example.com.ballidaku.commonClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
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


/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class CommonDialogs
{
    String TAG= CommonDialogs.class.getSimpleName();

    public Dialog dialog;

    private static CommonDialogs instance = new CommonDialogs();

    public static CommonDialogs getInstance()
    {
        return instance;
    }


    private void dismissDialog()
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

        dialog.findViewById(R.id.textViewCancel).setOnClickListener(view -> dismissDialog());

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

                String email=editable.toString();
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



    public void progressDialog(Context context)
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
