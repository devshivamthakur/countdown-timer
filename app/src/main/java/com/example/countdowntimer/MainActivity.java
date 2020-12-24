package com.example.countdowntimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
Button start_timing,pause_resume_btn;
TextView set_time,all_set_time;
EditText minutes,second,hour;
    CountDownTimer countDownTimer;
    static long last_time;
    MediaPlayer tic_mediaPlayer;
    MediaPlayer final_mediaPlayer;
    static  boolean flag;
    static  boolean flag1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       init();
        start_timing.setOnClickListener(v -> {   // start timing
          start_timing_method();
        });
        pause_resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resume_pause();
            }
        });

    }

    private void start_timing_method() {
        String min="0";
        String sec="0";
        String h="0";
        try {
            min=minutes.getText().toString();  // minute from user
            sec= second.getText().toString();  // second
            h= hour.getText().toString();       // hours
            Toast.makeText(getApplicationContext(),String.valueOf(h),Toast.LENGTH_SHORT).show();
            if(min.isEmpty()){
                min="0";
            } if(sec.isEmpty()){
                sec="0";
            } if(h.isEmpty()){
                h="0";
            }

        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        if(!min.equals("0")||!sec.equals("0")||!h.equals("0")){

            try {
                int m=Integer.parseInt(min);
                int hrs=Integer.parseInt(h);
                int time=hrs*60*60+m*60+Integer.parseInt(sec);
                Toast.makeText(getApplicationContext(),String.valueOf(time),Toast.LENGTH_SHORT).show();
                if(countDownTimer!=null){ // if timer already running and want new timer run than first old time will be canceled and new timer will be run
                    countDownTimer.cancel();
                }
                start_t(time);
            }catch (Exception e){

            }
        }

    }

    private void init(){
    start_timing=findViewById(R.id.start_timer);
    set_time=findViewById(R.id.set_timing);
    minutes=findViewById(R.id.minutes);
    second=findViewById(R.id.second);
    hour=findViewById(R.id.hours);
    all_set_time=findViewById(R.id.set_time);
    pause_resume_btn=findViewById(R.id.pause_resume);

    getSupportActionBar().setTitle("Timer");
}
    private void resume_pause() {
        if(countDownTimer!=null){

            if(pause_resume_btn.getText().equals(getResources().getString(R.string.pause))){
                countDownTimer.cancel();
                if(tic_mediaPlayer!=null){
                    tic_mediaPlayer.stop();
                    flag=false;
                }
                Toast.makeText(getApplicationContext(),"Timer Paused",Toast.LENGTH_SHORT).show();
                pause_resume_btn.setText(getResources().getString(R.string.resume));
            }else if(pause_resume_btn.getText().equals(getResources().getString(R.string.resume))){
                start_t((int) (last_time));
                if(tic_mediaPlayer!=null){
//                    tic_mediaPlayer.stop();
//                    tic_mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.tic);
//                    tic_mediaPlayer.start();
                    flag=true;
                }
                Toast.makeText(getApplicationContext(),"Timer Resumed",Toast.LENGTH_SHORT).show();
                pause_resume_btn.setText(getResources().getString(R.string.pause));
            }
            Toast.makeText(getApplicationContext(),"last time"+last_time,Toast.LENGTH_SHORT).show();
        }
    }

    private void start_t(int time)
    {
        pause_resume_btn.setText(getResources().getString(R.string.pause));
        if(tic_mediaPlayer!=null){ // this will work on whenever user entered many time , start timer.
            if(tic_mediaPlayer.isPlaying()){
                tic_mediaPlayer.stop();
                tic_mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.tic);
                tic_mediaPlayer.start();
                tic_mediaPlayer.setLooping(true);
                Log.e("times not null", "start_t: " );
            }else {
                tic_mediaPlayer.release();
                tic_mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.tic);
                tic_mediaPlayer.start();
                tic_mediaPlayer.setLooping(true);
            }
        }else if(tic_mediaPlayer==null){
            tic_mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.tic); //it  will play first time
            tic_mediaPlayer.start();
            tic_mediaPlayer.setLooping(true);
            Log.e("times", "start_t: " );
        }
        if(final_mediaPlayer!=null&&final_mediaPlayer.isPlaying()){
            final_mediaPlayer.stop();
        }

      countDownTimer= new CountDownTimer(time*1000,1000){

        @Override
        public void onTick(long millisUntilFinished) {
            last_time=millisUntilFinished/1000;
            int mi= (int) (millisUntilFinished/1000);
          all_set_time.setText(format(mi));
          flag=true;
        }
        @Override
        public void onFinish() {
            flag=false;
            set_time.setText(getResources().getString(R.string.done));
             final_mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.bgm);
            final_mediaPlayer.start();
            final_mediaPlayer.setLooping(true);
        }
    };
       countDownTimer.start();
    }
    private String  format(int millisUntilFinished){
        String hours;
        int cal=millisUntilFinished/60;
        String  min= String.valueOf(cal%60); // to get minutes
        String  sec= String.valueOf(millisUntilFinished%60); // to get second
        hours= String.valueOf(cal/60);
        if(min.length()<2){
            min="0"+min;
        } if(sec.length()<2){
            sec="0"+sec;
        }
        if(hours.length()<2){
            hours="0"+hours;
        }
        return hours+":"+min+":"+sec;
    }
      long back_prssed;
    @Override
    public void onBackPressed() {
        if(back_prssed+1000>System.currentTimeMillis()){
            super.onBackPressed();
        }else {
            Toast.makeText(getApplicationContext(),"press back again to exit",Toast.LENGTH_SHORT).show();
        }
        back_prssed=System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        Log.e("life_cycle", "onPause: " );
        if(tic_mediaPlayer!=null&&tic_mediaPlayer.isPlaying()){
            flag=false;
            tic_mediaPlayer.pause();
            tic_mediaPlayer.release();
        }
        super.onPause();
    }

//    @Override
//    protected void onStop() {
//        Log.e("life_cycle", "onStop: ");
//        mediaPlayer.stop();
//        super.onStop();
//    }

//    @Override
//    protected void onStart() {
//        Log.e("life_cycle" , "onStart: " );
//        Log.e("life_cycle", String.valueOf(flag));
//        if(flag){
//            mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.tic);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.start();
//        }
//        super.onStart();
//    }


}