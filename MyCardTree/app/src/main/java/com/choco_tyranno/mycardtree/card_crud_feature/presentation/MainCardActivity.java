package com.choco_tyranno.mycardtree.card_crud_feature.presentation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.choco_tyranno.mycardtree.R;
import com.choco_tyranno.mycardtree.card_crud_feature.Logger;
import com.choco_tyranno.mycardtree.card_crud_feature.domain.card_data.CardDTO;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.card_rv.CardGestureListener;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.card_rv.CardViewShadowProvider;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.card_rv.ContactCardViewHolder;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.card_rv.ImageToFullScreenClickListener;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.container_rv.CardContainerViewHolder;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.container_rv.ContainerAdapter;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.container_rv.ContainerRecyclerView;
import com.choco_tyranno.mycardtree.card_crud_feature.presentation.searching_drawer.FindingCardBtn;
import com.choco_tyranno.mycardtree.databinding.ActivityMainFrameBinding;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MainCardActivity extends AppCompatActivity {
    private CardViewModel viewModel;
    private ActivityMainFrameBinding binding;
    private Handler mMainHandler;
    private FindingCardBtn findingCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Optional.ofNullable(mMainHandler).isPresent())
            mMainHandler = new Handler(getMainLooper());
        viewModel = new ViewModelProvider(MainCardActivity.this).get(CardViewModel.class);
        loadDefaultCardImage();
        mainBinding();
        binding.setViewModel(viewModel);
        setContainerRv();
        setSearchingResultRv();

        findingCardBtn = new FindingCardBtn(this);
        // worker thread's job available.
        CardGestureListener cardGestureListener = new CardGestureListener();
        GestureDetectorCompat cardGestureDetector = new GestureDetectorCompat(MainCardActivity.this, cardGestureListener);
        viewModel.setCardGestureListener(cardGestureListener);
        viewModel.setCardGestureDetector(cardGestureDetector);
        viewModel.connectGestureUtilsToOnCardTouchListener();

        viewModel.loadData(() -> {
            waitDefaultCardImageLoading(getMainHandler());
            loadPictureCardImages(viewModel.getPictureCardArr(), getMainHandler());
        });
        binding.mainScreen.appNameFab.setOnClickListener((view) -> {
//            ContainerRecyclerView containerRecyclerView = binding.mainScreen.mainBody.containerRecyclerview;
//            CardContainerViewHolder containerViewHolder = (CardContainerViewHolder)containerRecyclerView.findViewHolderForAdapterPosition(1);
//            ContactCardViewHolder cardViewHolder = (ContactCardViewHolder)containerViewHolder.getBinding().cardRecyclerview.findViewHolderForAdapterPosition(0);
//            ConstraintLayout backLayout = cardViewHolder.getBinding().cardBackLayout.backCardConstraintLayout;
//            ConstraintLayout frontLayout = cardViewHolder.getBinding().cardFrontLayout.frontCardConstraintLayout;
//            viewModel.printTargetCardState(0, 0);
//            viewModel.printTargetCardDto(0, 0);
//            viewModel.printContainers();
//            viewModel.printAllData();
        });
    }

    public FindingCardBtn getFindCardBtn() {
        return findingCardBtn;
    }

    private void setSearchingResultRv() {
        binding.rightDrawer.cardSearchResultRecyclerview
                .addItemDecoration(new DividerItemDecoration(MainCardActivity.this, DividerItemDecoration.VERTICAL));
    }


    public void loadPictureCardImages(CardDTO[] allCardArr, Handler handler) {
        for (CardDTO theCardDto : allCardArr) {
            if (TextUtils.equals(theCardDto.getImagePath(), ""))
                continue;
            handler.postDelayed(() -> {
                final int cardNo = theCardDto.getCardNo();
                try {
                    int width = Math.round(getResources().getDimension(R.dimen.card_thumbnail_image_width));
                    int height = Math.round(getResources().getDimension(R.dimen.card_thumbnail_image_height));
                    Glide.with(MainCardActivity.this).asBitmap()
                            .load(theCardDto.getImagePath()).addListener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            viewModel.setPictureCardImage(resource, cardNo);
                            return false;
                        }
                    }).submit(width, height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        }
    }

    public void waitDefaultCardImageLoading(Handler handler) {
        handler.postDelayed(() -> {
            if (viewModel.hasDefaultCardImage()) {
                showContainerCardUi();
            } else {
                waitDefaultCardImageLoading(handler);
            }
        }, 1000);
    }

    public void showContainerCardUi() {
        runOnUiThread(() -> Objects.requireNonNull(binding.mainScreen.mainBody.containerRecyclerview.getAdapter())
                .notifyDataSetChanged());
    }

    private void loadDefaultCardImage() {
        new Thread(() -> {
            try {
                int width = Math.round(getResources().getDimension(R.dimen.card_thumbnail_image_width));
                int height = Math.round(getResources().getDimension(R.dimen.card_thumbnail_image_height));
                Glide.with(MainCardActivity.this).asBitmap()
                        .load(R.drawable.default_card_image_01).addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        viewModel.setDefaultCardImage(resource);
                        return false;
                    }
                }).submit(width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void mainBinding() {
        binding = ActivityMainFrameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
    }

    private void setContainerRv() {
        ContainerRecyclerView rv = binding.mainScreen.mainBody.containerRecyclerview;
        rv.setAdapter(new ContainerAdapter(this));
        rv.setLayoutManager(new ContainerRecyclerView.ItemScrollingControlLayoutManager(MainCardActivity.this, LinearLayoutManager.VERTICAL, false));
        Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
    }

    public ActivityMainFrameBinding getMainBinding() {
        return binding;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.message("onStop");
        SingleToastManager.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.message("destroyed");
        CardViewShadowProvider.onDestroy();
        SingleToastManager.clear();
    }

    public CardViewModel getCardViewModel() {
        return viewModel;
    }

    public Handler getMainHandler() {
        return mMainHandler;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageToFullScreenClickListener.REQ_MANAGE_DETAIL) {
            if (data == null)
                return;
            CardDTO updatedCardDto = (CardDTO) data.getSerializableExtra("post_card");
            boolean imageChanged = viewModel.applyCardFromDetailActivity(updatedCardDto);
            if (imageChanged) {
                CardDTO[] cardDTOArr = {updatedCardDto};
                loadPictureCardImages(cardDTOArr, mMainHandler);
            }
        }
    }

    public void scrollToFindingTargetCard(Pair<Integer, Integer[]> scrollUtilDataForFindingOutCard) {
        final int startContainerPosition = scrollUtilDataForFindingOutCard.first;
        final Integer[] scrollTargetCardSeqArr = scrollUtilDataForFindingOutCard.second;

        String seqText = "";
        for (int a : scrollTargetCardSeqArr){
            seqText = seqText.concat("/"+a);
        }

        RecyclerView containerRecyclerview = binding.mainScreen.mainBody.containerRecyclerview;
        Queue<Runnable> scrollAction = new LinkedList<>();
        int s = 0;
        for (int i = startContainerPosition; i < startContainerPosition + scrollTargetCardSeqArr.length; i++) {
            final int s1 = s;
            final int i1 = i;
            scrollAction.offer(()->{
                containerRecyclerview.smoothScrollToPosition(i1);
                Runnable delayedAction = ()->{
                    CardContainerViewHolder containerViewHolder = (CardContainerViewHolder) containerRecyclerview.findViewHolderForAdapterPosition(i1);
                    RecyclerView cardRecyclerview = containerViewHolder.getBinding().cardRecyclerview;
                    cardRecyclerview.smoothScrollToPosition(scrollTargetCardSeqArr[s1]);
                };
                mMainHandler.postDelayed(delayedAction, 900);
            });
            s++;
        }
        scrollActionDelayed(scrollAction);
    }

    private void scrollActionDelayed(Queue<Runnable> scrollActionQueue){
        mMainHandler.postDelayed(()->{
            if (scrollActionQueue.isEmpty())
                return;
            scrollActionQueue.poll().run();
            scrollActionDelayed(scrollActionQueue);
        },900);
    }
}