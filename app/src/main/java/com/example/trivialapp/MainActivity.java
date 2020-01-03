package com.example.trivialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivialapp.data.AnswerListAnsyncResponse;
import com.example.trivialapp.data.QuestionBank;
import com.example.trivialapp.modle.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextview;
    private TextView questionCounter;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton previosButton;
    private int currentQuestionIndex=0;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionCounter=findViewById(R.id.counter_text);;
        questionTextview=findViewById(R.id.question_textview);
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        nextButton=findViewById(R.id.next_button);
        previosButton=findViewById(R.id.previous_button);


        previosButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);


        questionList= new QuestionBank().getQuestions(new AnswerListAnsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText(currentQuestionIndex + "/" +questionList.size());
                //Log.d("Main","onCreate: "+questionArrayList);

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous_button:
                if (currentQuestionIndex > 0) {


                    currentQuestionIndex = ( currentQuestionIndex - 1 ) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
                currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();

                break;
            case R.id.true_button:

                checkAnswer(true);
                updateQuestion();
                break;
        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue;
        answerIsTrue=questionList.get(currentQuestionIndex).isAnswerTrue();
        int   toastMsgId=0;
        if(userChooseCorrect==answerIsTrue){
            fadeView();
            toastMsgId=R.string.correct_answer;
            updateQuestion();
            currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();

        }else {
            shakeAnimation();
            toastMsgId=R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this,toastMsgId,Toast.LENGTH_SHORT).show();

    }

    private void updateQuestion() {
        String question=questionList.get(currentQuestionIndex).getAnswer();
        questionTextview.setText(question);
        questionCounter.setText(currentQuestionIndex + "/" +questionList.size());




    }
    private void fadeView(){
        final CardView cardView=findViewById(R.id.cardView);
        final AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //alphaAnimation.setBackgroundColor(Color.GREEN);
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_the_quetion);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
