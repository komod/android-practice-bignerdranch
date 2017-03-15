package com.example.komod.geoquiz;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CheatActivity extends Activity {
    private static final String TAG = "CheatActivity";
    public static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.komod.geoquiz.answer_is_true";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_CHEATED = "cheated";

    private boolean mAnswerIsTrue;
    private boolean mCheated;

    private void showCheatAnswer() {
        TextView answerTextView = (TextView)findViewById(R.id.answer_text_view);
        if (mAnswerIsTrue) {
            answerTextView.setText(R.string.true_button);
        } else {
            answerTextView.setText(R.string.false_button);
        }
        setResult(RESULT_OK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER, false);
            mCheated = savedInstanceState.getBoolean(KEY_CHEATED, false);
            if (mCheated) {
                showCheatAnswer();
            }
        } else {
            mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
            mCheated = false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setSubtitle(R.string.subtitle);
            }
        }

        findViewById(R.id.show_answer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheated = true;
                showCheatAnswer();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_CHEATED, mCheated);
    }

}
