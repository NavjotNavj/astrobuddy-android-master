package in.appnow.astrobuddy.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import in.appnow.astrobuddy.R;


/**
 * Created by NILESH BHARODIYA on 29-08-2019.
 */
public class RatedView extends LinearLayout {

    private Context context;
    private int totalStar;
    private int ratedStar;
    private Drawable selectedDrawable;
    private Drawable unSelectedDrawable;
    private int height;

    public RatedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RatedView, 0, 0);


        totalStar = a.getInt(R.styleable.RatedView_total_star, 0);
        ratedStar = a.getInt(R.styleable.RatedView_rated_star, 0);
        selectedDrawable = a.getDrawable(R.styleable.RatedView_selected_drawable);
        unSelectedDrawable = a.getDrawable(R.styleable.RatedView_unselected_drawable);
        height = (int) a.getDimension(R.styleable.RatedView_icon_height, 30);

        if (unSelectedDrawable == null) {
            unSelectedDrawable = getResources().getDrawable(R.drawable.ic_star_unselected);
        }

        a.recycle();

        setView();
    }

    private void setView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rated_view, this, true);

        LinearLayout linearLayout = view.findViewById(R.id.root_view);

        linearLayout.removeAllViews();

        for (int i = 0; i < totalStar; i++) {
            ImageView imageView = new ImageView(context);
            LayoutParams params = new LayoutParams(height, height);
            imageView.setLayoutParams(params);
            imageView.setImageDrawable(i < ratedStar ? selectedDrawable : unSelectedDrawable);
            linearLayout.addView(imageView);
        }

    }

    public void setUnSelectedDrawable(Drawable unSelectedDrawable) {
        this.unSelectedDrawable = unSelectedDrawable;
        setView();
    }

    public void setRatedStar(int ratedStar) {
        this.ratedStar = ratedStar;
        setView();
    }

    public void setTotalStar(int totalStar) {
        this.totalStar = totalStar;
        setView();
    }

    public void setSelectedDrawable(Drawable selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
        setView();
    }
}
