package com.choco_tyranno.team_tree.presentation.detail_page;

import android.util.Log;
import android.view.View;

import com.choco_tyranno.team_tree.databinding.ActivityDetailBinding;

public class OnClickListenerForUtilContainerFab implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        DetailCardActivity detailCardActivity = (DetailCardActivity) v.getContext();
        ActivityDetailBinding binding = detailCardActivity.getBinding();
        detailCardActivity.getDetailFab().animateFab(binding);
    }

}
