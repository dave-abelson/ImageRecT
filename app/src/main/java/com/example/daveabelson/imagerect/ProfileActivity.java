package com.example.daveabelson.imagerect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    //UI
    private ImageView picture;
    private Button takePicture;
    private Button submitPicture;

    //Picture
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri mPictureLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Set up UI
        picture = (ImageView)findViewById(R.id.profileImage);
        takePicture = (Button)findViewById(R.id.profileSnap);
        submitPicture = (Button)findViewById(R.id.profileSubmit);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setup picture taking poriton
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mPictureLocation = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                System.out.println(mPictureLocation.toString());

                i.putExtra(MediaStore.EXTRA_OUTPUT, mPictureLocation); // set the image file name

                // start the image capture Intent
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });

        submitPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 Register user into database
                 */

                //Grab picture and convert to bytes
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Drawable d = Drawable.createFromPath(mPictureLocation.getPath());
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

                //Check if bitmap is null
                if(bitmap == null){
                    Toast.makeText(ProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Compress bitmap
                Bitmap b = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()*3/4, bitmap.getHeight()*3/4, false);
                b.compress(Bitmap.CompressFormat.JPEG, 0, stream);

                // Grab bytes
                byte[] arr = stream.toByteArray();
                System.out.println(arr.length);

                //Send Parse user info
                ParseUser p = new ParseUser();
                p.setUsername(getIntent().getStringExtra("user"));
                p.setPassword(getIntent().getStringExtra("pass"));
                p.setEmail(getIntent().getStringExtra("email"));
                p.put("propic", arr);
                p.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //User account was created
                            Toast.makeText(ProfileActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, e.getMessage() + e.getCode(), Toast.LENGTH_LONG).show();
                            //Something went wrong
                            //i.e. username taken,
                            //blank password, etc.
                            //Check e.getCode() and e.getMessage() and inform the user
                        }
                    }
                });
            }
        });
    }

    //Process pictures taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        mPictureLocation, Toast.LENGTH_SHORT).show();

                //Grab image and display it
                Drawable d = Drawable.createFromPath(mPictureLocation.getPath());
                picture.setImageDrawable(d);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
}
