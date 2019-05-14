package com.mind.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MindScoreView extends FrameLayout{
    TextView tvSafeScore;

    public MindScoreView(Context context) {
        super(context);
        initView();
    }

    public MindScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MindScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_mind_score, this, false);
        addView(v);

        tvSafeScore = v.findViewById(R.id.safe_score);

    }


    public void setScore(int score){
        tvSafeScore.setText(String.valueOf(score));

    }


}
