package com.decrovert.langlash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private void clearLanguageList() {
        LinearLayout languagesLinearLayout = findViewById(R.id.languagesLinearLayout);
        languagesLinearLayout.removeAllViews();
    }

    private void addLanguageButton(String languageName) {
        Button newButton = new Button(this);
        newButton.setText(languageName);

        newButton.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout languagesLinearLayout = findViewById(R.id.languagesLinearLayout);
        languagesLinearLayout.addView(newButton);
    }

    private void populateLanguageList() {
        clearLanguageList();

        JSONObject appData = AppDataHandler.readAppDataFileRecheck(this);
        JSONArray languagesData = null;

        try {
            languagesData = appData.getJSONArray("languages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < languagesData.length(); ++i) {
            try {
                addLanguageButton(languagesData.getJSONObject(i).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newLanguageButton = findViewById(R.id.new_language);
        newLanguageButton.setOnClickListener(v -> openNewLanguageMenu());

        populateLanguageList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateLanguageList();
    }

    private void openNewLanguageMenu() {
        Intent intent = new Intent(this, NewLanguageActivity.class);
        startActivity(intent);
    }
}