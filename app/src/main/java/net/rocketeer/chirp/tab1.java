package net.rocketeer.chirp;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab1 extends Fragment {

    public tab1() {
        // Required empty public constructor
    }

    private Handler h = new Handler();
    private AnimationDrawable animation;
    private int currentAmplitude;
    private ImageView image;
    private MediaRecorder recorder;
    private ImageButton button;
    private Chronometer chronometer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        image = (ImageView) view.findViewById(R.id.img);
        image.setBackgroundResource(R.drawable.animationidle);
        animation = (AnimationDrawable) image.getBackground();

        chronometer = (Chronometer) view.findViewById(R.id.chronometerdisplay);

        button = (ImageButton) view.findViewById(R.id.button);

        button.setOnTouchListener(new View.OnTouchListener() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    image.setBackgroundResource(R.drawable.animationlisten);
                    animation = (AnimationDrawable) image.getBackground();
                    animation.start();
                    chronometer.start();
                    h.postDelayed(this, 200);
                    getAmp();
                    if (currentAmplitude > 20000) {
                        image.setBackgroundResource(R.drawable.animationloud);
                        animation = (AnimationDrawable) image.getBackground();
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recordAudio();
                    h.post(runnable);
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    h.removeCallbacks(runnable);
                    image.setBackgroundResource(R.drawable.animationdone);
                    animation = (AnimationDrawable) image.getBackground();
                    chronometer.stop();
                    stopRecording();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        animation.start();
    }

    private void recordAudio() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // creates a file in the external storage
            File chirpDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator +"chirp");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US);
            Date currentTime = new Date();
            String fileName = formatter.format(currentTime) + ".3gp";

            recorder.setOutputFile(chirpDir + File.separator + fileName);
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getAmp() {
        currentAmplitude = this.recorder.getMaxAmplitude();
        return currentAmplitude;
    }

    private void stopRecording() {
        if (null != recorder) {
            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            } catch (RuntimeException ex) {
                ex.getMessage();
            }
        }
    }
}
