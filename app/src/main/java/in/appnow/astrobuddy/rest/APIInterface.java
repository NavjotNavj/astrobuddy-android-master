package in.appnow.astrobuddy.rest;

import in.appnow.astrobuddy.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.FetchAllMessageResponse;
import in.appnow.astrobuddy.rest.request.AbortPaymentRequest;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;
import in.appnow.astrobuddy.rest.request.AppVersionRequest;
import in.appnow.astrobuddy.rest.request.ApplyPromoCodeRequest;
import in.appnow.astrobuddy.rest.request.AuthorizePaymentRequest;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CallFeedbackRequest;
import in.appnow.astrobuddy.rest.request.CaptureCallPaymentRequest;
import in.appnow.astrobuddy.rest.request.CapturePaymentRequest;
import in.appnow.astrobuddy.rest.request.ChangePasswordRequest;
import in.appnow.astrobuddy.rest.request.ChatFeedbackRequest;
import in.appnow.astrobuddy.rest.request.CreateSubscriptionRequest;
import in.appnow.astrobuddy.rest.request.HoroscopeDetailRequest;
import in.appnow.astrobuddy.rest.request.InitiatePaymentRequest;
import in.appnow.astrobuddy.rest.request.LoginRequestModel;
import in.appnow.astrobuddy.rest.request.PaymentStatusRequest;
import in.appnow.astrobuddy.rest.request.PostUserStatsRequest;
import in.appnow.astrobuddy.rest.request.ProcessTransactionRequest;
import in.appnow.astrobuddy.rest.request.PromoCodeRequest;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import in.appnow.astrobuddy.rest.request.RegistrationRequestModel;
import in.appnow.astrobuddy.rest.request.ResetPasswordRequest;
import in.appnow.astrobuddy.rest.request.TipOfTheRequest;
import in.appnow.astrobuddy.rest.request.TrackNotificationClickRequest;
import in.appnow.astrobuddy.rest.request.TransactionReportRequest;
import in.appnow.astrobuddy.rest.request.UpdateEmailRequest;
import in.appnow.astrobuddy.rest.request.UpdateImageRequest;
import in.appnow.astrobuddy.rest.request.UpdateMaritalStatusRequest;
import in.appnow.astrobuddy.rest.request.UpdateSocketIdRequest;
import in.appnow.astrobuddy.rest.response.AccountHelpResponse;
import in.appnow.astrobuddy.rest.response.AppVersionResponse;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.CallHistoryResponse;
import in.appnow.astrobuddy.rest.response.CallPlansResponse;
import in.appnow.astrobuddy.rest.response.ChangePasswordResponse;
import in.appnow.astrobuddy.rest.response.ChatHistoryResponse;
import in.appnow.astrobuddy.rest.response.ChatSampleResponse;
import in.appnow.astrobuddy.rest.response.CreateSubscriptionResponse;
import in.appnow.astrobuddy.rest.response.ForgotPasswordResponse;
import in.appnow.astrobuddy.rest.response.HoroscopeDetailResponse;
import in.appnow.astrobuddy.rest.response.InitiatePaymentResponse;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.MythBusterResponse;
import in.appnow.astrobuddy.rest.response.OTPResponseModel;
import in.appnow.astrobuddy.rest.response.PlanResponse;
import in.appnow.astrobuddy.rest.response.ProcessTransactionResponse;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.rest.response.PromoCodeResponse;
import in.appnow.astrobuddy.rest.response.ReferralCodeResponse;
import in.appnow.astrobuddy.rest.response.ResetPasswordResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.response.TipOfTheDayResponse;
import in.appnow.astrobuddy.rest.response.TopicRateResponse;
import in.appnow.astrobuddy.rest.response.TransactionHistoryResponse;
import in.appnow.astrobuddy.rest.response.UpdateEmailResponse;
import in.appnow.astrobuddy.rest.response.UpdateImageResponse;
import in.appnow.astrobuddy.rest.response.UserPaymentCheckResponse;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

public interface APIInterface {
    @POST("abphp/api/v1/register-user.php")
    Observable<OTPResponseModel> sendOTP(@Body RegisterMobileRequest registerMobileRequest);

