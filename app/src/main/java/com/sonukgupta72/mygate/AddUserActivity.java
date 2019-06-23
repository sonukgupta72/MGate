package com.sonukgupta72.mygate;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

import static android.Manifest.*;
import static android.Manifest.permission.*;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AddUserActivity extends AppCompatActivity {

    EditText etName;
    Button btnCam, btnSubmit;
    ImageView imageView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int TAKE_PICTURE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 103;
    private Uri imageUri;

    String uniqId, filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etName = findViewById(R.id.editText);
        btnCam = findViewById(R.id.buttonCam);
        btnSubmit = findViewById(R.id.buttonSubmit);
        imageView = findViewById(R.id.imageView);


        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch camera intent and take photo

                launchCamera();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert data into db

                submitData();

            }
        });


    }

    /*
    * check cam permission
     * request for permission
     * launch new intent*/
    private void launchCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{CAMERA}, MY_CAMERA_REQUEST_CODE);

        } else {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }



//            imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(photoIntent, TAKE_PICTURE);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(imageBitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            //Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private Uri getOutputMediaFileUri(int mediaTypeImage)
    {
        //check for external storage
        if(isExternalStorageAvaiable())
        {
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

            fileName = "IMG_"+timeStamp;
            fileType = ".jpg";

            File mediaFile;
            try
            {
                mediaFile = File.createTempFile(fileName,fileType,mediaStorageDir);
//                Log.i("st","File: "+FileProvider.getUriForFile(AddUserActivity.this,
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        mediaFile));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.i("St","Error creating file: " + mediaStorageDir.getAbsolutePath() +fileName +fileType);
                return null;
            }
            return FileProvider.getUriForFile(AddUserActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider_paths",
                    mediaFile);

        }
        //something went wrong
        return null;

    }

    private boolean isExternalStorageAvaiable()
    {
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case TAKE_PICTURE:
//                if (resultCode == Activity.RESULT_OK) {
//
//                    Uri selectedImage = imageUri;
//                    getContentResolver().notifyChange(selectedImage, null);
//
//                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
//                    imageView.setVisibility(View.VISIBLE);
//                    ContentResolver cr = getContentResolver();
//                    Bitmap bitmap;
//                    try {
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//
//                        imageView.setImageBitmap(bitmap);
//                        Toast.makeText(this, selectedImage.toString(),
//                                Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
//                                .show();
//                        Log.e("Camera", e.toString());
//                    }
//                }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                launchCamera();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }//end onRequestPermissionsResult

    /*
    * generate 6 digit random code
    * insert data to db
    * return result ok
    * */
    private void submitData() {


        DatabaseHandler db = new DatabaseHandler(this);
        db.addUser(etName.getText().toString(), getRandomNumberString(), "");

        finishActivityWithResultOk();


    }

    private void finishActivityWithResultOk() {
        Intent data = new Intent();
        String text = "Result to be returned....";
        data.setData(Uri.parse(text));
        setResult(RESULT_OK, data);
        finish();

    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
