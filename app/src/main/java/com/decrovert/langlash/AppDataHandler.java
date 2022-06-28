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
        JSONObject finalObject = null;

        try {
            fis = context.openFileInput(DATA_FILENAME);

            InputStreamReader fisReader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(fisReader);
            StringBuilder result = new StringBuilder();
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                result.append(currentLine).append("\n");
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
            finalObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return finalObject;
    }

    private static JSONObject readAppDataFileRecheck(Context context) {
        JSONObject appJSONData = readAppDataFile(context);

        for (byte i = 0; appJSONData == null; ++i) {
            if (i == 1) {
                return null;
            }

            createAppDataFile(context);
            appJSONData = readAppDataFile(context);
        }

        return appJSONData;
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

    public static void saveNewLanguage(Context context, String languageName) {
        if (languageName.isEmpty()) {
            Toast.makeText(context, "Can't create a language without a name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (languageName.length() > 30) {
            Toast.makeText(context, "The given name is too long!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isStringAZ(languageName)) {
            Toast.makeText(context, "Language names can only contain characters from the English alphabet!", Toast.LENGTH_LONG).show();
            return;
        }

        languageName = languageName.replace(" ", "");

        JSONObject appJSONData = readAppDataFileRecheck(context);

        if (appJSONData == null) {
            return;
        }

        JSONObject new_language = new JSONObject();

        try {
            new_language.put("name", languageName);
            new_language.put("words", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        try {
            if (!appJSONData.has("languages")) {
                appJSONData.put("languages", new JSONArray());
            }

            JSONArray languages_array = appJSONData.getJSONArray("languages");

            for (int i = 0; i < languages_array.length(); ++i) {
                if (languages_array.getJSONObject(i).getString("name").equals(languageName)) {
                    Toast.makeText(context, "This language already exists!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            languages_array.put(new_language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        writeAppDataFile(context, appJSONData.toString());
    }
}
