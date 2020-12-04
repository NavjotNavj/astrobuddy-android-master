package in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.conversation_module.view_holders.ChatAlertViewHolder;
import in.appnow.astrobuddy.conversation_module.view_holders.ChatItemViewHolder;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.response.Messages;


public class ChatHistoryConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ChatHistoryConversationAdapter.class.getSimpleName();

    private static final int TYPE_SELF = 1;
    private static final int TYPE_OTHER = 2;
    private static final int TYPE_TEXT_CHAT_ALERT = 3;
    private Context mContext;
    private List<Messages> messagesArrayList = new ArrayList<>(0);

    private PreferenceManger preferenceManger;

    public ChatHistoryConversationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPreferenceManger(PreferenceManger preferenceManger) {
        this.preferenceManger = preferenceManger;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_TEXT_CHAT_ALERT) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_alert, parent, false);
            return new ChatAlertViewHolder(mContext, itemView);
        } else if (viewType == TYPE_SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
            return new ChatItemViewHolder(mContext, itemView);
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
        Messages messages = messagesArrayList.get(position);
      /*  if (isPositionHeader(position))
            return TYPE_HEADER;
        else */
        if (messages.getMsgType().equalsIgnoreCase(ConversationUtils.TEXT_CHAT_ALERT)) {
            return TYPE_TEXT_CHAT_ALERT;
        } else if (preferenceManger.getUserDetails().getUserProfile().getUserId().equalsIgnoreCase(String.valueOf(messages.getFromUserId())))
            return TYPE_SELF;
        else
            return TYPE_OTHER;
        // return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatItemViewHolder) {
            Messages messages = messagesArrayList.get(position);
            ((ChatItemViewHolder) holder).bindChatHistoryData(messages);
        } else if (holder instanceof ChatAlertViewHolder) {
            Messages messages = messagesArrayList.get(position);
            ((ChatAlertViewHolder) holder).bindData(messages);
        }
    }


    @Override
    public int getItemCount() {
        return messagesArrayList != null ? messagesArrayList.size() : 0;
    }


    public void swapData(List<Messages> messagesArrayList) {
        this.messagesArrayList.clear();
        if (messagesArrayList != null && !messagesArrayList.isEmpty()) {
            this.messagesArrayList.addAll(messagesArrayList);
        }
        notifyDataSetChanged();
    }

}

