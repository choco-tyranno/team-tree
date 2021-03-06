package com.choco_tyranno.team_tree.presentation.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.choco_tyranno.team_tree.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class RemoveSwitch extends SwitchMaterial implements DependentView{

    public RemoveSwitch(@NonNull Context context) {
        super(context);
        ready();
    }

    public RemoveSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ready();
    }

    public RemoveSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ready();
    }

    public void ready() {
        if (ready.get())
            return;
        RemoveSwitch view = this;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                view.ready.set(true);
                if (!attributeSettingActions.isEmpty()) {
                    while (!attributeSettingActions.isEmpty()) {
                        Runnable action = attributeSettingActions.poll();
                        Optional.ofNullable(action).ifPresent(Runnable::run);
                    }
                }
            }
        });
    }

    //Promise the param(View baseView) view layout is ready.
    public void setScaleByTopAppBar(@NonNull View topAppBar) {
        Runnable action = () -> {
            final float switchRatioToTopAppBar = Float.parseFloat(this.getContext().getResources().getString(R.string.mainBody_removeSwitchRatioToTopAppBar));
            final int switchHeightPx = this.getHeight();
            final int topAppBarHeightPx = topAppBar.getHeight();
            float multiplyingValue = 1.0f;
            float myScale = switchRatioToTopAppBar * topAppBarHeightPx / switchHeightPx;
            if (myScale > 1){
                multiplyingValue = myScale;
            }
            this.setScaleX(multiplyingValue);
            this.setScaleY(multiplyingValue);
        };

        postAttributeSettingAction(action);
    }

}
