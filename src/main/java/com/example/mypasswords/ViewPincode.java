package com.example.mypasswords;
/*Developed by Sheriff © */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kevalpatel.passcodeview.PinView;
import com.kevalpatel.passcodeview.authenticator.PasscodeViewPinAuthenticator;
import com.kevalpatel.passcodeview.indicators.CircleIndicator;
import com.kevalpatel.passcodeview.interfaces.AuthenticationListener;
import com.kevalpatel.passcodeview.keys.KeyNamesBuilder;
import com.kevalpatel.passcodeview.keys.RoundKey;

import java.util.Objects;

public class ViewPincode extends Activity {
    private static final String ARG_CURRENT_PIN = "current_pin";
    private PinView mPinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPinView = findViewById(R.id.pattern_view);

        //Pin
        final int[] correctPattern = new int[]{0, 0, 1, 3};
        mPinView.setPinAuthenticator(new PasscodeViewPinAuthenticator(correctPattern));

        mPinView.setKey(new RoundKey.Builder(mPinView)
                .setKeyPadding(R.dimen.key_padding)
                .setKeyStrokeColorResource(R.color.colorBackground)
                .setKeyStrokeWidth(R.dimen.key_stroke_width)
                .setKeyTextColorResource(R.color.colorBackground)
                .setKeyTextSize(R.dimen.key_text_size));

        mPinView.setIndicator(new CircleIndicator.Builder(mPinView)
                .setIndicatorRadius(R.dimen.indicator_radius)
                .setIndicatorFilledColorResource(R.color.colorBackground)
                .setIndicatorStrokeColorResource(R.color.colorBackground)
                .setIndicatorStrokeWidth(R.dimen.indicator_stroke_width));

        mPinView.setPinLength(PinView.DYNAMIC_PIN_LENGTH);

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyOne(this, R.string.key_1)
                .setKeyTwo(this, R.string.key_2)
                .setKeyThree(this, R.string.key_3)
                .setKeyFour(this, R.string.key_4)
                .setKeyFive(this, R.string.key_5)
                .setKeySix(this, R.string.key_6)
                .setKeySeven(this, R.string.key_7)
                .setKeyEight(this, R.string.key_8)
                .setKeyNine(this, R.string.key_9)
                .setKeyZero(this, R.string.key_0));

        mPinView.setTitle("");

        mPinView.setAuthenticationListener(new AuthenticationListener() {
            @Override
            public void onAuthenticationSuccessful() {
                Toast.makeText(getBaseContext(), R.string.granted, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewPincode.this, ViewPasswords.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                startActivity(new Intent(ViewPincode.this, ViewAccessDenied.class));
                Toast.makeText(getBaseContext(), R.string.denied, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntArray(ARG_CURRENT_PIN, mPinView.getCurrentTypedPin());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPinView.setCurrentTypedPin(Objects.requireNonNull(savedInstanceState.getIntArray(ARG_CURRENT_PIN)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}