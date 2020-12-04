package in.appnow.astrobuddy.horoscope_chart;

/**
 *  @author Vedic Rishi Astro Pvt Ltd
 *  @desc South Indian horoscope chart view based on /horo_chart/:chartId API
 *  @copyright Vedic Rishi Astro Pvt Ltd @ 2015
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import in.appnow.astrobuddy.R;


public class ChartViewSouth extends View {

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


    private int mTextSize = 16;

    private String[] FIRST_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] SECOND_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] THIRD_STR = {"Sa Ju Ra", "Mo Me", "Ra"};
    private String[] FOURTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] FIFTH_STR = {"Sa Ju Ra", "Mo Me", "Ra"};
    private String[] SIXTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] SEVENTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] EIGHTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] NINTH_STR = {"Sa Ju Ra", "Mo Me", "Ra"};
    private String[] TENTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};
    private String[] ELEVENTH_STR = {"Sa Ju Ra", "Mo Me", "Ra"};
    private String[] TWELFTH_STR = {"Su Mo Ve Ma Me", "Sa Ju Ra", ""};


    public ChartViewSouth(Context context, int parentWidth, int[] rashiArr, String[] planetStr) {
        super(context);
        mParentWidth = parentWidth;
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.SANS_SERIF);
        calculateDimensions();
        this.rashiArr = rashiArr;
        this.planetStr = planetStr;
        updatePlanetStrArrays();
    }



    public ChartViewSouth(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewParent parent = getParent();
        mParentWidth = getWidth();
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.SANS_SERIF);
        calculateDimensions();

    }

    public ChartViewSouth(Context context, AttributeSet attrs) {

        super(context, attrs);
        mParentWidth = getWidth();
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.SANS_SERIF);
        calculateDimensions();
    }

    private void updatePlanetStrArrays(){

        //Log.i("PLANETS", Arrays.toString(planetStr));
        for(int i=0; i<planetStr.length;i++)
        {

            String str = planetStr[i];
            String arrStr[] = {};
            if(!(null != str && str.length() > 0)) {
                str = " ";
            }
            int rashi = fix12(this.rashiArr[0] + i);

            switch (rashi){
                case 1:
                    arrStr = FIRST_STR;
                    break;
                case 2:
                    arrStr = SECOND_STR;
                    break;
                case 3:
                    arrStr = THIRD_STR;
                    break;
                case 4:
                    arrStr = FOURTH_STR;
                    break;
                case 5:
                    arrStr = FIFTH_STR;
                    break;
                case 6:
                    arrStr = SIXTH_STR;
                    break;
                case 7:
                    arrStr = SEVENTH_STR;
                    break;
                case 8:
                    arrStr = EIGHTH_STR;
                    break;
                case 9:
                    arrStr = NINTH_STR;
                    break;
                case 10:
                    arrStr = TENTH_STR;
                    break;
                case 11:
                    arrStr = ELEVENTH_STR;
                    break;
                case 12:
                    arrStr = TWELFTH_STR;
                    break;
            }
            //Log.e("PLANETS IN STR", str);
            //Log.e("PLANETS IN STR", str.length()+"");
            if(str.length() >= 12){
                arrStr[0] = str.substring(0,12);
                try{
                    if(str.length() >= 26)
                    {
                        arrStr[1] = str.substring(12, 26);
                    }
                    else
                    {
                        arrStr[1] = str.substring(12, str.length());
                    }
                }
                catch (IndexOutOfBoundsException e)
                {
                    arrStr[1] = "";
                    arrStr[2] = "";
                }
                try{
                    arrStr[2] =  str.substring(26,str.length());
                }
                catch(IndexOutOfBoundsException e)
                {
                    arrStr[2] = "";
                }
                //Log.i("PLANETS IN IF", Arrays.toString(arrStr));
            }else{
                arrStr[0] = str;
                arrStr[1] = "";
                arrStr[2] = "";
               // Log.i("PLANETS IN ELSE", Arrays.toString(arrStr));
            }

        }


    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(getResources().getColor(R.color.black_overlay));
        mPaint.setStrokeWidth(2);

        canvas.drawLine(xOffset, yOffset, xOffset + mChartWidth, yOffset, mPaint);
        canvas.drawLine(xOffset, yOffset, xOffset, yOffset + mChartHeight,  mPaint);

        canvas.drawLine(xOffset + mChartWidth, yOffset, xOffset + mChartWidth, yOffset + mChartHeight,  mPaint);
        canvas.drawLine(xOffset, yOffset + mChartHeight, xOffset + mChartWidth, yOffset + mChartHeight,  mPaint);

        canvas.drawLine(xOffset + mQurterWidth, yOffset, xOffset + mQurterWidth, yOffset + mChartHeight, mPaint);
        canvas.drawLine(xOffset + mQurterWidth * 3, yOffset, xOffset + mQurterWidth * 3, yOffset + mChartHeight, mPaint);

        canvas.drawLine(xOffset, yOffset + mQuarterHeight, xOffset + mChartWidth, yOffset + mQuarterHeight, mPaint);
        canvas.drawLine(xOffset, yOffset + mQuarterHeight * 3, xOffset + mChartWidth, yOffset + mQuarterHeight * 3, mPaint);

        canvas.drawLine(xOffset, yOffset + mQuarterHeight * 2, xOffset + mQurterWidth, yOffset + mQuarterHeight * 2, mPaint);
        canvas.drawLine(xOffset + mQurterWidth * 3, yOffset + mQuarterHeight * 2, xOffset + mChartWidth, yOffset + mQuarterHeight * 2, mPaint);

        canvas.drawLine(xOffset + mQurterWidth * 2, yOffset, xOffset + mQurterWidth * 2, yOffset + mQuarterHeight, mPaint);
        canvas.drawLine(xOffset + mQurterWidth * 2, yOffset + mQuarterHeight * 3, xOffset + mQurterWidth * 2, yOffset + mChartHeight, mPaint);

        /*First House*/
        drawHouse(canvas, FIRST_STR[0], FIRST_STR[1], FIRST_STR[2], xOffset + mQurterWidth, yOffset, isAscendantHouse(1));

        /*Second House*/
        drawHouse(canvas, SECOND_STR[0], SECOND_STR[1], SECOND_STR[2], xOffset + mQurterWidth * 2, yOffset, isAscendantHouse(2));

        /*Third House*/
        drawHouse(canvas, THIRD_STR[0], THIRD_STR[1], THIRD_STR[2], xOffset + mQurterWidth * 3, yOffset, isAscendantHouse(3));

        /*Forth House*/
        drawHouse(canvas, FOURTH_STR[0], FOURTH_STR[1], FOURTH_STR[2], xOffset + mQuarterHeight * 3, yOffset + mQurterWidth, isAscendantHouse(4));

        /*Fifth House*/
        drawHouse(canvas, FIFTH_STR[0], FIFTH_STR[1], FIFTH_STR[2], xOffset + mQuarterHeight * 3, yOffset + mQurterWidth * 2, isAscendantHouse(5));

        /*Sixth House*/
        drawHouse(canvas, SIXTH_STR[0], SIXTH_STR[1], SIXTH_STR[2], xOffset + mQuarterHeight * 3, yOffset + mQurterWidth * 3, isAscendantHouse(6));

        /*Seventh House*/
        drawHouse(canvas, SEVENTH_STR[0], SEVENTH_STR[1], SEVENTH_STR[2], xOffset + mQuarterHeight * 2, yOffset + mQurterWidth * 3, isAscendantHouse(7));

        /*Eighth House*/
        drawHouse(canvas, EIGHTH_STR[0], EIGHTH_STR[1], EIGHTH_STR[2], xOffset + mQuarterHeight, yOffset + mQurterWidth * 3, isAscendantHouse(8));

        /*Ninth House*/
        drawHouse(canvas, NINTH_STR[0], NINTH_STR[1], NINTH_STR[2], xOffset, yOffset + mQurterWidth * 3, isAscendantHouse(9));

        /*Tenth House*/
        drawHouse(canvas, TENTH_STR[0], TENTH_STR[1], TENTH_STR[2], xOffset, yOffset + mQurterWidth * 2, isAscendantHouse(10));

        /*Eleventh House*/
        drawHouse(canvas, ELEVENTH_STR[0], ELEVENTH_STR[1], ELEVENTH_STR[2], xOffset, yOffset + mQurterWidth, isAscendantHouse(11));

        /*Twelfth House*/
        drawHouse(canvas, TWELFTH_STR[0], TWELFTH_STR[1], TWELFTH_STR[2], xOffset, yOffset, isAscendantHouse(12));

    }

    private boolean isAscendantHouse(int house)
    {
        return house == fix12(rashiArr[0]);
    }

    private void drawHouse(Canvas canvas, String topString, String middleString, String bottomString, float topX, float topY, boolean isAscendant)
    {
        float tHeight;

        mPaint.getTextBounds(topString, 0, topString.length(), topTextBounds);

        tHeight = topTextBounds.height();
        mTextSize = (int) (x10pcOffset / 3);
        mPaint.setTextSize(mTextSize);

        float  xCorrT = topX + mQurterWidth * .10f;
        float  yCorrT = topY + mQuarterHeight * .25f;

        float xCorrM = xCorrT;
        float yCorrM = yCorrT + tHeight + mQuarterHeight * .10f;

        float  xCorrB = xCorrT;
        float  yCorrB = yCorrM +  mQuarterHeight * .25f;

        if (bottomString.length() > 1)
        {
            canvas.drawText(bottomString,  xCorrB, yCorrB, mPaint);
        }

        if(middleString.length() > 1)
        {
            canvas.drawText(middleString, xCorrM, yCorrM, mPaint);
        }

        if (topString.length() > 1)
        {
            canvas.drawText(topString, xCorrT, yCorrT, mPaint);
        }


        if(isAscendant)
        {
            if(bottomString.length() > 1)
            {
                canvas.drawText(bottomString + "ASC",  xCorrB, yCorrB, mPaint);
            }
            else
            {
                mPaint.setColor(getResources().getColor(R.color.blue));
                canvas.drawText("ASC", xCorrB, yCorrB, mPaint);
                mPaint.setColor(getResources().getColor(R.color.black_overlay));
            }
        }

    }

    private void calculateDimensions() {
        xOffset = mParentWidth / 10;
        yOffset = mParentWidth / 20;

        mChartWidth = mParentWidth - 2 * xOffset;
        mChartHeight = mParentWidth - 2 * xOffset;

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

