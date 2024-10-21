package com.ayoub.threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView textViewData;
    private ImageView imageView;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewData = findViewById(R.id.text_view_data);imageView = findViewById(R.id.image_view);
        Button buttonFetchData = findViewById(R.id.button_fetch_data);
        buttonFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUrl("https://api.myip.com");
                //downloadAndSetImage("https://randomfox.ca/images/122.jpg");
            }
        });
    }

    private void getDataFromUrl(String urlString) {
        executor.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                StringBuilder result = new StringBuilder();
                int data = in.read();
                while (data != -1) {
                    result.append((char) data);
                    data = in.read();
                }

                mainHandler.post(() -> {
                    textViewData.setText(result.toString());
                    Log.i("Data", result.toString());
                });

                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        });
    }

}
