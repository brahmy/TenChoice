package com.vmrits.android.app;

import android.view.View;

public interface RVClickListener {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);

}
