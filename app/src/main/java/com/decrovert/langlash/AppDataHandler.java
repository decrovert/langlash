package com.decrovert.langlash;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppDataHandler {
    private static final String DATA_FILENAME = "language_data.json";

    private static void createAppDataFile(Context context) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(DATA_FILENAME, MODE_PRIVATE);

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

    private static JSONObject readAppDataFile(Context context) {
        FileInputStream fis = null;
        String data = "";
        JSONObject final_object = null;

        try {
            fis = context.openFileInput(DATA_FILENAME);

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

    public static void saveNewLanguage(Context context, String language_name) {
        if (language_name.isEmpty()) {
            Toast.makeText(context, "Can't create a language without a name!", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject app_json_data = readAppDataFile(context);

        for (byte i = 0; app_json_data == null; ++i) {
            if (i == 1) {
                return;
            }

            createAppDataFile(context);
            app_json_data = readAppDataFile(context);
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
                    Toast.makeText(context, "This language already exists!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            languages_array.put(new_language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(DATA_FILENAME, MODE_PRIVATE);
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
