package youngjoo.com.includetest;

import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import youngjoo.com.mylibrary.MyLib;
import youngjoo.com.mylibrary.ValueSelector;

public class MainActivity extends AppCompatActivity {

    private MyLib mMyLib;
    private ValueSelector mValueSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyLib = (MyLib) findViewById(R.id.mylib_view);
        mMyLib.setTextView("MyLib inflated on main_activity");

        mValueSelector = (ValueSelector)findViewById(R.id.value_selector);
        mValueSelector.setValue(0);
        mValueSelector.setMaxValue(100);
        mValueSelector.setMinValue(0);
    }

}
