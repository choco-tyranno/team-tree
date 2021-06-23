package com.choco_tyranno.mycardtree.card_crud_feature.presentation.card_rv;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.choco_tyranno.mycardtree.card_crud_feature.Logger;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.NullPassUtil;

import java.util.Objects;
import java.util.Optional;

public class CardScrollListener extends RecyclerView.OnScrollListener {
    private OnFocusChangedListener focusChangedListener;
    private CardRecyclerView.ScrollingControlLayoutManager layoutManager;
    private int registeredPosition;
    private int containerPosition;
    private int centerX;
    private Runnable finalEvent;

    private int getCenterX() {
        return centerX;
    }

    private void setCenterX(int centerX) {
        this.centerX = centerX;
    }

//    public CardScrollListener(OnFocusChangedListener focusListener, OnScrollStateChangeListener stateListener) {
//        Logger.message("cardScrollListener#constructor");
//        this.focusChangedListener = focusListener;
//        this.scrollStateChangeListener = stateListener;
//        this.layoutManager = null;
//        this.finalEvent = null;
//        this.registeredPosition = RecyclerView.NO_POSITION;
//        this.containerPosition = -1;
//        this.centerX = -1;
//    }

    public CardScrollListener() {
        Logger.message("cardScrollListener#constructor");
        this.registeredPosition = RecyclerView.NO_POSITION;
        this.containerPosition = -1;
        this.centerX = -1;
    }

    public void initialize(CardRecyclerView.ScrollingControlLayoutManager layoutManager, OnFocusChangedListener focusChangedListener, int containerPosition){
        if (!hasLayoutManager()){
            setLayoutManager(layoutManager);
        }
        if (!hasFocusChangeListener()){
            setFocusChangedListener(focusChangedListener);
        }
        this.finalEvent = null;
        this.containerPosition = containerPosition;
    }
//
//    public CardScrollListener(OnFocusChangedListener focusListener) {
//        Logger.message("cardScrollListener#constructor");
//        this.focusChangedListener = focusListener;
//        this.layoutManager = null;
//        this.finalEvent = null;
//        this.registeredPosition = RecyclerView.NO_POSITION;
//        this.containerPosition = -1;
//        this.centerX = -1;
//    }

    public boolean hasFocusChangeListener(){
        return focusChangedListener != null;
    }

    public void setFocusChangedListener(OnFocusChangedListener focusChangedListener){
        this.focusChangedListener = focusChangedListener;
    }

    public boolean hasLayoutManager(){
        return layoutManager != null;
    }

    public void setLayoutManager(CardRecyclerView.ScrollingControlLayoutManager layoutManager) {
        Logger.message("cardScrollLsn#setLM");
        this.layoutManager = layoutManager;
    }

    public void setContainerPosition(int containerPosition) {
        Logger.message("cardScrollListener#setContainerPos : " + containerPosition);
        this.containerPosition = containerPosition;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                Optional.ofNullable(finalEvent).ifPresent(Runnable::run);
                finalEvent = null;
//                scrollStateChangeListener.onStateIdle(recyclerView, containerPosition);
//                this.dragStart = false;
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
//                if (!this.dragStart)
//                    scrollStateChangeListener.onStateDragStart(recyclerView, containerPosition);
//                dragStart = true;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (layoutManager == null) {
            Logger.message("cardScrollLsn#onScrolled : lm null");
            return;
        }
        if (containerPosition == -1) {
            Logger.message("cardScrollLsn#onScrolled : containerPos is -1");
            return;
        }
        if (getCenterX() == -1) {
            setCenterX(((Activity) recyclerView.getContext()).getWindowManager().getCurrentWindowMetrics().getBounds().centerX());
        }
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (firstVisibleItemPosition == -1) {
            return;
        }

        if (registeredPosition == -1) {
            registeredPosition = firstVisibleItemPosition;
        }

        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        if (firstVisibleItemPosition == lastVisibleItemPosition) {
            handelSingleItemVisible(recyclerView, firstVisibleItemPosition);
            return;
        }
        handleMultiItemVisible(recyclerView, firstVisibleItemPosition, lastVisibleItemPosition);
    }

    private synchronized void handelSingleItemVisible(RecyclerView recyclerView, int visiblePosition) {
        Logger.message("handleSingle/itemPos:" + visiblePosition + "/reg Pos:" + registeredPosition);
        if (visiblePosition == registeredPosition) {
            return;
        }
        if (visiblePosition > registeredPosition) {
            registeredPosition = visiblePosition;
            finalEvent = () -> focusChangedListener.onNextFocused(recyclerView, containerPosition, visiblePosition);
            return;
        }
        registeredPosition = visiblePosition;
        finalEvent = () -> focusChangedListener.onPreviousFocused(recyclerView, containerPosition, visiblePosition);
    }

    private synchronized void handleMultiItemVisible(RecyclerView recyclerView, int firstVisibleItemPosition, int lastVisibleItemPosition) {
        Logger.message("handleMulti/f itemPos:" + firstVisibleItemPosition + "/l itemPos :" + lastVisibleItemPosition + "/reg Pos:" + registeredPosition);
        float lastVisibleItemX = Objects.requireNonNull(layoutManager.getChildAt(1)).getX();

        if (firstVisibleItemPosition == registeredPosition) {
            if (lastVisibleItemX > centerX) {
                return;
            }
            registeredPosition = lastVisibleItemPosition;
            finalEvent = () -> focusChangedListener.onNextFocused(recyclerView, containerPosition, lastVisibleItemPosition);
            return;
        }

        if (lastVisibleItemPosition == registeredPosition) {
            if (lastVisibleItemX <= centerX) {
                return;
            }
            registeredPosition = firstVisibleItemPosition;
            finalEvent = () -> focusChangedListener.onPreviousFocused(recyclerView, containerPosition, firstVisibleItemPosition);
            return;
        }

        if (firstVisibleItemPosition < registeredPosition && lastVisibleItemPosition < registeredPosition) {
            if (lastVisibleItemX <= centerX) {
                registeredPosition = lastVisibleItemPosition;
                finalEvent = () -> focusChangedListener.onPreviousFocused(recyclerView, containerPosition, lastVisibleItemPosition);
            } else {
                registeredPosition = firstVisibleItemPosition;
                finalEvent = () -> focusChangedListener.onPreviousFocused(recyclerView, containerPosition, firstVisibleItemPosition);
            }
            return;
        }

        if (firstVisibleItemPosition > registeredPosition && lastVisibleItemPosition > registeredPosition) {
            if (lastVisibleItemX <= centerX) {
                registeredPosition = lastVisibleItemPosition;
                finalEvent = () -> focusChangedListener.onNextFocused(recyclerView, containerPosition, lastVisibleItemPosition);
            } else {
                registeredPosition = firstVisibleItemPosition;
                finalEvent = () -> focusChangedListener.onNextFocused(recyclerView, containerPosition, firstVisibleItemPosition);
            }
        }
    }

    public interface OnFocusChangedListener {
        void onNextFocused(RecyclerView view, int containerPosition, int cardPosition);
        void onPreviousFocused(RecyclerView view, int containerPosition, int cardPosition);
    }

//    public interface OnScrollStateChangeListener {
//        void onStateIdle(RecyclerView view, int containerPosition);
//
//        void onStateDragStart(RecyclerView view, int containerPosition);
//    }
}