    @POST("abphp/api/v1/confirm-registration.php")
    Observable<LoginResponseModel> registerUser(@Body RegistrationRequestModel registrationRequestModel);

    @POST("abphp/api/v1/login-user.php")
    Observable<LoginResponseModel> loginUser(@Body LoginRequestModel loginRequestModel);

    @POST("abphp/api/v1/forgot-password.php")
    Observable<ForgotPasswordResponse> forgotPassword(@Body RegisterMobileRequest registerMobileRequest);

    @POST("abphp/api/v1/reset-password.php")
    Observable<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);


    @POST("api/chat/v1/checkforTopic")
    Observable<MyAccountResponse> getMyAccountDetails(@Body BaseRequestModel baseRequestModel);

    @POST("abphp/api/v1/horoscope-details.php")
    Observable<HoroscopeDetailResponse> getHoroscopeDetails(@Body HoroscopeDetailRequest horoscopeDetailRequest);

    @POST("abphp/api/v1/update-email.php")
    Observable<UpdateEmailResponse> updateEmail(@Body UpdateEmailRequest updateEmailRequest);

    @POST("abphp/api/v1/change-password.php")
    Observable<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST("abphp/api/v1/update-profile-picture.php")
    Observable<UpdateImageResponse> updateUserProfileImage(@Body UpdateImageRequest updateImageRequest);

    @POST("abphp/api/v1/get-promocodes.php")
    Observable<PromoCodeResponse> getPromoCodes(@Body PromoCodeRequest getPromoCodeRequest);

    @POST("abphp/api/v1/apply-promo.php")
    Observable<ApplyPromoCodeResponse> applyPromoCode(@Body ApplyPromoCodeRequest applyPromoCodeRequest);

