package com.example.lkdeneme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        fillScreen();
    }

    public void fillScreen() {
        TextView name = findViewById(R.id.textName);
        String message = getIntent().getStringExtra("name");
        name.setText(message);

        TextView status = findViewById(R.id.status);
        message = getIntent().getStringExtra("status");
        status.setText(message);

        TextView specy = findViewById(R.id.specy);
        message = getIntent().getStringExtra("specy");
        specy.setText(message);

        TextView gender = findViewById(R.id.gender);
        message = getIntent().getStringExtra("gender");
        gender.setText(message);

        TextView origin = findViewById(R.id.origin);
        message = getIntent().getStringExtra("origin");
        origin.setText(message);

        TextView location = findViewById(R.id.location);
        message = getIntent().getStringExtra("location");
        location.setText(message);

        TextView episodes = findViewById(R.id.episodes);
        message = getIntent().getStringExtra("episodes");
        episodes.setText(message);

        TextView created = findViewById(R.id.created);
        message = getIntent().getStringExtra("created");
        created.setText(message);

        ImageView image = findViewById(R.id.image);
        message = getIntent().getStringExtra("image");
        Glide.with(this)
                .load(message)
                .into(image);
    }
}