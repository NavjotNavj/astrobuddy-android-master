package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 16:51, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AccountHelpResponse extends BaseResponseModel {
    @SerializedName("myacc_help_info")
    private String accountHelpInfo;

    public String getAccountHelpInfo() {
        return accountHelpInfo;
    }
}
