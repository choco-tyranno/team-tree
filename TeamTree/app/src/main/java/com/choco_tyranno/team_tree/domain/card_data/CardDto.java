package com.choco_tyranno.team_tree.domain.card_data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.choco_tyranno.team_tree.BR;
import com.choco_tyranno.team_tree.presentation.card_rv.ContactCardViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardDto extends BaseObservable implements Comparable<CardDto>, Serializable {
    public static final int NO_ROOT_CARD = 0;

    private int mCardNo;

    private int mSeqNo;

    private int mContainerNo;

    private int mRootNo;

    private int mType;

    private String mTitle;

    private String mSubtitle;

    private String mContactNumber;

    private String mFreeNote;

    private String mImagePath;

    CardDto(CardDto origin) {
        this.mCardNo = origin.getCardNo();
        this.mSeqNo = origin.getSeqNo();
        this.mContainerNo = origin.getContainerNo();
        this.mRootNo = origin.getRootNo();
        this.mType = origin.getType();
        this.mTitle = origin.getTitle();
        this.mSubtitle = origin.getSubtitle();
        this.mContactNumber = origin.getContactNumber();
        this.mFreeNote = origin.getFreeNote();
        this.mImagePath = origin.getImagePath();
    }

    public static void orderByContainerNoDesc(List<CardDto> list){
        list.sort((o1,o2)->Integer.compare(o2.getContainerNo(), o1.getContainerNo()));
    }

    public static void orderByContainerNoAsc(List<CardDto> list){
        list.sort((o1,o2)->Integer.compare(o1.getContainerNo(), o2.getContainerNo()));
    }

    public static List<CardDto> cloneList(List<CardDto> origin){
        List<CardDto> result = new ArrayList<>();
        for (CardDto cardDTO : origin){
            result.add(cardDTO.cloneInstance());
        }
        return result;
    }

    public CardDto cloneInstance(){
        return new CardDto(this);
    }

    CardDto(Builder builder) {
        this.mCardNo = builder.mCardNo;
        this.mSeqNo = builder.mSeqNo;
        this.mContainerNo = builder.mContainerNo;
        this.mRootNo = builder.mRootNo;
        this.mType = builder.mType;
        this.mTitle = builder.mTitle;
        this.mSubtitle = builder.mSubtitle;
        this.mContactNumber = builder.mContactNumber;
        this.mFreeNote = builder.mFreeNote;
        this.mImagePath = builder.mImagePath;
    }

    public CardEntity toEntity() {
        return new CardEntity.Builder().dtoToEntity(this).build();
    }

    public static CardDto entityToDTO(CardEntity entity) {
        return new CardDto.Builder().entityToDTO(entity).build();
    }

    @Override
    public int compareTo(CardDto compareTarget) {
        return Integer.compare(this.getSeqNo(), compareTarget.getSeqNo());
    }

    public static class Builder {
        private int mCardNo;
        private int mSeqNo;
        private int mContainerNo;
        private int mRootNo;
        private int mType;
        private String mTitle;
        private String mSubtitle;
        private String mContactNumber;
        private String mFreeNote;
        private String mImagePath;

        public Builder() {
            init();
        }

        private void init() {
            this.mCardNo = 0;
            this.mSeqNo = 0;
            this.mContainerNo = 0;
            this.mRootNo = 0;
            this.mType = ContactCardViewHolder.CONTACT_CARD_TYPE;
            this.mTitle = "";
            this.mSubtitle = "";
            this.mContactNumber = "";
            this.mFreeNote = "";
            this.mImagePath = "";
        }

        public Builder entityToDTO(CardEntity entity) {
            this.mCardNo = entity.getCardNo();
            this.mSeqNo = entity.getSeqNo();
            this.mContainerNo = entity.getContainerNo();
            this.mRootNo = entity.getRootNo();
            this.mType = entity.getType();
            this.mTitle = entity.getTitle();
            this.mSubtitle = entity.getSubtitle();
            this.mContactNumber = entity.getContactNumber();
            this.mFreeNote = entity.getFreeNote();
            this.mImagePath = entity.getImagePath();
            return this;
        }

        public Builder cardNo(int cardNo) {
            this.mCardNo = cardNo;
            return this;
        }

        public Builder seqNo(int seqNo) {
            this.mSeqNo = seqNo;
            return this;
        }

        public Builder containerNo(int containerNo) {
            this.mContainerNo = containerNo;
            return this;
        }

        public Builder rootNo(int rootNo) {
            this.mRootNo = rootNo;
            return this;
        }

        public Builder type(int type) {
            this.mType = type;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.mSubtitle = subtitle;
            return this;
        }

        public Builder contactNumber(String contactNumber) {
            this.mContactNumber = contactNumber;
            return this;
        }

        public Builder freeNote(String freeNote) {
            this.mFreeNote = freeNote;
            return this;
        }

        public Builder imagePath(String imagePath) {
            this.mImagePath = imagePath;
            return this;
        }

        public CardDto build() {
            return new CardDto(this);
        }
    }

    @Bindable
    public int getCardNo() {
        return mCardNo;
    }

    public void setCardNo(int cardNo) {
        this.mCardNo = cardNo;
        notifyPropertyChanged(BR.cardNo);
    }

    @Bindable
    public int getSeqNo() {
        return mSeqNo;
    }

    public void setSeqNo(int seqNo) {
        this.mSeqNo = seqNo;
        notifyPropertyChanged(BR.seqNo);
    }

    @Bindable
    public int getContainerNo() {
        return mContainerNo;
    }

    public void setContainerNo(int containerNo) {
        this.mContainerNo = containerNo;
        notifyPropertyChanged(BR.containerNo);
    }

    @Bindable
    public int getRootNo() {
        return mRootNo;
    }

    public void setRootNo(int rootNo) {
        this.mRootNo = rootNo;
        notifyPropertyChanged(BR.rootNo);
    }

    @Bindable
    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subTitle) {
        this.mSubtitle = subTitle;
        notifyPropertyChanged(BR.subtitle);
    }

    @Bindable
    public String getContactNumber() {
        return mContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.mContactNumber = contactNumber;
        notifyPropertyChanged(BR.contactNumber);
    }

    @Bindable
    public String getFreeNote() {
        return mFreeNote;
    }

    public void setFreeNote(String freeNote) {
        this.mFreeNote = freeNote;
        notifyPropertyChanged(BR.freeNote);
    }

    @Bindable
    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        this.mImagePath = imagePath;
        notifyPropertyChanged(BR.imagePath);
    }
}
