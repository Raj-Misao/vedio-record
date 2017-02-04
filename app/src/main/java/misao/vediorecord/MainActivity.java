package misao.vediorecord;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Request code for recording video
    private static final int RECORD_VIDEO_REQUEST_CODE = 100;

    //CHECK THE MEDIA TYPE IS VIDEO
    private static final int MEDIA_TYPE_VIDEO = 1;

    //Directory name in external storage
    private static final String VIDEO_DIRECTORY = "MyVideo";

    //Uri for get the path
    private Uri fileUri;

    Button btn_video;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_video = (Button)findViewById(R.id.btn_video);
        videoView = (VideoView)findViewById(R.id.video);

        btn_video.setOnClickListener(this);

        if(!isDeviceSupportCamera())
        {
            Toast.makeText(getApplicationContext(),"Sorry! your device is not support the camera", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean isDeviceSupportCamera() {
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        recordVideo();
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, RECORD_VIDEO_REQUEST_CODE);
    }

    //this is get the image and convert in uri
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //this is get the image from external storage directory
    private File getOutputMediaFile(int type) {
        //External Sd card location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),VIDEO_DIRECTORY);

        //it's create the directory if your directory is not available
        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs())
            {
                Log.d(VIDEO_DIRECTORY,"oops..Failed Create"+VIDEO_DIRECTORY+"directory");
                return null;
            }
        }

        //Create a file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;

        if(type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath()+ File.separator+"IMG_"+timeStamp+".jpg");
        }
        else
        {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RECORD_VIDEO_REQUEST_CODE && resultCode == RESULT_OK)
        {
            previewVideo();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Sorry for not record the video",Toast.LENGTH_LONG).show();
        }
    }

    private void previewVideo() {

        videoView.setVideoPath(fileUri.getPath());
        videoView.start();
    }
}
