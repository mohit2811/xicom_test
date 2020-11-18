package com.arora.developer.myapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DetailedActivity extends AppCompatActivity {
    EditText first_name, last_name, email, phone;
    String first_nameS, last_nameS, emailS, phones, url, ImgPath = "";
    ImageView image;

    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        url = getIntent().getStringExtra("url");
        image = findViewById(R.id.imageView);


        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                imageBitmap = bitmap;

                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        first_name = findViewById(R.id.firstname_edit);
        last_name = findViewById(R.id.lastname_edit);
        email = findViewById(R.id.email_edit);
        phone = findViewById(R.id.phone_edit);

    }

    public void finish(View view) {
        finish();
    }

    public void submit(View view) throws IOException {
        if (validateData()) {
            try {


                new uploadSelctedIMG(ImgPath).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(DetailedActivity.this, "please enter correct details", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateData() {
//Validating Data

        first_nameS = first_name.getText().toString();
        last_nameS = last_name.getText().toString();
        emailS = email.getText().toString();
        phones = phone.getText().toString();

        if (first_nameS.equals(""))
            return false;
        if (last_nameS.equals(""))
            return false;
        if (phones.length() < 10)
            return false;
        if (!isValidMail(emailS))
            return false;

        return true;
    }


    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    class uploadSelctedIMG extends AsyncTask<String, String, Boolean> {
        String ImgPath;

        public uploadSelctedIMG(String imgPath) {
            this.ImgPath = imgPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Boolean doInBackground(String... arg0) {
            try {

                //save bitmap to file

                File tempFile = new File(getApplicationContext().getCacheDir(), "image.jpg");

                tempFile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

                fileOutputStream.write(bitmapToByte());

                fileOutputStream.flush();

                fileOutputStream.close();

                //Upload data
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("first_name", first_nameS)
                        .addFormDataPart("last_name", last_nameS)
                        .addFormDataPart("email", emailS)
                        .addFormDataPart("phone", phones)
                        .addFormDataPart("user_image", tempFile.getAbsolutePath(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(tempFile.getAbsolutePath())))
                        .build();
                Request request = new Request.Builder()
                        .url("http://dev1.xicom.us/xttest/savedata.php")
                        .method("POST", body)
                        .build();
                Response response = null;

                response = client.newCall(request).execute();

                if (response.code() == 200)
                    return true;

                Log.d("upload image response ", response.body().string());

            } catch (IOException ex) {
                ex.printStackTrace();

                //  Toast.makeText(DetailedActivity.this, "mm"+ex , Toast.LENGTH_SHORT).show();

                Log.d("upload error ", ex.getMessage());


            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
                //if successful upload
            if (result)
                Toast.makeText(DetailedActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }

    }
    //change bitmap to byte
    public byte[] bitmapToByte() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();


        return imageBytes;
    }
}





