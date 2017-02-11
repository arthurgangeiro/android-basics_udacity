package br.com.arthurgrangeiro.cheese;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    short result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *
     * Retrieves the answers given by the user, checks if they are correct and shows on the screen.
     * @param view
     */
    public void checkForRightAnwser(View view){
        //is the RadioButton checked right? Questions 1 to 3
        RadioButton question1 = (RadioButton) findViewById(R.id.q1_answer);
        boolean answer1 = question1.isChecked();
        isRight(answer1);
        RadioButton question2 = (RadioButton) findViewById(R.id.q2_answer);
        boolean answer2 = question2.isChecked();
        isRight(answer2);
        RadioButton question3 = (RadioButton) findViewById(R.id.q3_answer);
        boolean answer3 = question3.isChecked();
        isRight(answer3);
        //Question 4 - Checkbox
        CheckBox question4a = (CheckBox) findViewById(R.id.q4_answer1);
        boolean answer4a = question4a.isChecked();
        CheckBox question4b = (CheckBox) findViewById(R.id.q4_answer2);
        boolean answer4b = question4b.isChecked();
        CheckBox question4c = (CheckBox) findViewById(R.id.q4_option1);
        boolean answer4c = question4c.isChecked();
        CheckBox question4d = (CheckBox) findViewById(R.id.q4_option1);
        boolean answer4d = question4c.isChecked();
        isRight((answer4a && answer4b)&&(!answer4c && !answer4d));
        //Question 5 - Text
        EditText answer5 = (EditText) findViewById(R.id.q5_answer);
        String textAnswer5 = answer5.getText().toString().trim();
        String rightAnswer1 = getResources().getString(R.string.q5_answer1);
        String rightAnswer2 = getResources().getString(R.string.q5_answer2);
        isRight((textAnswer5.equalsIgnoreCase(rightAnswer1) || textAnswer5.equalsIgnoreCase(rightAnswer2)));
        //Show the answer
        showTheScore();
        //Restart the result value
        result = 0;

    }

    /**
     *
     *Checks if the answer is true, if yes, increase the result by one.
     * @param answer
     */
    private void isRight(boolean answer){
        if(answer) {
            result += 1;
        }
    }

    /**
     * Display result on screen
     */
    private void showTheScore(){
        String feedback = "";
        if(result<3){
            feedback = getResources().getString(R.string.not_good);
        }else if(result<5){
            feedback = getResources().getString(R.string.good);
        }else {
            feedback = getResources().getString(R.string.excellent);
        }
        String text = feedback +" "+ String.format(getResources().getString(R.string.user_score), (String.valueOf(result)));
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
