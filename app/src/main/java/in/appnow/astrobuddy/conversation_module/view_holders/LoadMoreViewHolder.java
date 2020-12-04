package in.appnow.astrobuddy.conversation_module.view_holders;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import in.appnow.astrobuddy.R;


/**
 * Created by SONU on 01/04/16.
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    public Button loadMore_button;

    public LoadMoreViewHolder(View itemView) {
        super(itemView);

        loadMore_button = (Button) itemView.findViewById(R.id.load_more_chat_button);
    }
}