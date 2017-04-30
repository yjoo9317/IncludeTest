package youngjoo.com.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yjoo9_000 on 2017-04-29.
 */

public class ValueBar extends View {

    private int mMaxValue = 100;
    private int mCurrentValue = 0;

    private int mBarHeight;
    private int mCircleRadius;
    private int mSpaceAfterBar;
    private int mMaxValueTextSize;
    private int mTextSizeInCircle;
    private int mLabelTextSize;
    private int mLabelTextColor;
    private int mCurrentValueTextColor;
    private int mTextColorInCircle;
    private int mBaseColor;
    private int mFillColor;

    private String mLabelText;

    private Paint mLabelPaint;
    private Paint mMaxValuePaint;
    private Paint mBarBasePaint;
    private Paint mBarFillPaint;
    private Paint mCirclePaint;
    private Paint mValueInCirclePaint;

    public ValueBar(Context context){
        super(context);
    }

    public ValueBar(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public void init(Context context, AttributeSet attributeSet){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ValueBar, 0, 0);

        mBarHeight = ta.getDimensionPixelSize(R.styleable.ValueBar_barHeight, 0);
        mCircleRadius = ta.getDimensionPixelSize(R.styleable.ValueBar_circleRadius, 0);
        mSpaceAfterBar = ta.getDimensionPixelSize(R.styleable.ValueBar_spaceAfterBar, 0);
        mMaxValueTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_maxValueTextSize, 20);
        mTextSizeInCircle = ta.getDimensionPixelSize(R.styleable.ValueBar_textSizeInCircle, 10);
        mLabelTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_labelTextSize, 10);
        mLabelTextColor = ta.getColor(R.styleable.ValueBar_labelTextColor, Color.BLACK);
        mCurrentValueTextColor = ta.getColor(R.styleable.ValueBar_maxValueTextColor, Color.BLACK);
        mTextColorInCircle = ta.getColor(R.styleable.ValueBar_textColorInCircle, Color.WHITE);
        mBaseColor = ta.getColor(R.styleable.ValueBar_baseColor, Color.BLACK);
        mFillColor = ta.getColor(R.styleable.ValueBar_fillColor, Color.BLACK);
        mLabelText = ta.getString(R.styleable.ValueBar_labelText);
        ta.recycle();

        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setTextSize(mLabelTextSize);
        mLabelPaint.setColor(mLabelTextColor);
        mLabelPaint.setTextAlign(Paint.Align.LEFT);

        mMaxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaxValuePaint.setTextSize(mMaxValueTextSize);
        mMaxValuePaint.setColor(mLabelTextColor);
        mMaxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mMaxValuePaint.setTextAlign(Paint.Align.RIGHT);


        mBarBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarBasePaint.setColor(mBaseColor);

        mBarFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarFillPaint.setColor(mFillColor);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mFillColor);

        mValueInCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValueInCirclePaint.setColor(mTextColorInCircle);
        mValueInCirclePaint.setTextSize(mTextSizeInCircle);
        mValueInCirclePaint.setTextAlign(Paint.Align.CENTER);
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
        invalidate();
        requestLayout();
    }

    public void setValue(int value){
        if(value < 0) value = 0;
        else if(value > mMaxValue) value = mMaxValue;
        mCurrentValue = value;
        invalidate();
    }

    private int measureHeight(int measureSpec){
        int size = getPaddingTop()+getPaddingBottom();
        size += mLabelPaint.getFontSpacing();
        float maxValueSpacing = mMaxValuePaint.getFontSpacing();
        size += Math.max(maxValueSpacing, Math.max(mBarHeight, mCircleRadius*2));
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec){
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        mLabelPaint.getTextBounds(mLabelText, 0, mLabelText.length(), bounds);
        size += bounds.width();

        bounds = new Rect();
        String maxValue = String.valueOf(mMaxValue);
        mMaxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), bounds);
        size += bounds.width();
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private float getBarCenter(){
        float barCenter = (getHeight() -getPaddingTop() -getPaddingBottom()) /2;
        barCenter += getPaddingTop() + 0.1f * getHeight();
        return barCenter;
    }

    private void drawLabel(Canvas canvas){
        float x = getPaddingLeft();
        Rect bounds = new Rect();
        mLabelPaint.getTextBounds(mLabelText, 0, mLabelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();
        canvas.drawText(mLabelText, 0, mLabelText.length(), x, y, mLabelPaint);
    }

    private void drawMaxValue(Canvas canvas){
        String maxValue = String.valueOf(mMaxValue);
        Rect bounds = new Rect();
        mMaxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), bounds);

        // since the text will drawn from right to left since alignment is RIGHT.
        float x = getWidth() - getPaddingRight();

        // the text should align with the vertical center of the bar
        float y = getBarCenter() + bounds.height()/2;
        canvas.drawText(maxValue, 0, maxValue.length(), mMaxValuePaint);
    }

    private void drawBar(Canvas canvas){
        String maxValue = String.valueOf(mMaxValue);
        Rect bounds = new Rect();
        mMaxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), bounds);
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - mCircleRadius - bounds.width() - mSpaceAfterBar;

        float barCenter = getBarCenter();
        float halfBarHeight = mBarHeight /2;
        float top = barCenter - halfBarHeight;
        float bottom = barCenter + halfBarHeight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;

        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, halfBarHeight, halfBarHeight, mBarBasePaint);

        float percentFilled = (float) mCurrentValue / (float) mMaxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;
        RectF fillRect = new RectF(left, top, fillPosition, bottom);
        canvas.drawRoundRect(fillRect, halfBarHeight, halfBarHeight, mBarFillPaint);

        canvas.drawCircle(fillPosition, barCenter, mCircleRadius, mCirclePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

}
