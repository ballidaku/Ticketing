package example.com.ballidaku.frontScreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import example.com.ballidaku.MainActivity;
import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.MySharedPreference;


public class Splash extends AppCompatActivity
{

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        countDownTimer.start();
    }

    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000)
    {

        public void onTick(long millisUntilFinished)
        {

        }

        public void onFinish()
        {
            if (MySharedPreference.getInstance().getUserID(context).isEmpty())
            {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
            else
            {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        }
    };
}
