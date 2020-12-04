package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:18, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ApplyPromoCodeResponse extends BaseResponseModel implements Parcelable{
    @SerializedName("promo_applied")
    private boolean isPromoApplied;
    @SerializedName("topics")
    private String topicsCount;
    @SerializedName("promo_topics")
    private String promoTopicsCount;
    @SerializedName("amount")
    private String amount;
    @SerializedName("promo_amount")
    private String promoAmount;
    @SerializedName("promo_label")
    private String promoLabel;
    @SerializedName("promo_id")
    private String promoId;

    public ApplyPromoCodeResponse() {
    }

    protected ApplyPromoCodeResponse(Parcel in) {
        isPromoApplied = in.readByte() != 0;
        topicsCount = in.readString();
        promoTopicsCount = in.readString();
        amount = in.readString();
        promoAmount = in.readString();
        promoLabel = in.readString();
        promoId = in.readString();
    }

    public static final Creator<ApplyPromoCodeResponse> CREATOR = new Creator<ApplyPromoCodeResponse>() {
        @Override
        public ApplyPromoCodeResponse createFromParcel(Parcel in) {
            return new ApplyPromoCodeResponse(in);
        }

        @Override
        public ApplyPromoCodeResponse[] newArray(int size) {
            return new ApplyPromoCodeResponse[size];
        }
    };

    public boolean isPromoApplied() {
        return isPromoApplied;
    }

    public String getTopicsCount() {
        return topicsCount;
    }

    public String getPromoTopicsCount() {
        return promoTopicsCount;
    }

    public String getAmount() {
        return amount;
    }

    public String getPromoAmount() {
        return promoAmount;
    }

    public String getPromoLabel() {
        return promoLabel;
    }

    public String getPromoId() {
        return promoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isPromoApplied ? 1 : 0));
        parcel.writeString(topicsCount);
        parcel.writeString(promoTopicsCount);
        parcel.writeString(amount);
        parcel.writeString(promoAmount);
        parcel.writeString(promoLabel);
        parcel.writeString(promoId);
    }
}
