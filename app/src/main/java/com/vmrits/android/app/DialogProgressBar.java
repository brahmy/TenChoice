package com.vmrits.android.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class DialogProgressBar {
    private Dialog dialog;
    private Context context;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();


    public DialogProgressBar(Context context) {
        this.context = context;

    }

    public void dialogInit() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_progressbar);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar = dialog.findViewById(R.id.id_Progress_bar);
        dialog.setCancelable(false);
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    // Update the progress status
                    progressStatus += 1;

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            // If task execution completed
                            if (progressStatus == 100) {
                                // Set a message of completion
                            }
                        }
                    });
                }
            }
        }).start(); // Start the operation


    }

    public void hideDialog() {
        dialog.dismiss();

    }

    public void showDialog() {
        dialog.show();
    }
}
