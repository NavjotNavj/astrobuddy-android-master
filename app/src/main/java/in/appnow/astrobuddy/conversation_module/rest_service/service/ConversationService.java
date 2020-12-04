package in.appnow.astrobuddy.conversation_module.rest_service.service;

import android.content.Context;

import androidx.annotation.NonNull;

import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sonu on 18/12/17.
 */

public class ConversationService {

   public static void submitMessageStatus(final Context context, APIInterface blurtApiInterface, UpdateMessageRequest updateMessageRequest, final APICallback blurtApiCallback, final int requestCode) {

       Call<BaseResponseModel> call = blurtApiInterface.updateMessageStatus(updateMessageRequest);
       call.enqueue(new Callback<BaseResponseModel>() {
           @Override
           public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
               blurtApiCallback.onResponse(call, response, requestCode, updateMessageRequest);
           }

           @Override
           public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
               blurtApiCallback.onFailure(call, t, requestCode, updateMessageRequest);

           }
       });
   }
}
