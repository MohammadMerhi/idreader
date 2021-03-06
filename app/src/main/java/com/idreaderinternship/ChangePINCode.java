package com.idreaderinternship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePINCode extends AppCompatActivity {
    final String PREF_NAME = "My Preferences";
    String pinCodeInput = "";
    int position = 0, time = 2;
    String pin = "", var1 = "", var2 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pincode);
    }

    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.Btn1:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "1";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn2:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "2";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn3:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "3";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn4:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "4";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn5:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "5";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn6:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "6";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn7:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "7";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn8:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "8";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn9:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "9";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn0:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "0";
                    position++;
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn_backspace:
                if (pinCodeInput.length() > 0) {
                    char[] pA = pinCodeInput.toCharArray();
                    pinCodeInput = "";
                    for (int i = 0; i < pA.length - 1; i++) {
                        pinCodeInput = pinCodeInput + pA[i];
                    }
                    Log.e("msg:", pinCodeInput);
                    position--;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOff(position);
                } else if (pinCodeInput.length() == 0) {
                    pinCodeInput = "";
                    Log.e("msg:", pinCodeInput);
                }

                break;
            default:
                break;
        }

    }

    public boolean checkPinCode(String pinCode) {
        if (pinCode.equals(pin)) {
            return true;
        } else {
            return false;
        }
    }

    public void turnCircleOn(int circleNb) {
        switch (circleNb) {
            case 1:
                ImageView circleOne = findViewById(R.id.circle_one);
                circleOne.setImageResource(R.drawable.circle_2);
                break;
            case 2:
                ImageView circleTwo = findViewById(R.id.circle_two);
                circleTwo.setImageResource(R.drawable.circle_2);
                break;
            case 3:
                ImageView circleThree = findViewById(R.id.circle_three);
                circleThree.setImageResource(R.drawable.circle_2);
                break;
            case 4:
                ImageView circleFour = findViewById(R.id.circle_four);
                circleFour.setImageResource(R.drawable.circle_2);
                break;
        }
    }

    public void turnCircleOff(int circleNb) {
        switch (circleNb) {
            case 0:
                ImageView circleOne = findViewById(R.id.circle_one);
                circleOne.setImageResource(R.drawable.circle_1);
                break;
            case 1:
                ImageView circleTwo = findViewById(R.id.circle_two);
                circleTwo.setImageResource(R.drawable.circle_1);
                break;
            case 2:
                ImageView circleThree = findViewById(R.id.circle_three);
                circleThree.setImageResource(R.drawable.circle_1);
                break;
            case 3:
                ImageView circleFour = findViewById(R.id.circle_four);
                circleFour.setImageResource(R.drawable.circle_1);
                break;
        }
    }

    public void turnCircleOffAll() {

        ImageView circleOne = findViewById(R.id.circle_one);
        circleOne.setImageResource(R.drawable.circle_1);

        ImageView circleTwo = findViewById(R.id.circle_two);
        circleTwo.setImageResource(R.drawable.circle_1);

        ImageView circleThree = findViewById(R.id.circle_three);
        circleThree.setImageResource(R.drawable.circle_1);

        ImageView circleFour = findViewById(R.id.circle_four);
        circleFour.setImageResource(R.drawable.circle_1);

    }

    public void waitToGoOrNo() {
        if (position == 4 && time == 0) {
            var1 = pinCodeInput;
            pinCodeInput = "";
            position = 0;
            time = 1;
            turnCircleOffAll();
            TextView msg = findViewById(R.id.enter_pin_msg);
            msg.setText(R.string.re_enter_your_new_pin);
        } else if (position == 4 && time == 1) {
            var2 = pinCodeInput;
            if (var1.equals(var2)) {

                SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("PIN CODE", var2);
                editor.apply();

                // commit is important here.

                Intent goToMainActivity = new Intent(ChangePINCode.this, Settings.class);
                startActivity(goToMainActivity);
                finish();
            } else {
                Toast.makeText(this, "Different PIN Code\nTry Again", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        } else if (position == 4 && time == 2) {

            SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String oldPin = (shared.getString("PIN CODE", ""));

            if (pinCodeInput.equals(oldPin)) {
                pinCodeInput = "";
                position = 0;
                turnCircleOffAll();
                time = 0;
                TextView msg = findViewById(R.id.enter_pin_msg);
                msg.setText(R.string.enter_your_new_pin);
            } else {
                Toast.makeText(this, "Wrong PIN Code\nTry Again", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, PINCodeActivity.class);
        intent.putExtra("activityName", "ChangePINCode");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
