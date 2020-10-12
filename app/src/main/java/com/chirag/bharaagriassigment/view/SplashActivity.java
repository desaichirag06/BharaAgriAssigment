package com.chirag.bharaagriassigment.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chirag.bharaagriassigment.R;
import com.chirag.bharaagriassigment.databinding.ActivitySplashBinding;
import com.chirag.bharaagriassigment.view.home.MainActivity;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding mBinding;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.ivSplash.startAnimation(animation);
        mBinding.tvAppName.startAnimation(animation);
    }
}