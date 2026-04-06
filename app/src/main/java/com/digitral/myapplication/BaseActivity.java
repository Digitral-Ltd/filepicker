package com.digitral.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.digitral.filepicker.FilePicker;
import com.digitral.filepicker.FilePickerImpl;
import com.digitral.filepicker.FilePickerLaunchers;
import com.digitral.filepicker.callback.FilePickerCallback;
import com.digitral.filepicker.controls.ActivityResultHandler;
import com.digitral.filepicker.utils.TraceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public FilePicker mFilePicker;

    private String mLanguageId = "1";

    public boolean isResumed;

    public ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;

    public ActivityResultHandler<String[], Map<String, Boolean>> permissionLauncher;

    public ActivityResultHandler<Intent, ActivityResult> activityLauncher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFilePicker(null);

        permissionLauncher = ActivityResultHandler.registerPermissionForResult(this);

        activityLauncher = ActivityResultHandler.registerActivityForResult(this);

        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                TraceUtils.logE("Ooredoo", "In pickMedia::");
                if (mFilePicker != null) {
                    mFilePicker.onGalleryFileSelected(-1, new Intent(), uri);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFilePicker != null) {
            mFilePicker.onActivityResult(requestCode, resultCode, data);
        }
    }

    void initFilePicker(FilePickerCallback callback) {
        //clearFilePicker();
        mFilePicker = FilePickerImpl.newInstance(this, callback, this);
        /*mFilePicker.setActivityLauncher(FilePickerLaunchers.fromIntentHandler(activityLauncher));
        mFilePicker.setPermissionLauncher(FilePickerLaunchers.fromPermissionHandler(permissionLauncher ));
        mFilePicker.setPickMediaLauncher(pickMediaLauncher);*/
        //mFilePicker.setActivityResultCaller(this)
        mFilePicker.cropEnabled(true).compressEnabled(false).waterMarkEnabled(false);
    }

    void clearFilePicker() {
        mFilePicker = null;
    }

    public int checkPermission(String permissions, int code) {

        if (ContextCompat.checkSelfPermission(this, permissions) == PackageManager.PERMISSION_GRANTED) {
            return 1;
        }

        if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{permissions}, code);

            return 0;

        }
        return -1;
    }


    public String getOnlyDateFromMillis(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
    }

    public String getCurrentOnlyDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

   /* public void initFilePicker(FilePickerCallback callback) {
        clearFilePicker();
        mFilePicker = FilePickerImpl.newInstance(this, callback, this);
        //mFilePicker.setActivityLauncher(FilePickerLaunchers.fromIntentHandler(activityLauncher));
        //mFilePicker.setPermissionLauncher(FilePickerLaunchers.fromPermissionHandler(permissionLauncher));
        //mFilePicker.setPickMediaLauncher(pickMediaLauncher);
        mFilePicker.cropEnabled(true).compressEnabled(false).waterMarkEnabled(false);
    }*/

   /* public void clearFilePicker() {
        mFilePicker = null;
    }

    public void launchGalleryNew(int requestCode, Object returnObject, FilePickerCallback callback) {

        System.gc();

        initFilePicker(callback);

        if (mFilePicker != null) {
            mFilePicker.launchGallery(requestCode, returnObject);
        }

    }

    public void launchGalleryNew(int requestCode, Object returnObject, FilePickerCallback callback, boolean cropEnabled, boolean compressEnabled) {

        System.gc();

        initFilePicker(callback);
        mFilePicker.compressEnabled(compressEnabled);
        if (compressEnabled) {
            mFilePicker.compressSize(80);
        }
        mFilePicker.cropEnabled(cropEnabled);
        mFilePicker.launchGallery(requestCode, returnObject);

    }

    public void launchCameraNew(int requestCode, Object returnObject, FilePickerCallback callback) {

        System.gc();

        initFilePicker(callback);

        mFilePicker.launchCamera(requestCode, returnObject);

    }

    public void launchCameraNew(int requestCode, Object returnObject, FilePickerCallback callback, boolean cropEnabled, boolean compressEnabled) {

        System.gc();

        initFilePicker(callback);
        mFilePicker.compressEnabled(compressEnabled);
        if (compressEnabled) {
            mFilePicker.compressSize(80);
        }
        mFilePicker.cropEnabled(cropEnabled);
        mFilePicker.launchCamera(requestCode, returnObject);

    }*/

    public String getLCode() {
        return mLanguageId;
    }

    public void showKeypad(EditText editText) {
        try {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
            TraceUtils.logException(e);
        }
    }

    public boolean allPermissionGranted(Map<String, Boolean> result) {
        boolean isAllAllowed = true;
        for (Map.Entry<String, Boolean> entry : result.entrySet()) {
            if (!entry.getValue()) {
                return false;
            }

        }
        return isAllAllowed;
    }

    public int getSizeByViewport(int mSize) {
        try {


            double width = getResources().getDisplayMetrics().widthPixels;
            double mDensity = getResources().getDisplayMetrics().density;

            double viewPortWidth = width / mDensity;

            double mWidth = (viewPortWidth * ((double) (mSize / 375f) * 100) / 100) * mDensity;

            return (int) mWidth;

        } catch (Exception e) {

            TraceUtils.logException(e);

        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        //No Impl
    }


    public int getDeviceHeight() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public int getDeviceWidth() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public void composeSMS(String toNumber) {
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri data = Uri.parse("sms:" + toNumber);
        intent.setData(data);
        startActivity(intent);
    }


    public void changeLanguage(String languageToLoad) {

        try {

            String localeString = "EN";

            if (languageToLoad.equals("2")) localeString = "MY";

            Locale locale = new Locale(localeString);

            Locale.setDefault(locale);

            Configuration config = new Configuration();

            config.locale = locale;

            getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        } catch (Exception e) {

            TraceUtils.logException(e);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
