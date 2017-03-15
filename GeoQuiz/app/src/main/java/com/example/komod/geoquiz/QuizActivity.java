package com.example.komod.geoquiz;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ISCHEATER = "cheater";

    private TextView mQuestionTextView;

    private static final TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_tainan, true),
            new TrueFalse(R.string.question_japan, false),
            new TrueFalse(R.string.question_korea, true),
            new TrueFalse(R.string.question_china, false),
    };

    private int mCurrentIndex = 0;
    private boolean[] mIsCheater = new boolean[mQuestionBank.length];

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestionText();
    }
    private void updateQuestionText() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getQuestion());
    }

    private void checkAnswer(boolean userAnswer) {
        int msgId;
        if (mIsCheater[mCurrentIndex]) {
            msgId = R.string.judgment_toast;
        } else {
            if (userAnswer == mQuestionBank[mCurrentIndex].isTrueQuestion()) {
                msgId = R.string.correct_toast;
            } else {
                msgId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(QuizActivity.this, msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            for(int i = 0; i < mIsCheater.length; ++i) {
                mIsCheater[i] = savedInstanceState.getBoolean(KEY_ISCHEATER + "_" + i, false);
            }

        } else {
            mCurrentIndex = 0;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(R.string.subtitle);
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        updateQuestionText();
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        findViewById(R.id.true_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        findViewById(R.id.false_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        findViewById(R.id.previous_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
                updateQuestionText();
            }
        });
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        findViewById(R.id.cheat_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, mQuestionBank[mCurrentIndex].isTrueQuestion());
                startActivityForResult(i, 0);
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
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        for(int i = 0; i < mIsCheater.length; ++i) {
            savedInstanceState.putBoolean(KEY_ISCHEATER + "_" + i, mIsCheater[i]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, request: " + requestCode + ", result: " + resultCode);
        // Directly use result code instead of setting extra data
        if (resultCode == RESULT_OK) {
            mIsCheater[mCurrentIndex] = true;
        }
    }
}
