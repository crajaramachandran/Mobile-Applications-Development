package com.example.raja.questhunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{
    Button button;
    TextView XPTV;
    TextView levelTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.viewQuestListsButton);
        levelTV = (TextView) findViewById(R.id.MainLevel);
        XPTV = (TextView) findViewById(R.id.MainXP);
        animateStartScreen();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    public void animateStartScreen(){
        button.setVisibility(View.INVISIBLE);
        final Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        Animation leftRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.left_right);
        final Animation rightLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.right_left);
        levelTV.startAnimation(leftRight);
        XPTV.startAnimation(rightLeft);
        leftRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(fadeIn);
                button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_activity,
                R.anim.fade_out_activity);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
