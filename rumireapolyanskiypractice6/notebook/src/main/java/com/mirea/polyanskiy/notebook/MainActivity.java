package com.mirea.polyanskiy.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String LAST_FILE = "last_file";
    private EditText Name;
    private TextView Text;
    private Button button;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name = findViewById(R.id.Name);
        Text = findViewById(R.id.Text);
        button = findViewById(R.id.btnSave);
        preferences = getPreferences(MODE_PRIVATE);
        String text = getTextFromLastFile();
        Text.setText(text);
    }
    public void onSaveFile(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        String name = Name.getText().toString();
        String text = Text.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "name of File", Toast.LENGTH_SHORT).show();
            return;
        }
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(name, Context.MODE_PRIVATE))) {
            outputStreamWriter.write(text);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        editor.putString(LAST_FILE, name);
        editor.apply();
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    public String getTextFromLastFile() {
        FileInputStream fin = null;
        String lastFileName = preferences.getString(LAST_FILE, "");
        try {
            fin = openFileInput(lastFileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(LOG_TAG, text);
            return text;
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return "";
    }
}