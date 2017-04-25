package youngjoo.com.mylibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by samsung on 2017. 4. 20..
 */

public class MyLib extends LinearLayout {
    View mRootView;
    TextView mTextView;

    public MyLib(Context context){
        super(context);
        init(context);
    }

    public MyLib(Context context, AttributeSet attributeSet){

        super(context, attributeSet);
        init(context);
    }

    public void init(Context context){
        mRootView = inflate(context, R.layout.library_layout, this );
        mTextView = (TextView)mRootView.findViewById(R.id.mylib_text);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextView(String text) {
        mTextView.setText(text);
    }
}
