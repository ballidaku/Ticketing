package example.com.ballidaku.commonClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import example.com.ballidaku.BuildConfig;
import example.com.ballidaku.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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
    private SimpleDateFormat tfi = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private SimpleDateFormat tfo = new SimpleDateFormat("dd MMM yyyy", Locale.US);

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
    public String getFormattedDateTime(String dateTime)
    {
        Date date = null;
        String str = null;

        try {
            date = tfi.parse(dateTime);
            str = tfo.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
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


    public JsonObject convertStringToJson(String json)
    {
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(json);
    }


    public Call post(String url, String json, Callback callback)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, json);

        Request request;
        if (json.isEmpty())
        {
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();
        }
        else
        {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();
        }

        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;
    }

    public Call postMediaData(String url, String image, String json, Callback callback)
    {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        RequestBody body;
        if(image.isEmpty())
        {
             body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("Data", json)
                    .build();
        }
        else
        {
             body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", new File(image).getName(), RequestBody.create(MediaType.parse("image/*"), new File(image)))
                    .addFormDataPart("Data", json)
                    .build();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;
    }


    public Call postDataWithAuth(Context context,String url, String json, Callback callback)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        MediaType mediaType;
        if(url.equals(MyConstants.SAVE_TICKET))
        {
            mediaType = MediaType.parse("application/json; charset=utf-8");
        }
        else
        {
            mediaType = MediaType.parse("application/x-www-form-urlencoded");
        }
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "bearer "+MySharedPreference.getInstance().getUserData(context,MyConstants.ACCESS_TOKEN))
                .build();


        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;
    }


    public String convertBeanToString(TicketModel ticketModel)
    {
        Gson gson = new Gson();
        return gson.toJson(ticketModel);
    }

    public HistoryModel convertStringToBean(String json)
    {
        Gson gson = new Gson();
        return  gson.fromJson(json, HistoryModel.class);
    }

    public JsonObject convertStringToJsonObject(String json)
    {
        JsonParser jsonParser = new JsonParser();
        return  (JsonObject)jsonParser.parse(json);
    }


    public boolean isInternetAvailable() {

        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(1000, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }


    public boolean isInternetAvailable(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}
