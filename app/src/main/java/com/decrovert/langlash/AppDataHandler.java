package com.decrovert.langlash;

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

    private static void writeAppDataFile(Context context, String string) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
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

    private static void createAppDataFile(Context context) {
        JSONObject root = new JSONObject();
        writeAppDataFile(context, root.toString());
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

    private static JSONObject readAppDataFileRecheck(Context context) {
        JSONObject app_json_data = readAppDataFile(context);

        for (byte i = 0; app_json_data == null; ++i) {
            if (i == 1) {
                return null;
            }

            createAppDataFile(context);
            app_json_data = readAppDataFile(context);
        }

        return app_json_data;
    }

    /** Checks if a String only contains characters from the English alphabet. */
    private static boolean isStringAZ(String string) {
        for (int i = 0; i < string.length(); ++i) {
            boolean lowercase = (byte)string.charAt(i) >= 'a' && (byte)string.charAt(i) <= 'z';
            boolean uppercase = (byte)string.charAt(i) >= 'A' && (byte)string.charAt(i) <= 'Z';

            if (!lowercase && !uppercase) {
                return false;
            }
        }

        return true;
    }

    public static void saveNewLanguage(Context context, String language_name) {
        if (language_name.isEmpty()) {
            Toast.makeText(context, "Can't create a language without a name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (language_name.length() > 30) {
            Toast.makeText(context, "The given name is too long!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isStringAZ(language_name)) {
            Toast.makeText(context, "Language names can only contain characters from the English alphabet!", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject app_json_data = readAppDataFileRecheck(context);

        if (app_json_data == null) {
            return;
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

        writeAppDataFile(context, app_json_data.toString());
    }
}
