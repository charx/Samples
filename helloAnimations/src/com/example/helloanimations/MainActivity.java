package com.example.helloanimations;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ListView mList;
    private String[] toppings = { "Animation 1 - TIMER and MARGIN",
            "Animation 2 - LayoutChange (API 11)", "ANimation 3 - Custom Animation Class",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives", "Pepperoni", "Black Olives",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives", "Pepperoni", "Black Olives",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives", "Pepperoni", "Black Olives",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives", "Pepperoni", "Black Olives",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives", "Pepperoni", "Black Olives",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives", "Pepperoni", "Black Olives",
            "Pepperoni", "Black Olives", "Pepperoni", "Black Olives" };

    private TextView mTvFilterView;
    private boolean mIsTimerRunning = false;
    private final static int FRAMES_PER_SECOND = 60;
    private final static int SECOND = 1000;
    private final static int ANIMATION_DURATION = 300;
    private final static int ANIMATION_STEPS = (FRAMES_PER_SECOND * ANIMATION_DURATION) / SECOND;
    private android.widget.LinearLayout.LayoutParams llp;
    private CountDownTimer timer;
    private int mFilterViewHeight;
    private int mHeightStep = 0;
    private int mRestValue = 0;
    protected boolean mIsFilterViewGone = false;
    private LinearLayout mLL;
    public int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvFilterView = (TextView) findViewById(R.id.tv_view);
        mList = (ListView) findViewById(R.id.list);
        mLL = (LinearLayout) findViewById(R.id.ll_wrapper);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_item, toppings);
        mList.setAdapter(adapter);

        llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 240);

        timer = new CountDownTimer(SECOND, ANIMATION_STEPS) {

            public int mMarginToSet = 0;
            private int counter = 0;

            @Override
            public void onTick(long millisUntilFinished) {

                mMarginToSet = mMarginToSet + mHeightStep;

                if (mIsFilterViewGone) {
                    if (mMarginToSet < mFilterViewHeight - mRestValue) {
                        System.out.println("VIEW IS GONE: " + counter++);
                        llp.setMargins(0, -mFilterViewHeight + mMarginToSet, 0, 0);
                        mTvFilterView.setLayoutParams(llp);
                    } else if (mMarginToSet == mFilterViewHeight - mRestValue) {
                        System.out.println("counter: " + counter++);
                        llp.setMargins(0, 0, 0, 0);
                        System.out.println("CANCEL!: ");
                        mTvFilterView.setLayoutParams(llp);
                        mIsFilterViewGone = false;
                        mIsTimerRunning = false;
                        timer.cancel();
                    }
                } else {
                    if (mMarginToSet < mFilterViewHeight - mRestValue) {
                        System.out.println("VIEW IS VISIBLE margin: " + mMarginToSet);
                        llp.setMargins(0, -mMarginToSet, 0, 0);
                        mTvFilterView.setLayoutParams(llp);
                    } else if (mMarginToSet == mFilterViewHeight - mRestValue) {
                        System.out.println("counter: " + counter++);
                        System.out.println("CANCEL!: ");
                        llp.setMargins(0, -mMarginToSet - mRestValue, 0, 0);
                        mTvFilterView.setLayoutParams(llp);
                        mIsFilterViewGone = true;
                        timer.cancel();
                        mIsTimerRunning = false;
                    }
                }
            }

            @Override
            public void onFinish() {
                mMarginToSet = 0;
                mIsTimerRunning = false;
                System.out.println("mFilterViewHeight" + mFilterViewHeight);
                System.out.println("mHeightStep: " + mHeightStep);
                System.out.println("ANIMATION_STEPS: " + ANIMATION_STEPS);
                System.out.println("mRestValue: " + mRestValue);
            }
        };

        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == 0) {
                    mFilterViewHeight = mTvFilterView.getHeight();
                    mHeightStep = mFilterViewHeight / ANIMATION_STEPS;
                    mRestValue = mFilterViewHeight % (ANIMATION_STEPS);
                    if (!mIsTimerRunning) {
                        System.out.println("timer start");
                        mIsTimerRunning = true;
                        timer.start();
                    }
                } else if (arg2 == 1) {
                    if (mLL.getChildCount() == 1) {
                        mLL.addView(mTvFilterView, 0);
                    } else {
                        mLL.removeView(mTvFilterView);
                    }
                } else {
                    if (mTvFilterView.getVisibility() == View.VISIBLE) {
                        MyCustomAnimation a = new MyCustomAnimation(mTvFilterView, 300,
                                MyCustomAnimation.COLLAPSE);
                        height = a.getHeight();
                        mTvFilterView.startAnimation(a);
                    } else {
                        MyCustomAnimation a = new MyCustomAnimation(mTvFilterView, 300,
                                MyCustomAnimation.EXPAND);
                        a.setHeight(height);
                        mTvFilterView.startAnimation(a);
                    }
                }

            }
        });
    }
}
