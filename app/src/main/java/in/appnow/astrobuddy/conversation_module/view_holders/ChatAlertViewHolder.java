package in.appnow.astrobuddy.conversation_module.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.rest.response.Messages;
import in.appnow.astrobuddy.utils.VectorUtils;

/**
 * Created by SONU on 01/04/16.
 */
public class ChatAlertViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ChatAlertViewHolder.class.getSimpleName();

    @BindView(R.id.message)
    TextView message;
    private Context mContext;

    public ChatAlertViewHolder(Context mContext, View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.mContext = mContext;
    }

    public void bindData(ConversationResponse conversationModel) {
        message.setText(conversationModel.getMessage());
        VectorUtils.setVectorCompoundDrawable(message,mContext, R.drawable.ic_notifications_black_24dp,0,0,0, R.color.colorPrimary);
    }
    public void bindData(Messages messages) {
        message.setText(messages.getMessage());
        VectorUtils.setVectorCompoundDrawable(message,mContext, R.drawable.ic_notifications_black_24dp,0,0,0, R.color.colorPrimary);
    }

}
