package com.decrovert.langlash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class NewLanguage extends AppCompatActivity {
    private static final String DATA_FILENAME = "language_data.json";
    private static TextInputEditText language_name_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_language);

        Button confirm_button, cancel_button;

        confirm_button = findViewById(R.id.new_language_confirm_button);
        confirm_button.setOnClickListener(l -> {
            saveNewLanguage();
            finish();
        });

        cancel_button = findViewById(R.id.new_language_cancel_button);
        cancel_button.setOnClickListener(l -> finish());

        language_name_container = findViewById(R.id.new_language_name_input_text);
    }

    private void createAppDataFile() {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(DATA_FILENAME, MODE_PRIVATE);

            JSONObject root = new JSONObject();

            fos.write(root.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject readAppDataFile() {
        FileInputStream fis = null;
        String data = "";
        JSONObject final_object = null;

        try {
            fis = openFileInput(DATA_FILENAME);

            InputStreamReader fis_reader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(fis_reader);
            StringBuilder result = new StringBuilder();
            String current_line;

            while ((current_line = br.readLine()) != null) {
                result.append(current_line).append("\n");
            }

            data = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            final_object = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return final_object;
    }

    private void saveNewLanguage() {
        final String language_name = Objects.requireNonNull(language_name_container.getText()).toString();

        if (language_name.isEmpty()) {
            return;
        }

        JSONObject app_json_data = readAppDataFile();

        for (byte i = 0; app_json_data == null; ++i) {
            if (i == 1) {
                return;
            }

            createAppDataFile();
            app_json_data = readAppDataFile();
        }

        JSONObject new_language = new JSONObject();

        try {
            new_language.put("name", language_name);
            new_language.put("words", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        try {
            if (!app_json_data.has("languages")) {
                app_json_data.put("languages", new JSONArray());
            }

            JSONArray languages_array = app_json_data.getJSONArray("languages");

            for (int i = 0; i < languages_array.length(); ++i) {
                if (languages_array.getJSONObject(i).getString("name").equals(language_name)) {
                    return; // The language already exists.
                }
            }

            languages_array.put(new_language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(DATA_FILENAME, MODE_PRIVATE);
            fos.write(app_json_data.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}