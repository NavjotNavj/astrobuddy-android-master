package in.appnow.astrobuddy.conversation_module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.conversation_module.view_holders.ChatAlertViewHolder;
import in.appnow.astrobuddy.conversation_module.view_holders.ChatItemViewHolder;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.response.Messages;
import in.appnow.astrobuddy.utils.Logger;


public class ConversationMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ConversationMessageAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SELF = 1;
    private static final int TYPE_OTHER = 2;
    private static final int TYPE_TEXT_CHAT_ALERT = 3;
    private static final int TYPE_SELF_IMAGE = 3;
    private static final int TYPE_OTHER_IMAGE = 4;
    private static final int TYPE_SELF_VIDEO = 5;
    private static final int TYPE_OTHER_VIDEO = 6;

    private Context mContext;
    private List<ConversationResponse> conversationModelArrayList = new ArrayList<>(0);


    private PreferenceManger blurtPreferenceManger;

    public ConversationMessageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBlurtPreferenceManger(PreferenceManger blurtPreferenceManger) {
        this.blurtPreferenceManger = blurtPreferenceManger;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
            return new ChatItemViewHolder(mContext, itemView);
        } else if (viewType == TYPE_TEXT_CHAT_ALERT) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_alert, parent, false);
            return new ChatAlertViewHolder(mContext, itemView);
        } else if (viewType == TYPE_OTHER) {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
            return new ChatItemViewHolder(mContext, itemView);
        }


        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public int getItemViewType(int position) {
        ConversationResponse conversationModel = conversationModelArrayList.get(position);
        if (conversationModel.getMessageType().equalsIgnoreCase(ConversationUtils.TEXT_CHAT_ALERT)) {
            return TYPE_TEXT_CHAT_ALERT;
        }else if (conversationModel.getMessageId() == 1)
            return TYPE_OTHER;
        else if (blurtPreferenceManger.getUserDetails().getUserId().equalsIgnoreCase(String.valueOf(conversationModel.getSenderId())) )
            return TYPE_SELF;
        else
            return TYPE_OTHER;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatItemViewHolder) {
            ConversationResponse conversationModel = conversationModelArrayList.get(position);
            ((ChatItemViewHolder) holder).bindData(conversationModel, blurtPreferenceManger.getUserDetails().getUserId().equalsIgnoreCase(String.valueOf(conversationModel.getSenderId())));
        }else if (holder instanceof ChatAlertViewHolder) {
            ConversationResponse messages = conversationModelArrayList.get(position);
            ((ChatAlertViewHolder) holder).bindData(messages);
        }
    }


    @Override
    public int getItemCount() {
        return conversationModelArrayList != null ? conversationModelArrayList.size() : 0;
    }


    public void swapData(List<ConversationResponse> conversationResponseList) {
        this.conversationModelArrayList.clear();
        if (conversationResponseList != null && !conversationResponseList.isEmpty()) {
            this.conversationModelArrayList.addAll(conversationResponseList);
        }
        notifyDataSetChanged();
    }

    public List<ConversationResponse> getConversationModelArrayList() {
        return conversationModelArrayList;
    }
}

