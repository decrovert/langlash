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

        Button confirm_button, cancel_button;
        TextInputEditText language_name_container;

        language_name_container = findViewById(R.id.new_language_name_input_text);

        confirm_button = findViewById(R.id.new_language_confirm_button);
        confirm_button.setOnClickListener(l -> {
            AppDataHandler.saveNewLanguage(this, Objects.requireNonNull(language_name_container.getText()).toString());
            finish();
        });

        cancel_button = findViewById(R.id.new_language_cancel_button);
        cancel_button.setOnClickListener(l -> finish());
    }
}