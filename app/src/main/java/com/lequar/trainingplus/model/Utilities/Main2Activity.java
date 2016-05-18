package com.lequar.trainingplus.model.Utilities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import com.lequar.trainingplus.R;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener  {

    private TextView messageText;
    private Button uploadButton, btnselectpic;
    private EditText etxtUpload;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        uploadButton = (Button)findViewById(R.id.uploadButton);
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imageView_pic);
        etxtUpload = (EditText)findViewById(R.id.etxtUpload);

        btnselectpic.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        jsonObject = new JSONObject();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_selectpic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Utils.REQCODE);
                break;
            case R.id.uploadButton:
                Bitmap image = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                dialog.show();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                try {
                    jsonObject.put(Utils.imageName, etxtUpload.getText().toString().trim());
                    Log.e("Image name", etxtUpload.getText().toString().trim());
                    jsonObject.put(Utils.image, encodedImage);
                } catch (JSONException e) {
                    Log.e("JSONObject Here", e.toString());
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Utils.urlUpload, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.e("Message from server", jsonObject.toString());
                                dialog.dismiss();
                                messageText.setText("Image Uploaded Successfully");
                                Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Message from server", volleyError.toString());
                        dialog.dismiss();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(this).add(jsonObjectRequest);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.REQCODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
    }
}