package com.vmrits.android.app;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ImageLoadDialog {
    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialoge_image_load);
        RadioGroup radioGroup = dialog.findViewById(R.id.id_custom_dialog_image_load_RG);
        RadioButton radioButton_Camera = dialog.findViewById(R.id.id_custom_dialog_image_load_camera_RB);
        RadioButton radioButton_Gallery = dialog.findViewById(R.id.id_custom_dialog_image_load_gallery_RB);

        Button dialogButton = (Button) dialog.findViewById(R.id.id_custom_dialog_image_load_cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}

