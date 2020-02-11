package com.ivn.lamejortienda.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ivn.lamejortienda.R;

import java.io.InputStream;
import java.net.URL;

public class PruebasActivity extends AppCompatActivity {

    ImageView foto;

    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);

        /*
        try {

            String createPersonUrl = "http://localhost:8082/spring-rest/createPerson";
            String updatePersonUrl = "http://localhost:8082/spring-rest/updatePerson";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject personJsonObject = new JSONObject();

            personJsonObject.put("id", 1);

            personJsonObject.put("name", listaMarcas.get(0).getBitmap());

        } catch (JSONException e) {
            e.printStackTrace();
        }

         */


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        foto = findViewById(R.id.ivFoto);
        //foto.setImageBitmap(listaMarcas.get(0).getBitmap());

/*
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();



        // Upload image
        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(imagenSeleccionada);
*/

        // Get img
        final Bitmap[] mIcon11 = {null};
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("/J.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                try {
                    InputStream in = new URL(uri.toString()).openStream();
                    mIcon11[0] = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.d("Error", e.getStackTrace().toString());

                }
            }
        });

        foto.setImageBitmap(mIcon11[0]);
    }
}

    /*
    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("timestamp", 1488873360);
                    jsonParam.put("uname", message.getUser());
                    jsonParam.put("message", message.getMessage());
                    jsonParam.put("latitude", 0D);
                    jsonParam.put("longitude", 0D);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

        */

        /*
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        String Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

}
*/