package example.com.ballidaku.commonClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import example.com.ballidaku.BuildConfig;
import example.com.ballidaku.R;

public class CommonMethods
{
    String TAG = CommonMethods.class.getSimpleName();

    private static Toast toast;
    private static Snackbar snackbar;

    private static CommonMethods instance = new CommonMethods();

    public static CommonMethods getInstance()
    {
        return instance;
    }


    public void showToast(Context context, String text)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);

        toast.show();
    }


    /*TO SHOW SNACKBAR*/

    public void showSnackbar(View view, Context context, String message)
    {

        if (snackbar != null)
        {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        snackbar.show();

    }


    public void switchfragment(Context context, Fragment toWhere, boolean willStoreInStack)
    {

        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        if (willStoreInStack)
        {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    public void hideKeypad(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideKeypad(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public String fixedLengthString(int value, int length)
    {
        return String.format("%1$" + length + "s", String.valueOf(value));
    }

    public void showImageGlide(Context context, ImageView imageView, String path)
    {
        Glide.with(context).load(path).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.ic_user_placeholder).error(R.mipmap.ic_user_placeholder)).into(imageView);
    }

    public Uri getTempraryImageFile(Context context)
    {
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "IMG_Temp.jpg");

        Log.e(TAG, "Path " + outputFile.toString());
        return Uri.fromFile(outputFile);
    }

    private SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    private SimpleDateFormat tf = new SimpleDateFormat("hh:mm a", Locale.US);

    public String getDate()
    {
        Date c = Calendar.getInstance().getTime();
        return df.format(c);
    }

    public String getTime()
    {
        Date c = Calendar.getInstance().getTime();
        return tf.format(c);
    }

    public boolean isValidEmail(CharSequence target)
    {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidMobile(String phone)
    {
        boolean check = false;
        check = !Pattern.matches("[a-zA-Z]+", phone) && phone.length() >= 6;
        return check;
    }


    @SuppressLint("NewApi")
    public String getRealPathFromURI(Context context, Uri uri) throws URISyntaxException
    {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri))
        {
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            else if (isDownloadsDocument(uri))
            {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            }
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type))
                {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try
            {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst())
                {
                    return cursor.getString(column_index);
                }
            }
            catch (Exception e)
            {
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    void capture(Context context)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri apkURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getTempraryImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);
        ((Activity) context).startActivityForResult(intent, MyConstants.CAMERA_REQUEST);

    }

    private File getTempraryImageFile()
    {
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "IMG_Temp.jpg");
        Log.e(TAG, "Path " + outputFile.toString());
        return outputFile;
    }

    public void deleteTempraryImage()
    {
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "IMG_Temp.jpg");
        deleteRecursive(outputFile);
    }

    private void deleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public void openSettings(Context context)
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(Context context, int dp)
    {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
