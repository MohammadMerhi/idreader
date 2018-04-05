package com.example.merhi.idreader;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PINCodeActivity extends AppCompatActivity {
    final String PREF_NAME = "My Preferences";
    String pinCodeInput = "";
    int position = 0;
    String pin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        pin = (shared.getString("PIN CODE", ""));

    }

    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.Btn1:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "1";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn2:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "2";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn3:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "3";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn4:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "4";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn5:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "5";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn6:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "6";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn7:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "7";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn8:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "8";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn9:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "9";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
                    turnCircleOn(position);
                    waitToGoOrNo();
                }
                break;
            case R.id.Btn0:
                if (pinCodeInput.length() < 4) {
                    pinCodeInput = pinCodeInput + "0";
                    Log.e("msg:", pinCodeInput);
                    checkPinCode(pinCodeInput);
                    Log.d("SomeTag", checkPinCode(pinCodeInput) ? "true" : "false");
                    position++;
                    Log.d("MYINT", "value: " + position);
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
        if (position == 4 && checkPinCode(pinCodeInput) == true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent goToMain = new Intent(PINCodeActivity.this, MainActivity.class);
            startActivity(goToMain);
            finish();
        } else if (position == 4 && checkPinCode(pinCodeInput) == false) {
            pinCodeInput = "";
            position = 0;
            turnCircleOffAll();
            TextView msg = findViewById(R.id.enter_pin_msg);
            msg.setText(R.string.try_again);
        }
    }

}