//    @POST("abphp/api/v1/get-subscription-plans.php")
    //  Observable<SubscriptionPlanResponse> getSubscriptionPlans(@Body BaseRequestModel baseRequestModel);

    @POST("abphp/api/v1/RZPGetPlans.php")
    Observable<PlanResponse> getSubscriptionPlans(@Body BaseRequestModel baseRequestModel);

    @POST("abphp/api/v1/RZPCreateSubscription.php")
    Observable<CreateSubscriptionResponse> createSubscription(@Body CreateSubscriptionRequest subscriptionRequest);

    @POST("abphp/api/v1/CancelCurrentSubPlan.php")
    Observable<BaseResponseModel> cancelSubscription(@Body BaseRequestModel baseRequestModel);

    @POST("api/utils/v1/get_trans_history_user")
    Observable<TransactionHistoryResponse> getTransactionHistory(@Body BaseRequestModel baseRequestModel);

    @POST("abphp/api/v1/process-payment.php")
    Observable<ProcessTransactionResponse> processTransaction(@Body ProcessTransactionRequest processTransactionRequest);

    @POST("abphp/api/v1/get-chat-samples.php")
    Observable<ChatSampleResponse> getChatSamples();

    @POST("abphp/api/v1/myaccount-help-info.php")
    Observable<AccountHelpResponse> getMyAccountHelpInfo();

    @POST("abphp/api/v1/get-chat-security-tips.php")
    Observable<AccountHelpResponse> getChatSecurityTip();

    @POST("abphp/api/v1/get-user-referral-code.php")
    Observable<ReferralCodeResponse> getUserReferralCode(@Body BaseRequestModel requestModel);

    @POST("abphp/api/v1/change-plan-to-basic.php")
    Observable<BaseResponseModel> changePlanToBasic(@Body BaseRequestModel requestModel);

    @POST("abphp/api/v1/update-marital-status.php")
    Observable<BaseResponseModel> updateMaritalStatus(@Body UpdateMaritalStatusRequest requestModel);

    @POST("abphp/api/v1/get-tip-of-day.php")
    Observable<TipOfTheDayResponse> getTipOfTheDay(@Body TipOfTheRequest request);

    @FormUrlEncoded
    @POST("abphp/api/v1/ccavenue/GetRSA.php")
    Call<ResponseBody> getRSA(@Field("access_code") String accessCode, @Field("order_id") String orderId);

    @POST("abphp/api/v1/get-myth-busters.php")
    Observable<MythBusterResponse> getMythBusters(@Body BaseRequestModel requestModel);

    @POST("abphp/api/v1/submit-pg-status.php")
    Observable<BaseResponseModel> submitPGStatus(@Body PaymentStatusRequest request);

    @POST("api/utils/v1/report_transaction_app")
    Call<BaseResponseModel> transactionReport(@Body TransactionReportRequest reportRequest);

    @POST("abphp/api/v1/submit-app-download-details.php")
    Call<BaseResponseModel> appDownload(@Body AppDownloadRequest reportRequest);

    @POST("abphp/api/v1/get-user-topic-rate.php")
    Observable<TopicRateResponse> getUserTopicRate(@Body BaseRequestModel requestModel);

    @POST("abphp/api/v1/CheckPlaystoreAPPVersion.php")
    Observable<AppVersionResponse> getAppVersion(@Body AppVersionRequest request);

    @POST("abphp/api/v1/logout-user.php")
    Observable<BaseResponseModel> doLogout(@Body BaseRequestModel request);

    @POST("astro/droid/api/notification_aquisition")
    Call<BaseResponseModel> trackNotificationClick(@Body TrackNotificationClickRequest request);

    @POST("api/analytics/v1/storeAnaltics")
    Call<BaseResponseModel> postUserStats(@Body PostUserStatsRequest request);

    @GET
    Observable<ResponseBody> downloadImage(@Url String imageURL);


    /* Chat APIs */
    @POST("api/chat/v1/startChat")
    Observable<StartChatResponse> startChat();

    @POST("api/chat/v1/updateSocketId")
    Observable<BaseResponseModel> updateSocketId(@Body UpdateSocketIdRequest socketIdRequest);

    @POST("api/chat/v1/update_fdbk_rating")
    Observable<BaseResponseModel> submitChatFeedback(@Body ChatFeedbackRequest chatFeedbackRequest);

    @POST("abphp/api/v1/rate-astrobuddy-call.php")
    Observable<BaseResponseModel> submitCallFeeedBack(@Body CallFeedbackRequest callFeedbackRequest);

    @POST("api/chat/v1/getAllmsgsBysessionId")
    Observable<FetchAllMessageResponse> getAllMessagesBySessionId(@Body FetchMessageRequest baseRequestModel);

    @POST("api/chat/v1/update_message_status_client")
    Call<BaseResponseModel> updateMessageStatus(@Body UpdateMessageRequest request);

    @POST("abphp/api/v1/get-chat-history.php")
    Observable<ChatHistoryResponse> getChatHistory(@Body BaseRequestModel requestModel);

    @POST("abphp/api/v1/get-call-history.php")
    Observable<CallHistoryResponse> getCallHistory();

    @POST("api/abapp/v1/get_promo_banners")
    Observable<PromoBannerResponse> getPromoBanners();

    @POST("abphp/api/v1/get-call-plans.php")
    Observable<CallPlansResponse> getCallPlans(@Body BaseRequestModel baseRequestModel);

    /*Payment APIs */

    @POST("api/pymt/v1/pymtUserCheck")
    Observable<UserPaymentCheckResponse> checkUserPayment();

    @POST("api/pymt/v1/inititePayment")
    Observable<InitiatePaymentResponse> initiatePayment(@Body InitiatePaymentRequest paymentRequest);

    @POST("api/pymt/v1/paymentabortedUser")
    Observable<BaseResponseModel> abortPayment(@Body AbortPaymentRequest paymentRequest);

    @POST("api/pymt/v1/paymentAuthrise")
    Observable<BaseResponseModel> authorizePayment(@Body AuthorizePaymentRequest paymentRequest);

    @POST("api/pymt/v1/capturePayment")
    Observable<BaseResponseModel> capturePayment(@Body CapturePaymentRequest capturePaymentRequest);


    @POST("api/pymt/v1/capturePayment_voice_call")
    Observable<BaseResponseModel> captureCallPayment(@Body CaptureCallPaymentRequest capturePaymentRequest);

}
