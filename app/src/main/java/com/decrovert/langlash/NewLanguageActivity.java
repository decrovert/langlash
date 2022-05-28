package com.decrovert.langlash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class NewLanguageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_language);

        Button confirmButton, cancelButton;
        TextInputEditText languageNameContainer;

        languageNameContainer = findViewById(R.id.new_language_name_input_text);

        confirmButton = findViewById(R.id.new_language_confirm_button);
        confirmButton.setOnClickListener(l -> {
            AppDataHandler.saveNewLanguage(this, Objects.requireNonNull(languageNameContainer.getText()).toString());
            finish();
        });

        cancelButton = findViewById(R.id.new_language_cancel_button);
        cancelButton.setOnClickListener(l -> finish());
    }
}