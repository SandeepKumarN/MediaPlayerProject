package com.example.mediatest;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageButton btnPlay, btnFwd, btnBwd;

    ImageView img;
    SeekBar seekBar;
    Handler mHandler = new Handler();
    Runnable mRunnable;
    MediaPlayer mediaPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.imageView);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnFwd = (ImageButton) findViewById(R.id.btnFwd);
        btnBwd = (ImageButton) findViewById(R.id.btnBwd);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);

        btnPlay.setImageResource(R.drawable.play);
        btnBwd.setImageResource(R.drawable.rewind);
        btnFwd.setImageResource(R.drawable.forward);
        img.setImageResource(R.drawable.music);



        mediaPlayer = MediaPlayer.create(this, R.raw.song0);
        seekBar = findViewById(R.id.seekBar1);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    getAudioStats();
                    btnPlay.setImageResource(R.drawable.pause);
                    updateSeekBar();
                } else {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }

            }
        });

        seekBar.setOnTouchListener((v, event) -> {
            SeekBar s = (SeekBar) v;
            int position = (mediaPlayer.getDuration() / 100) * s.getProgress();
            mediaPlayer.seekTo(position);
            return false;
        });

        btnFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.getDuration() > mediaPlayer.getCurrentPosition() + 10000) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                    updateSeekBar();
                }

            }
        });
        btnBwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.getCurrentPosition() > 10000) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                    updateSeekBar();
                    getAudioStats();

                }
            }
        });
    }


    private void updateSeekBar(){
        if(mediaPlayer.isPlaying()){
            seekBar.setProgress((int)((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration()*100));
            mHandler.postDelayed(updater,1000);
        }
    }

     protected void getAudioStats(){
            int duration  = mediaPlayer.getDuration()/1000; // In milliseconds
            int due = (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition())/1000;

     }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };
    private void stopPlaying () {
        // If media player is not null then try to stop it
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Stop playing.", Toast.LENGTH_SHORT).show();
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

}


