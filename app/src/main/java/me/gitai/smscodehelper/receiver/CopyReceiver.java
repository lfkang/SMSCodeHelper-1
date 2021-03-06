package me.gitai.smscodehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.gitai.library.utils.ToastUtil;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.R;
import me.gitai.library.utils.StringUtils;

/**
 * Created by gitai on 15-12-30.
 */
public class CopyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null){
            String code = b.getString(Constant.KEY_BUNDLE_SMS_CODE);

            if (StringUtils.isEmpty(code)){
                return;
            }

            // 复制到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(Constant.KEY_CLIP_LABEL, code);
            clipboardManager.setPrimaryClip(clipData);

            // toast
            ToastUtil.showId(R.string.format_copy_success,code);
        }
    }
}
