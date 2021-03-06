package me.gitai.smscodehelper.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.gitai.library.utils.L;
import me.gitai.library.utils.PackageUtils;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.widget.TestPreference;


/**
 * Created by gitai on 15-12-12.
 */
public class MainPreferences extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d();

        addPreferencesFromResource(R.xml.preferences);

        getListView().setPadding(0,0,0,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            setFitsSystemWindows(true);
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        if (getActionBar() != null){
            getActionBar().setDisplayShowHomeEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission(Manifest.permission.RECEIVE_SMS);
        }

        if (getIntent()!=null && Intent.ACTION_SEND.equals(getIntent().getAction())){
            ((TestPreference)findPreference(Constant.KEY_GENERAL_TEST))
                    .share(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    @Override
    protected void onDestroy() {
        L.d();
        if (SharedPreferencesUtil.getInstence(null).getBoolean(Constant.KEY_GENERAL_HIDDEN_ICON, false)){
            PackageUtils.disableComponent(this, MainPreferences.class);
        }else {
            PackageUtils.enableComponent(this, MainPreferences.class);
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    MaterialDialog materialDialog = new MaterialDialog(this)
                            .setTitle("OAQ")
                            .setMessage(R.string.m_denied)
                            .setPositiveButton(R.string.m_exit, new MaterialDialog.OnClickListener() {
                                @Override
                                public boolean onClick(View v, View MaterialDialog) {
                                    finish();
                                    return false;
                                }
                            });
                    materialDialog.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(14)
    private void setFitsSystemWindows(boolean on) {
        ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(on);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getPermission(final String permission)
    {
        final Activity _this = this;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            MaterialDialog materialDialog = new MaterialDialog(this);
            materialDialog.setMessage(R.string.m_content);
            materialDialog.setPositiveButton(R.string.m_get, new MaterialDialog.OnClickListener() {
                @Override
                public boolean onClick(View v, View MaterialDialog) {
                    ActivityCompat.requestPermissions(_this, new String[]{permission}, 0);
                    return true;
                }
            });
            materialDialog.show();
        }
    }
}
