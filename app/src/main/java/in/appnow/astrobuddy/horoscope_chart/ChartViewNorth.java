package in.appnow.astrobuddy.horoscope_chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import java.util.Arrays;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by ashwani.janghu on 9/8/2015.
 */
public class ChartViewNorth extends View {

    private int mLagnaNo = 1;
    private int[] rashiArr;
    private String[] planetStr;
    private int[] mHousePosition;
    private int mChartType;

    private Paint mPaint;
    private int mParentHeight;
    private int mParentWidth;
    private int mChartWidth;
    private int mChartHeight;

    private int mHalfHeight;
    private int mHalfWidth;

    private int mQurterWidth;
    private int mQuarterHeight;

    private int xOffset;
    private int yOffset;

    private float[] xPoints = new float[8];
    private float[] yPoints = new float[8];

    private float x10pcOffset;
    private float y10pcOffset;

    private float x5pcOffset;
    private float y5pcOffset;

    Rect topTextBounds = new Rect();
    Rect bottomTextBounds = new Rect();


    private int mTextSize = 38;

    private String[] FIRST_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] SECOND_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] THIRD_STR = {"Sa Ju Ra", "Mo Me", "Ra", "Ve"};
    private String[] FOURTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] FIFTH_STR = {"Sa Ju Ra", "Mo Me", "Ra", "Ve"};
    private String[] SIXTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] SEVENTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] EIGHTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] NINTH_STR = {"Sa Ju Ra", "Mo Me", "Ra", "Ve"};
    private String[] TENTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};
    private String[] ELEVENTH_STR = {"Sa Ju Ra", "Mo Me", "Ra", "Ve"};
    private String[] TWELFTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra"};


    public ChartViewNorth(Context context, int parentWidth, int[] rashiArr, String[] planetStr) {
        super(context);
        mParentWidth = parentWidth;
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        calculateDimensions();
        this.rashiArr = rashiArr;
        this.planetStr = planetStr;
        updatePlanetStrArrays();
    }



    public ChartViewNorth(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewParent parent = getParent();
        mParentWidth = getWidth();
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        calculateDimensions();

    }

    public ChartViewNorth(Context context, AttributeSet attrs) {

        super(context, attrs);
        mParentWidth = getWidth();
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        calculateDimensions();
    }

    private void updatePlanetStrArrays(){

        Logger.DebugLog("PLANETS", Arrays.toString(planetStr));
        for(int i=0; i<planetStr.length;i++){

            String str = planetStr[i];
            if(!(null != str && str.length() > 0)) {
                str = " ";
            }
            switch (i) {
                case 0:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        FIRST_STR[0] = part1;
                        FIRST_STR[1] = part2;
                    }else{
                        FIRST_STR[0] = str;
                        FIRST_STR[1] = "";
                    }

                    break;
                case 1:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        SECOND_STR[0] = part1;
                        SECOND_STR[1] = part2;
                    }else{
                        SECOND_STR[0] = str;
                        SECOND_STR[1] = "";
                    }
                    break;
                case 2:
                    partComplexArray(str,THIRD_STR);
                    break;
                case 3:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        FOURTH_STR[0] = part1;
                        FOURTH_STR[1] = part2;
                    }else{
                        FOURTH_STR[0] = str;
                        FOURTH_STR[1] = "";
                    }
                    break;
                case 4:
                    partComplexArray(str,FIFTH_STR);
                case 5:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        SIXTH_STR[0] = part1;
                        SIXTH_STR[1] = part2;
                    }else{
                        SIXTH_STR[0] = str;
                        SIXTH_STR[1] = "";
                    }
                    break;
                case 6:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        SEVENTH_STR[0] = part1;
                        SEVENTH_STR[1] = part2;
                    }else{
                        SEVENTH_STR[0] = str;
                        SEVENTH_STR[1] = "";
                    }
                    break;
                case 7:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        EIGHTH_STR[0] = part1;
                        EIGHTH_STR[1] = part2;
                    }else{
                        EIGHTH_STR[0] = str;
                        EIGHTH_STR[1] = "";
                    }
                    break;
                case 8:
                    partComplexArray(str,NINTH_STR);
                case 9:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        TENTH_STR[0] = part1;
                        TENTH_STR[1] = part2;
                    }else{
                        TENTH_STR[0] = str;
                        TENTH_STR[1] = "";
                    }
                    break;
                case 10:
                    partComplexArray(str,ELEVENTH_STR);
                case 11:
                    if(str.length() > 14){
                        String part1 = str.substring(0,14);
                        String part2 = str.substring(14,str.length());
                        TWELFTH_STR[0] = part1;
                        TWELFTH_STR[1] = part2;
                    }else{
                        TWELFTH_STR[0] = str;
                        TWELFTH_STR[1] = "";
                    }
                    break;

            }


        }
    }

    private void partComplexArray(String str, String[] updateArr){

        if(str.length() > 10){
            updateArr[0] = str.substring(0,10);
            try {
                updateArr[1] = str.substring(11, 16);
            }catch (IndexOutOfBoundsException e){
                updateArr[1] = "";
                updateArr[2] = "";
                updateArr[3] = "";
                return;
            }
            try {
                updateArr[2] = str.substring(17, 19);
            }catch (IndexOutOfBoundsException e){
                updateArr[2] = "";
                updateArr[3] = "";
            }
            try {
                updateArr[3] = str.substring(20, str.length());
            }catch (IndexOutOfBoundsException e){
                updateArr[3] = "";
            }
        }else{
            updateArr[0] = str;
            updateArr[1] = "";
            updateArr[2] = "";
            updateArr[3] = "";
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setStrokeWidth(2);

        canvas.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1], mPaint);
        canvas.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2], mPaint);
        canvas.drawLine(xPoints[2], yPoints[2], xPoints[3], yPoints[3], mPaint);
        canvas.drawLine(xPoints[3], yPoints[3], xPoints[0], yPoints[0], mPaint);

        canvas.drawLine(xPoints[0], yPoints[0], xPoints[2], yPoints[2], mPaint);
        canvas.drawLine(xPoints[1], yPoints[1], xPoints[3], yPoints[3], mPaint);

        canvas.drawLine(xPoints[4], yPoints[4], xPoints[5], yPoints[5], mPaint);
        canvas.drawLine(xPoints[5], yPoints[5], xPoints[6], yPoints[6], mPaint);
        canvas.drawLine(xPoints[6], yPoints[6], xPoints[7], yPoints[7], mPaint);
        canvas.drawLine(xPoints[7], yPoints[7], xPoints[4], yPoints[4], mPaint);

        drawRashiNumbers(canvas);

        drawCenterHouse(canvas, FIRST_STR[1], FIRST_STR[0], xPoints[4], yPoints[4]);
        drawCenterHouse(canvas, FOURTH_STR[1], FOURTH_STR[0], xPoints[0] + mQurterWidth, yPoints[0] + mQuarterHeight);
        drawCenterHouse(canvas, SEVENTH_STR[1], SEVENTH_STR[0], xPoints[4], yPoints[4] + mHalfHeight);
        drawCenterHouse(canvas, TENTH_STR[1], TENTH_STR[0], xPoints[4] + mQurterWidth, yPoints[4] + mQuarterHeight);

        drawTrianeHouse(canvas, SECOND_STR[0], SECOND_STR[1], xPoints[0], yPoints[0], true);
        drawTrianeHouse(canvas, TWELFTH_STR[0], TWELFTH_STR[1], xPoints[4], yPoints[4], true);
        drawTrianeHouse(canvas, SIXTH_STR[1], SIXTH_STR[0], xPoints[3], yPoints[3], false);
        drawTrianeHouse(canvas, EIGHTH_STR[1], EIGHTH_STR[0], xPoints[6], yPoints[6], false);

        drawTrianeHouse2(canvas, THIRD_STR[0], THIRD_STR[1], THIRD_STR[2], THIRD_STR[3], xPoints[0], yPoints[0], true);
        drawTrianeHouse2(canvas, FIFTH_STR[0], FIFTH_STR[1], FIFTH_STR[2], FIFTH_STR[3], xPoints[7], yPoints[7], true);
        drawTrianeHouse2(canvas, ELEVENTH_STR[0], ELEVENTH_STR[1], ELEVENTH_STR[2], ELEVENTH_STR[3], xPoints[1], yPoints[1], false);
        drawTrianeHouse2(canvas, NINTH_STR[0], NINTH_STR[1], NINTH_STR[2], NINTH_STR[3], xPoints[5], yPoints[5], false);
    }


    private void drawCenterHouse(Canvas canvas, String topString, String bottomString, float topX, float topY) {
        float tHeight, tWidth, bWidth;

        mPaint.getTextBounds(topString, 0, topString.length(), topTextBounds);
        mPaint.getTextBounds(bottomString, 0, bottomString.length(), bottomTextBounds);

        tHeight = topTextBounds.height();

        tWidth = topTextBounds.width() / 2;
        bWidth = bottomTextBounds.width() / 2;
        Logger.DebugLog("Kundli Canvas", "Kundali Canvas (topWidth and BottomWidth) == " + tWidth + " , " + bWidth);

        float xCorrT = tWidth;
        float yCorrT = 2 * y10pcOffset;

        float xCorrB = bWidth;
        float yCorrB = 2 * y10pcOffset + tHeight + x5pcOffset;

        if (bottomString.length() > 1) {

            canvas.drawText(bottomString, topX - xCorrB, topY + yCorrB, mPaint);

            if (topString.length() > 1) {
                canvas.drawText(topString, topX - xCorrT, topY + yCorrT, mPaint);
            }
        }
    }

    private void drawTrianeHouse(Canvas canvas, String topString, String bottomString, float leftX, float leftY, boolean isTop) {
        float tHeight, tWidth, bWidth;

        mPaint.getTextBounds(topString, 0, topString.length(), topTextBounds);
        mPaint.getTextBounds(bottomString, 0, bottomString.length(), bottomTextBounds);

        tHeight = topTextBounds.height();
        tWidth = topTextBounds.width() / 2;
        bWidth = bottomTextBounds.width() / 2;

        float xCorrT = tWidth;
        float yCorrT = y5pcOffset;

        float xCorrB = bWidth;
        float yCorrB = x5pcOffset + tHeight;

        if (isTop) {
            if (topString.length() > 1) {

                canvas.drawText(topString, leftX + mQurterWidth - xCorrT, leftY + yCorrT, mPaint);

                if (bottomString.length() > 1) {
                    canvas.drawText(bottomString, leftX + mQurterWidth - xCorrB, leftY + yCorrB, mPaint);
                }
            }
        } else {
            if (bottomString.length() > 1) {

                canvas.drawText(bottomString, leftX + mQurterWidth - xCorrB, leftY - yCorrT, mPaint);

                if (topString.length() > 1) {
                    canvas.drawText(topString, leftX + mQurterWidth - xCorrT, leftY - yCorrB, mPaint);
                }
            }
        }
    }

    private void drawTrianeHouse2(Canvas canvas, String str1, String str2, String str3, String str4, float leftX, float leftY, boolean isLeft) {
        mPaint.getTextBounds(str1, 0, str1.length(), topTextBounds);

        float tHeight = topTextBounds.height();

        float yCorrT = 3 * y5pcOffset;
        float xCorr = y5pcOffset / 2;
        float yCorrB = (x10pcOffset / 5) + tHeight;

        if (isLeft) {
            if (str1.length() > 1) {
                canvas.drawText(str1, leftX + xCorr, leftY + yCorrT + yCorrB, mPaint);
                canvas.drawText(str2, leftX + xCorr, leftY + yCorrT + 2 * yCorrB, mPaint);
                canvas.drawText(str3, leftX + xCorr, leftY + yCorrT + 3 * yCorrB, mPaint);
                canvas.drawText(str4, leftX + xCorr, leftY + yCorrT, mPaint);
            }
        } else {
            if (str1.length() > 1) {

                mPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(str1, leftX - xCorr, leftY + yCorrT + yCorrB, mPaint);
                canvas.drawText(str2, leftX - xCorr, leftY + yCorrT + 2 * yCorrB, mPaint);
                canvas.drawText(str3, leftX - xCorr, leftY + yCorrT + 3 * yCorrB, mPaint);
                canvas.drawText(str4, leftX - xCorr, leftY + yCorrT, mPaint);
                mPaint.setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    private void drawRashiNumbers(Canvas canvas) {
        //mTextSize = (int) (x10pcOffset / 3);
        mPaint.setTextSize(mTextSize);
        String str = mLagnaNo + "";
        mPaint.getTextBounds(str, 0, str.length(), topTextBounds);
        int xc = topTextBounds.width() / 2;
        int yc = topTextBounds.height() / 2;

        canvas.drawText("" + fix12(rashiArr[0]), xPoints[4] - xc, yPoints[7] - y10pcOffset, mPaint); // for Lagna rashi
        canvas.drawText("" + fix12(rashiArr[1]), (xPoints[4] - mQurterWidth) - xc, (yPoints[7] - mQuarterHeight) - y10pcOffset, mPaint); // for second house
        canvas.drawText("" + fix12(rashiArr[2]), (xPoints[4] - mQurterWidth) - x10pcOffset, (yPoints[7] - mQuarterHeight) - yc, mPaint); // for third house

        canvas.drawText("" + fix12(rashiArr[3]), (xPoints[4] - mQurterWidth) - xc, (yPoints[6] - mQuarterHeight) - y10pcOffset, mPaint); // for fourth house
        canvas.drawText("" + fix12(rashiArr[4]), (xPoints[4] - mQurterWidth) - x10pcOffset, (yPoints[6] - mQuarterHeight) - yc, mPaint); // for fifth house

        canvas.drawText("" + fix12(rashiArr[5]), (xPoints[7] + mQurterWidth) - xc, (yPoints[6] - mQuarterHeight) + y10pcOffset, mPaint); // for sixth house
        canvas.drawText("" + fix12(rashiArr[6]), (xPoints[4]) - xc, (yPoints[7]) + y10pcOffset, mPaint); // for seventh house

        canvas.drawText("" + fix12(rashiArr[7]), (xPoints[4] + mQurterWidth) - xc, (yPoints[6] - mQuarterHeight) + y10pcOffset, mPaint); // for eighth house
        canvas.drawText("" + fix12(rashiArr[8]), (xPoints[4] + mQurterWidth) + x5pcOffset * 1.5f, (yPoints[7] + mQuarterHeight) - yc, mPaint); // for ninth house

        canvas.drawText("" + fix12(rashiArr[9]), (xPoints[4] + mQurterWidth) - xc, (yPoints[6] - mQuarterHeight) - y10pcOffset, mPaint); // for tenth house
        canvas.drawText("" + fix12(rashiArr[10]), (xPoints[4] + mQurterWidth) + x5pcOffset * 1.5f, (yPoints[4] + mQuarterHeight) - yc, mPaint); // for eleventh house

        canvas.drawText("" + fix12(rashiArr[11]), (xPoints[4] + mQurterWidth) - xc, (yPoints[4] + mQuarterHeight) - y10pcOffset, mPaint); // for twelfth house
    }

    private void calculateDimensions() {
        xOffset = mParentWidth / 20;
        yOffset = mParentWidth / 30;

        mChartWidth = mParentWidth - 2 * xOffset;
        mChartHeight = mChartWidth - 2 * xOffset;

        mHalfWidth = mChartWidth / 2;
        mHalfHeight = mChartHeight / 2;

        mQuarterHeight = mHalfHeight / 2;
        mQurterWidth = mHalfWidth / 2;

        xPoints[0] = xOffset; // Top Left X-Coordinate
        yPoints[0] = yOffset; // Top Left Y-Coordinate

        xPoints[1] = xOffset + mChartWidth;
        yPoints[1] = yOffset;

        xPoints[2] = xOffset + mChartWidth;
        yPoints[2] = yOffset + mChartHeight;

        xPoints[3] = xOffset;
        yPoints[3] = yOffset + mChartHeight;

        xPoints[4] = xOffset + mHalfWidth;
        yPoints[4] = yOffset;

        xPoints[5] = xOffset + mChartWidth;
        yPoints[5] = yOffset + mHalfHeight;

        xPoints[6] = xOffset + mHalfWidth;
        yPoints[6] = yOffset + mChartHeight;

        xPoints[7] = xOffset;
        yPoints[7] = yOffset + mHalfHeight;

        x10pcOffset = ((xPoints[1] - xPoints[0]) + (yPoints[1] - yPoints[0])) / 10;
        y10pcOffset = ((xPoints[3] - xPoints[0]) + (yPoints[3] - yPoints[0])) / 10;

        x5pcOffset = ((xPoints[1] - xPoints[0]) + (yPoints[1] - yPoints[0])) / 20;
        y5pcOffset = ((xPoints[3] - xPoints[0]) + (yPoints[3] - yPoints[0])) / 20;
    }

    public int fix12(int v){

        while(v > 12)
        {
            v -= 12;
        }
        return v;
    }
}

