package feri.com.modulaudiorecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_CODE = 1001;
    String pathsave="";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button record,stoprecord,play,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record=(Button) findViewById(R.id.record);
        stoprecord=(Button) findViewById(R.id.stoprecord);
        play=(Button)findViewById(R.id.play);
        stop=(Button)findViewById(R.id.stop);
    }

    private void requestPermission() {
        String [] permission = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions(this,permission,REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private boolean checkpermissionfromdevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result== PackageManager.PERMISSION_GRANTED&&
                record_audio_result==PackageManager.PERMISSION_GRANTED;
    }

    public void record(View view) {
        if (checkpermissionfromdevice()){
            pathsave= Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/"
                    + UUID.randomUUID().toString()+"_audio_record.3gp";
            setupMediaRecord();
            try{
                mediaRecorder.prepare();
                mediaRecorder.start();
                record.setEnabled(false);
                play.setEnabled(false);
                stop.setEnabled(false);
            }catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(),"recording..",Toast.LENGTH_LONG).show();
        }else{
            requestPermission();
        }
    }

    private void setupMediaRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);
    }

    public void stopRecord(View view) {
        if (checkpermissionfromdevice()){
            mediaRecorder.stop();
            record.setEnabled(true);
            play.setEnabled(true);
            stop.setEnabled(true);
            Toast.makeText(getApplicationContext(),"stop recording..",Toast.LENGTH_LONG).show();
        }else{
            requestPermission();
        }
    }

    public void play(View view) {
        if (checkpermissionfromdevice()){
            mediaPlayer=new MediaPlayer();
            try {
                mediaPlayer.setDataSource(pathsave);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mediaPlayer!=null){
                mediaPlayer.start();
            }
        }else{
            requestPermission();
        }
    }

    public void stop(View view) {
        if (checkpermissionfromdevice()){
            mediaPlayer.stop();
            mediaPlayer.release();
            setupMediaRecord();
        }else{
            requestPermission();
        }
    }
}
