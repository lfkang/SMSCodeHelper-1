package me.gitai.smscodehelper.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.bean.MSG;
import me.gitai.smscodehelper.receiver.SMSBroadcastReceiver;

/**
 * Created by gitai on 16-01-28.
 */
public class TestPreference extends EditPreference implements Preference.OnPreferenceClickListener {
    protected MaterialDialog licenseDialog;
    protected Context ctx;
    protected int title;
    protected EditText et_address, et_body;

    public TestPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TestPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestPreference(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void init(Context context){
        ctx = context;
        licenseDialog = new MaterialDialog(context)
                .setTitle(getTitle())
                .setCanceledOnTouchOutside(true)
                .setContentView(R.layout.layout_test, new MaterialDialog.OnViewInflateListener() {
                    @Override
                    public boolean onInflate(View v) {
                        et_address = (EditText)v.findViewById(R.id.address);
                        et_body = (EditText)v.findViewById(R.id.body);
                        et_body.setText(def_text);
                        return false;
                    }
                })
                .setPositiveButton(R.string.prefs_test, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(View v, View MaterialDialog) {
                        String address = et_address.getText().toString();
                        String body = et_body.getText().toString();

                        if (StringUtils.isEmpty(address) || StringUtils.isEmpty(body)){
                            ToastUtil.show("Address or Body is empty!");
                            return false;
                        }

                        if (!new SMSBroadcastReceiver().parse(ctx, new MSG(address, body))){
                            ToastUtil.show("_(:з」∠)_");
                        }

                        return false;
                    }
                });

        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        licenseDialog.show();
        return false;
    }

    public void share(String text){
        if (!StringUtils.isEmpty(text)){
            et_body.setText(text);
            licenseDialog.show();
        }
    }
}
