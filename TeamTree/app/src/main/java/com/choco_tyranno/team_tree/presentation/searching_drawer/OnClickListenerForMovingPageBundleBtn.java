package com.choco_tyranno.team_tree.presentation.searching_drawer;

import android.text.TextUtils;
import android.view.View;

import com.choco_tyranno.team_tree.R;
import com.choco_tyranno.team_tree.presentation.CardViewModel;
import com.choco_tyranno.team_tree.presentation.MainCardActivity;

public class OnClickListenerForMovingPageBundleBtn implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        CardViewModel viewModel = ((MainCardActivity) v.getContext()).getCardViewModel();
        if (v.getId()== R.id.prev_page_btn)
            onPrevBtnClicked(viewModel);
        if (v.getId()== R.id.next_page_btn)
            onNextBtnClicked(viewModel);
    }

    private void onPrevBtnClicked(CardViewModel viewModel) {
        viewModel.preparePrevPagers();
    }

    private void onNextBtnClicked(CardViewModel viewModel) {
        viewModel.prepareNextPagers();
    }
}
