package com.decrovert.langlash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newLanguageButton = findViewById(R.id.new_language);
        newLanguageButton.setOnClickListener(v -> openNewLanguageMenu());
    }

    private void openNewLanguageMenu() {
        Intent intent = new Intent(this, NewLanguageActivity.class);
        startActivity(intent);
    }
}