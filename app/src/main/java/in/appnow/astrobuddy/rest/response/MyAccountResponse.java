package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 15:40, 23/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyAccountResponse extends BaseResponseModel {

    @SerializedName("response")
    private AccountDetails accountDetails;

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public class AccountDetails implements Parcelable{

        @SerializedName("topics")
        private int userTopics;


        protected AccountDetails(Parcel in) {
            userTopics = in.readInt();
        }

        public final Creator<AccountDetails> CREATOR = new Creator<AccountDetails>() {
            @Override
            public AccountDetails createFromParcel(Parcel in) {
                return new AccountDetails(in);
            }

            @Override
            public AccountDetails[] newArray(int size) {
                return new AccountDetails[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        public int getUserTopics() {
            return userTopics;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(userTopics);
        }

        @Override
        public String toString() {
            return "AccountDetails{" +
                    "userTopics=" + userTopics +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MyAccountResponse{" +
                "accountDetails=" + accountDetails +
                '}';
    }
}
