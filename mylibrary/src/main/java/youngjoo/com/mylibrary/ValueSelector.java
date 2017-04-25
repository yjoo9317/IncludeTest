package youngjoo.com.mylibrary;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by samsung on 2017. 4. 25..
 */

public class ValueSelector extends RelativeLayout {
    private static final int REPEAT_INTERVAL_MS = 200;

    private View mRootView;
    private TextView mValueTextView;
    private EditText mValueEditText;
    private View mMinusButton, mPlusButton;
    private int mMinValue = Integer.MIN_VALUE;
    private int mMaxValue = Integer.MAX_VALUE;
    private boolean mPlusButtonPressed, mMinusButtonPressed;
    private Handler mHandler;

    public ValueSelector(Context context){
        super(context);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    public int getValue(){
        return Integer.valueOf(mValueEditText.getText().toString());
    }

    public void setValue(int newValue){
        int value = 0;
        if(newValue > mMaxValue)
            value = mMaxValue;
        else if(newValue < mMinValue)
            value = mMinValue;
        mValueEditText.setText(String.valueOf(value));
    }

    private void init(Context context){
        mHandler = new Handler();

        mRootView = inflate(context, R.layout.value_selector, this);
        mValueTextView = (TextView)mRootView.findViewById(R.id.value_selector_label);
        mValueEditText = (EditText)mRootView.findViewById(R.id.value_selector_edit_text);

        mMinusButton = (ImageView)mRootView.findViewById(R.id.value_selector_minus_button);
        mPlusButton = (ImageView)mRootView.findViewById(R.id.value_selector_plus_button);

        mMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue();
            }
        });

        mMinusButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mMinusButtonPressed = true;
                mHandler.post(new AutoDecrementer());
                return false;
            }
        });

        mMinusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if((event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL)){
                    mMinusButtonPressed = false;
                }
                return false;
            }
        });

        mPlusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue();
            }
        });

        mPlusButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPlusButtonPressed = true;
                mHandler.post(new AutoIncrementer());
                return false;
            }
        });
        mPlusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL){
                    mPlusButtonPressed = false;
                }
                return false;
            }
        });
    }
    private void incrementValue(){
        int current = Integer.parseInt(mValueEditText.getText().toString());
        if(current < mMaxValue)
            mValueEditText.setText(String.valueOf(current+1));
    }

    private void decrementValue(){
        int current = Integer.parseInt(mValueEditText.getText().toString());
        if(current > mMinValue)
            mValueEditText.setText(String.valueOf(current -1));
    }

    private class AutoIncrementer implements Runnable {
        @Override
        public void run(){
            if(mPlusButtonPressed){
                incrementValue();
                mHandler.postDelayed(new AutoIncrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }
    private class AutoDecrementer implements Runnable {
        @Override
        public void run(){
            if(mMinusButtonPressed){
                decrementValue();
                mHandler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }
}
