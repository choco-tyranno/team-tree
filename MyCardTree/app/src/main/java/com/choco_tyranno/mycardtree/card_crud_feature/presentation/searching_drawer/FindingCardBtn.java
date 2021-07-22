package com.choco_tyranno.mycardtree.card_crud_feature.presentation.searching_drawer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.choco_tyranno.mycardtree.R;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.CardViewModel;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.MainCardActivity;
import com.choco_tyranno.mycardtree.databinding.ActivityMainFrameBinding;

public class FindingCardBtn {
    private boolean sendingFindCardReq = false;
    Animation flyingToRightAnimation;

    public FindingCardBtn(Context context) {
        flyingToRightAnimation = AnimationUtils.loadAnimation(context, R.anim.search_page_sending_card);
        flyingToRightAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sendingFindCardReq = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MainCardActivity mainCardActivity = (MainCardActivity) context;
                ActivityMainFrameBinding binding = mainCardActivity.getMainBinding();
                DrawerLayout MainDL = binding.mainDrawerLayout;
                MainDL.closeDrawer(GravityCompat.END);
                SearchView searchView = binding.rightDrawer.cardSearchView;
                searchView.setQuery("",false);
                searchView.setIconified(true);
                sendingFindCardReq = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void animate(View view){
        view.startAnimation(flyingToRightAnimation);
    }

    public boolean isSendingFindCardReq(){
        return sendingFindCardReq;
    }

}
