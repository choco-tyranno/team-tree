package com.choco_tyranno.team_tree.presentation.card_rv;

import android.os.Parcelable;
import android.util.Pair;

import com.choco_tyranno.team_tree.domain.card_data.CardDto;

import java.util.List;

public class DragMoveDataContainer {
    public static final String DRAG_TYPE = "DRAG_MOVE";
    private CardDto rootCard;
    private List<CardDto> movingCardList;
    private List<CardDto> pastLocationNextCardList;
    private List<Pair<Integer, Parcelable>> pastOnFocusPositionAndScrollStatePairList;

    public DragMoveDataContainer(){
    }

    public void setRootCard(CardDto rootCard){
        this.rootCard = rootCard;
    }

    public void setMovingCardList(List<CardDto> movingCardList) {
        this.movingCardList = movingCardList;

    }

    public void setPastLocationNextCardList(List<CardDto> pastLocationNextCardList) {
        this.pastLocationNextCardList = pastLocationNextCardList;

    }

    public void setPastOnFocusPositionAndScrollStatePairList(List<Pair<Integer, Parcelable>> onFocusPositionAndScrollStatePairList) {
        this.pastOnFocusPositionAndScrollStatePairList = onFocusPositionAndScrollStatePairList;
    }

    public String getDragType(){
        return DRAG_TYPE;
    }

    public CardDto getRootCard() {
        return rootCard;
    }

    public List<CardDto> getMovingCardList() {
        return movingCardList;
    }

    public List<CardDto> getPastLocationNextCardList() {
        return pastLocationNextCardList;
    }

    public List<Pair<Integer, Parcelable>> getPastOnFocusPositionAndScrollStatePairList() {
        return pastOnFocusPositionAndScrollStatePairList;
    }
}
