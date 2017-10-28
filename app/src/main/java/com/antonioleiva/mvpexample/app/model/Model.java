package com.antonioleiva.mvpexample.app.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antonioleiva.mvpexample.app.BuildConfig;
import com.antonioleiva.mvpexample.app.R;
import com.antonioleiva.mvpexample.app.info.MainInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by nguye on 4/10/2017.
 */

public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Đường dẫn default để kết nối đến service ASP (không sửa)
     */
    public static final String NAMESPACE = "http://tempuri.org/";
    private static final String TAG = "Model";
    public static String deviceName = Build.MODEL;
    public static String androidOS = Build.VERSION.RELEASE;
    public static String versionName = BuildConfig.VERSION_NAME;


    public static File createImageFile(Context context)
    {
        try
        {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = new File(Environment.getExternalStorageDirectory(),".isight_cache");
            if (!storageDir.exists())
                storageDir.mkdirs();

            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            return image;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static String encodeUrl(String base64){
        String res="";
        byte[] message = base64.getBytes(Charset.forName("UTF-8"));
        res = Base64.encodeToString(message,10);
        return res;
    }

    public static String decodeUrl(String input){
        // Sending side
        // String base64="";
        byte[] decoded = Base64.decode(input,10);
        return new String(decoded, Charset.forName("UTF-8"));
    }

    public static void ToastTB(Context context, String thongbao) {
        Toast.makeText(context, thongbao, Toast.LENGTH_SHORT).show();
    }

    public static void connectPlayStore(Context context) {
        try {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(viewIntent);
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Không thể kết nối...",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return BuildConfig.VERSION_NAME;
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();


        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap xuly(Bitmap bitmap, String path) {
        try {

            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation)

            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = RotateBitmap(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = RotateBitmap(bitmap, 180);
                    break;
                // etc.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // dialog
    public static ProgressDialog dialogModel = null;

    public static void showDialog(Context context, String title, String message, Boolean cancel) {

        if (dialogModel == null) {
            dialogModel = new ProgressDialog(context);
            dialogModel.setIndeterminate(true);
            dialogModel.setCancelable(cancel);
            dialogModel.setTitle(title);
            dialogModel.setMessage(message);
            dialogModel.show();
            dialogModel.setContentView(R.layout.custom_progressdialog);
            dialogModel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        } else {
            dialogModel.dismiss();
            dialogModel.setTitle(title);
            dialogModel.setMessage(message);
            dialogModel.show();
            dialogModel.setContentView(R.layout.custom_progressdialog);
            dialogModel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    public static void closeDialog() {
        if (dialogModel != null) {
            dialogModel.dismiss();
        }
        dialogModel = null;
    }

    public static String getIMEI(Context context) {
       /* if (MainInfo.imeiTest.trim().length() > 0)
            return MainInfo.imeiTest;*/
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId() == null ? "00000000000" : telephonyManager.getDeviceId();
    }

    public static boolean isTimeAutomatic(Context c) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int time = Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0);
            int timezone = Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0);
            Log.d(TAG, "isTimeAutomatic: timezone " + timezone + " time " + time);
            if (time == 1 && timezone == 1)
                flag = true;
            else
                flag = false;
            //flag = (time==1?true:false && timezone==1?true:false);
        } else {
            int timezone = android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME_ZONE, 0);
            int time = android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
            Log.d(TAG, "isTimeAutomatic: timezone " + timezone + " time " + time);
            if (time == 1 && timezone == 1)
                flag = true;
            else
                flag = false;
            //flag = (time==1?true:false && timezone==1?true:false);
        }
        Log.d(TAG, "isTimeAutomatic: flag " + flag);
        return flag;
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateTime() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }

    public static void ShowSettingDatetime(final Context context, String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title);
        ab.setMessage(msg);
        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                context.startActivity(new Intent(
                        android.provider.Settings.ACTION_DATE_SETTINGS));
            }
        }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();
                    }
                });
        ab.show();
    }

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static int CompareTwoTime(String currentDate, String newDate) {
        Calendar calendarcurrent = Calendar.getInstance();
        Calendar calendarnewDate = Calendar.getInstance();
        Date date1 = null;
        try {
            date1 = dateFormatter.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = dateFormatter.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarcurrent.setTime(date1);
        calendarnewDate.setTime(date2);
        //  System.out.println("Compare Result : " + calendarnewDate.compareTo(calendarcurrent));
        /*
        Compare Result =0 là bằng nhau
        Compare Result =-1 là newdate nho hon currentdate
        Compare Result =1 là newdate lon hon currentdate
        * */
        return calendarnewDate.compareTo(calendarcurrent);
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || (isx86Port())
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static boolean isx86Port() {
        String kernelVersion = System.getProperty("os.version");
        return kernelVersion != null && kernelVersion.contains("x86");
    }

    public static void GetKeyHash(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "ytgdoc.com.vn.helpr",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static String Dialogbox(Context context, String title, String msg) {
        final String[] value = {""};
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setMessage(msg);
        alert.setTitle(title);
        alert.setView(edittext);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                value[0] = edittext.getText().toString();
                dialog.cancel();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
        return value[0];
    }

    public static void InitFolderImg(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.path_folder));
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            } else {
                Log.d(TAG, "InitFolderQuikQ: make dir QuikQ successful");
            }
        }
    }

    public static long getMiniSecordTime() {
        return System.currentTimeMillis() % 1000;
    }

    public static void CallCalTime(int thoigian_dung, final TextView title, CountDownTimer _countTimer, final String timer_voice) {
        if (_countTimer != null)
            _countTimer.onFinish();
        _countTimer = new CountDownTimer(thoigian_dung, 1000) {
            public void onTick(long millisUntilFinished) {
                title.setText("" + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                title.setText(timer_voice);
            }
        };
        _countTimer.start();
    }

    public static boolean saveFileToDevice(byte[] bytes, String dir, String fileName) {
        try {
            File file = new File(dir, fileName);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            //write the bytes in file
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes);
            // remember close de FileOutput
            fo.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
