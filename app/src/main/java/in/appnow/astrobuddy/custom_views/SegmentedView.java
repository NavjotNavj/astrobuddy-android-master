package in.appnow.astrobuddy.custom_views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import in.appnow.astrobuddy.R;

/**
 * Created by NILESH BHARODIYA on 02-09-2019.
 */
public class SegmentedView extends RadioGroup {

    /**
     * Interface for for the selection change event
     */
    public interface OnSelectionChangedListener {
        void newSelection(int id, String selectedItem, String selectedValue);
    }

    private Context context;

    private OnSelectionChangedListener listener;

    private int selectedColor = getResources().getColor(R.color.colorPrimary);
    private int unSelectedColor = Color.TRANSPARENT;
    private int defaultSelection = 0;
    private String currentSelectedKey = "";
    private String currentSelectedValue = "";
    private boolean stretch = false;
    private int borderColor = Color.BLACK;
    private float cornerRadius = 0f;
    private int borderWidth = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
    private float textSize = 10;

    private ColorStateList textColorStateList;
    private LinkedHashMap<String, String> itemMap = new LinkedHashMap<>();
    private ArrayList<RadioButton> options;

    public SegmentedView(Context context) {
        super(context);

        initialize(context);

        update();
    }

    public SegmentedView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);

        int selectedTextColor = Color.WHITE;
        int unSelectedTextColor = getResources().getColor(R.color.colorPrimary);

        if (attrs != null) {
            TypedArray attributes = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.SegmentedView, 0, 0);

            try {
                selectedColor = attributes.getColor(R.styleable.SegmentedView_sv_selectedColor,
                        selectedColor);
                unSelectedColor = attributes.getColor(R.styleable.SegmentedView_sv_unselectedColor,
                        unSelectedColor);
                selectedTextColor = attributes.getColor(R.styleable.SegmentedView_sv_selectedTextColor,
                        selectedTextColor);
                unSelectedTextColor = attributes.getColor(R.styleable.SegmentedView_sv_unselectedTextColor,
                        unSelectedTextColor);
                borderColor = attributes.getColor(R.styleable.SegmentedView_sv_border_color,
                        borderColor);
                borderWidth = attributes.getInteger(R.styleable.SegmentedView_sv_border_width,
                        borderWidth);
                cornerRadius = attributes.getDimension(R.styleable.SegmentedView_sv_corner_radius,
                        cornerRadius);

                textSize = attributes.getDimension(R.styleable.SegmentedView_sv_text_size, textSize);

                defaultSelection =
                        attributes.getInt(R.styleable.SegmentedView_sv_defaultSelection, defaultSelection);
                stretch = attributes.getBoolean(R.styleable.SegmentedView_sv_stretch, stretch);

                CharSequence[] keysArray =
                        attributes.getTextArray(R.styleable.SegmentedView_sv_keys);
                CharSequence[] valueArray = attributes.getTextArray(R.styleable.SegmentedView_sv_values);

                textColorStateList = new ColorStateList(new int[][]{
                        {-android.R.attr.state_checked}, {android.R.attr.state_checked}},
                        new int[]{unSelectedTextColor, selectedTextColor});

                if (keysArray == null) {
                    keysArray = new CharSequence[]{"YES", "NO"};
                }

                if (valueArray != null) {
                    if (keysArray.length != valueArray.length) {
                        throw new RuntimeException("ItemArray and ValueArray must be the same size");
                    }
                }

                if (valueArray != null) {
                    for (int i = 0; i < keysArray.length; i++) {
                        itemMap.put(keysArray[i].toString(), valueArray[i].toString());
                    }

                } else {
                    for (CharSequence item : keysArray) {
                        itemMap.put(item.toString(), item.toString());
                    }
                }

            } finally {
                attributes.recycle();
            }
        }
        update();
    }

    private void initialize(Context context) {
        this.context = context;
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.listener = listener;
    }

    public void setKeysWithValue(CharSequence[] keysArray, CharSequence[] valueArray) throws Exception {

        this.itemMap.clear();

        if (keysArray != null && valueArray != null) {
            if (keysArray.length != valueArray.length) {
                throw new Exception("Keys and value arrays must be the same size");
            }
        }

        if (keysArray != null) {

            if (valueArray != null) {
                for (int i = 0; i < keysArray.length; i++) {
                    itemMap.put(keysArray[i].toString(), valueArray[i].toString());
                }

            } else {

                for (CharSequence item : keysArray) {
                    itemMap.put(item.toString(), item.toString());
                }

            }
        }
        update();
    }

    private void update() {
        this.removeAllViews();

        this.setOrientation(HORIZONTAL);

        options = new ArrayList<RadioButton>();

        float[] selectedRadius = new float[8];
        Arrays.fill(selectedRadius, cornerRadius);

        float[] unSelectedRadius = new float[8];
        Arrays.fill(unSelectedRadius, 0F);

        Iterator iterator = itemMap.entrySet().iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Map.Entry<String, String> item = (Map.Entry) iterator.next();

            RadioButton radioButton = new RadioButton(context);
            radioButton.setTextColor(textColorStateList);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (stretch) {
                params.weight = 1.0f;
            }

            if (i > 0) {
                params.setMargins(-borderWidth, 0, 0, 0);
            }

            radioButton.setLayoutParams(params);

            radioButton.setButtonDrawable(new StateListDrawable());

            if (i == 0) {
                //Left
                GradientDrawable leftUnselected =
                        (GradientDrawable) context.getResources().getDrawable(R.drawable.left_option).mutate();
                leftUnselected.setStroke(borderWidth, borderColor);
                //  top-left, top-right, bottom-right, bottom-left
                leftUnselected.setCornerRadii(new float[]{cornerRadius, cornerRadius,
                        0F, 0F, 0F, 0F, cornerRadius, cornerRadius});

                leftUnselected.setColor(unSelectedColor);
                GradientDrawable leftSelected =
                        (GradientDrawable) context.getResources().getDrawable(R.drawable.left_option_selected).mutate();
                leftSelected.setColor(selectedColor);
                leftSelected.setStroke(borderWidth, borderColor);
                //  top-left, top-right, bottom-right, bottom-left
                leftSelected.setCornerRadii(selectedRadius);


                StateListDrawable leftStateListDrawable = new StateListDrawable();
                leftStateListDrawable.addState(new int[]{-android.R.attr.state_checked}, leftUnselected);
                leftStateListDrawable.addState(new int[]{android.R.attr.state_checked}, leftSelected);
                radioButton.setBackground(leftStateListDrawable);

            } else if (i == (itemMap.size() - 1)) {
                //Right
                GradientDrawable rightUnselected =
                        (GradientDrawable) context.getResources().getDrawable(R.drawable.right_option).mutate();
                rightUnselected.setStroke(borderWidth, borderColor);
                //  top-left, top-right, bottom-right, bottom-left
                rightUnselected.setCornerRadii(new float[]{0F, 0F, cornerRadius,
                        cornerRadius, cornerRadius, cornerRadius, 0F, 0F});

                rightUnselected.setColor(unSelectedColor);
                GradientDrawable rightSelected =
                        (GradientDrawable) context.getResources().getDrawable(R.drawable.right_option_selected).mutate();
                rightSelected.setColor(selectedColor);
                rightSelected.setStroke(borderWidth, borderColor);
                //  top-left, top-right, bottom-right, bottom-left
                rightSelected.setCornerRadii(selectedRadius);

                StateListDrawable rightStateListDrawable = new StateListDrawable();
                rightStateListDrawable.addState(new int[]{-android.R.attr.state_checked}, rightUnselected);
                rightStateListDrawable.addState(new int[]{android.R.attr.state_checked}, rightSelected);
                radioButton.setBackground(rightStateListDrawable);

            } else {
                //Middle
                GradientDrawable middleUnselected =
                        (GradientDrawable) context.getResources().getDrawable(R.drawable.middle_option).mutate();
                middleUnselected.setStroke(borderWidth, borderColor);
                //  top-left, top-right, bottom-right, bottom-left
                middleUnselected.setCornerRadii(unSelectedRadius);
                middleUnselected.setDither(true);
                middleUnselected.setColor(unSelectedColor);

                GradientDrawable middleSelected =
                        (GradientDrawable) context.getResources().getDrawable(R.drawable.middle_option_selected).mutate();
                middleSelected.setColor(selectedColor);
                middleSelected.setStroke(borderWidth, borderColor);
                //  top-left, top-right, bottom-right, bottom-left
                middleSelected.setCornerRadii(selectedRadius);

                StateListDrawable middleStateListDrawable = new StateListDrawable();
                middleStateListDrawable.addState(new int[]{-android.R.attr.state_checked}, middleUnselected);
                middleStateListDrawable.addState(new int[]{android.R.attr.state_checked}, middleSelected);
                radioButton.setBackground(middleStateListDrawable);

            }

            radioButton.setLayoutParams(params);
            radioButton.setMinWidth(borderWidth * 10);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTypeface(null, Typeface.NORMAL);
            radioButton.setText(item.getKey());
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

            options.add(radioButton);

            i++;
        }

        for (RadioButton option : options) {
            this.addView(option);
        }

        GradientDrawable backBG =
                (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_option).mutate();
        backBG.setStroke(borderWidth, borderColor);
        backBG.setCornerRadii(selectedRadius);
        backBG.setColor(unSelectedColor);
        this.setBackground(backBG);

        this.setOnCheckedChangeListener(onCheckedChangeListener);

        if (defaultSelection > -1) {
            this.check(getChildAt(defaultSelection).getId());
        }
    }

    public void setSelection(int selection) {
        this.defaultSelection = selection;

        update();
    }

    public String getCurrentSelectedKey() {
        return currentSelectedKey;
    }

    public String getCurrentSelectedValue() {
        return this.currentSelectedValue;
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            currentSelectedKey =
                    ((RadioButton) group.findViewById(checkedId)).getText().toString();

            currentSelectedValue =
                    itemMap.get(((RadioButton) group.findViewById(checkedId)).getText().toString());

            if (listener != null) {
                listener.newSelection(getId(), currentSelectedKey, currentSelectedValue);
            }

        }
    };

}
