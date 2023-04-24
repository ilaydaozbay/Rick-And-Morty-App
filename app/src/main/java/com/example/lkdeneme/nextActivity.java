package com.example.lkdeneme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class nextActivity extends AppCompatActivity {


    private Retrofit retrofit;
    private IApiClient ApiClient;
    private String baseUrl = "https://rickandmortyapi.com/api/";
    private Call<Location> locationsCall;
    private Call<Character> characterCall;

    private Location location;
    private Character character;

    private ArrayList<Button> myButtons = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        setRetrofitSettings();
    }



    private void Buttons(Location.Result result) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);
        Button btn = new Button(this);
        myButtons.add(btn);
        btn.setText(result.name);
        btn.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 20, 20, 20);
        btn.setLayoutParams(params);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        drawable.setStroke(3, Color.BLACK);
        drawable.setColor(Color.GRAY);
        btn.setBackground(drawable);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> id = new ArrayList<Integer>();
                for (int i = 0; i < result.residents.size(); i++) {
                    id.add(Integer.parseInt(result.residents.get(i).split("/")[5]));
                }


                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.cardV);
                linearLayout.removeAllViews();
                for (Button b : myButtons) {
                    GradientDrawable bg = (GradientDrawable) b.getBackground();
                    bg.setColor(Color.GRAY);
                }
                GradientDrawable bg = (GradientDrawable) btn.getBackground();
                bg.setColor(Color.WHITE);

                characterWebApi(id);
            }
        });
        layout.addView(btn);
    }

    private void Cards(Character character) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.cardV);

        //Kart Oluşturma
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(5, 5, 5, 5);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(5);
        cardView.setRadius(5);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.white));

        //Karakter resimleri
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(400, 400);
        imageParams.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(imageParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setElevation(3);
        }
        Glide.with(this)
                .load(character.getImage())
                .into(imageView);

        //Cinsiyet sembolleri
        ImageView gender = new ImageView(this);
        LinearLayout.LayoutParams imageParams2 = new LinearLayout.LayoutParams(300, 300);
        imageParams2.setMargins(600, 50, 5, 5);
        gender.setLayoutParams(imageParams2);
        if (character.getGender().equals("Male")) {
            gender.setImageResource(R.drawable.male);
        }
        else if (character.getGender().equals("Female")) {
            gender.setImageResource(R.drawable.female);
        }
        else {
            gender.setImageResource(R.drawable.unknown);
        }

        //Karakter isimleri
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textParams.setMargins(475, 137, 0, 0);
        textView.setLayoutParams(textParams);
        textView.setText(character.getName());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(getResources().getColor(R.color.purple_700));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

        cardView.addView(imageView);
        cardView.addView(gender);
        cardView.addView(textView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nextActivity.this, DetailPage.class);
                intent.putExtra("name", character.getName());
                intent.putExtra("status", character.getStatus());
                intent.putExtra("specy", character.getSpecies());
                intent.putExtra("gender", character.getGender());
                intent.putExtra("origin", character.getOrigin().name);
                intent.putExtra("location", character.getLocation().name);
                String episodes = "";
                for (int i = 0; i < character.getEpisode().size(); i++) {
                    if (i != character.getEpisode().size() - 1) {
                        episodes += Integer.parseInt(character.getEpisode().get(i).split("/")[5]) + ", ";
                    }
                    else {
                        episodes += Integer.parseInt(character.getEpisode().get(i).split("/")[5]);
                    }
                }
                intent.putExtra("episodes", episodes);
                intent.putExtra("created", character.getCreated().toString());
                intent.putExtra("image", character.getImage());
                startActivity(intent);
            }
        });

        linearLayout.addView(cardView);
    }

    private void setRetrofitSettings() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient = retrofit.create(IApiClient.class);
        locationsCall = ApiClient.getLocation();
        locationsCall.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                    location = response.body();
                    if (location != null) {
                        for (int i = 0; i < location.results.size(); i++) {
                            Buttons(location.results.get(i));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private void characterWebApi(ArrayList<Integer> list){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient = retrofit.create(IApiClient.class);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            characterCall = ApiClient.getCharacter("character/" + list.get(i));
            characterCall.enqueue(new Callback<Character>() {
                @Override
                public void onResponse(Call<Character> call, Response<Character> response) {
                    if (response.isSuccessful()) {
                        character = response.body();
                        if (character != null) {
                            Cards(character);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Character> call, Throwable t) {
                    System.out.println(t.toString());
                }
            });
        }
    }
}