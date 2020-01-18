package com.robohorse.gpversionchecker.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.robohorse.gpversionchecker.GPVersionChecker;
import com.robohorse.gpversionchecker.R;
import com.robohorse.gpversionchecker.domain.Version;

/**
 * Created by robohorse on 06.03.16.
 */
public class UIManager {

    public void showInfoView(final Activity activity, final Version version) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDialogOnUIThread(activity, version);
            }
        });
    }

    private void showDialogOnUIThread(final Context context, Version version) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.gpvch_layout_dialog, null);
        bindVersionData(view, version, context);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.gpvch_header)
                .setCancelable(!GPVersionChecker.forceUpdate)
                .setView(view)
                .setPositiveButton(R.string.gpvch_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.gpvch_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                        return GPVersionChecker.forceUpdate;
                    }
                })
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!GPVersionChecker.forceUpdate)
                                    dialog.dismiss();
                                UIManager.this.openGooglePlay(context);
                            }
                        });
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(GPVersionChecker.forceUpdate ? View.GONE : View.VISIBLE);
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void bindVersionData(View view, Version version, Context context) {
        final TextView tvVersion = view.findViewById(R.id.tvVersionCode);
        tvVersion.setText(context.getString(R.string.app_name) + ": " + version.getNewVersionCode());

        final TextView tvNews = view.findViewById(R.id.tvChanges);
        String lastChanges = version.getChanges();

        if (!TextUtils.isEmpty(lastChanges)) {
            tvNews.setText(lastChanges);

        } else {
            view.findViewById(R.id.lnChangesInfo).setVisibility(View.GONE);
        }
    }

    private void openGooglePlay(Context context) {
        final String packageName = context.getApplicationContext().getPackageName();
        final String url = context.getString(R.string.gpvch_google_play_url) + packageName;

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
