package com.decrovert.langlash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button new_language_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new_language_button = findViewById(R.id.new_language);
        new_language_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewLanguageMenu();
            }
        });
    }

    private void openNewLanguageMenu() {
        Intent intent = new Intent(this, NewLanguage.class);
        startActivity(intent);
    }
}