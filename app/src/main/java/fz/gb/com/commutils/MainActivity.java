package fz.gb.com.commutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fz.gb.commutil.text.StringUtil;

/**
 * 测试验证activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         boolean b =  StringUtil.isEmpty("");

         assert (b == true);
    }
}
