package com.example.sporttracker;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity {
    public static final int ACTIVITY_ID=606;
    public static final String HEIGHT = "HEIGHT";
    public static final String WEIGHT = "WEIGHT";
    public static final String USERNAME = "USERNAME";
    public static final String AGE = "AGE";
    public static final String GENDER = "GENDER";

    ApplicationMy app;
    SharedPreferences sp;

    TextView tvUUID;
    EditText etHeight;
    EditText etWeight;
    EditText etUsername;
    EditText etAge;
    RadioGroup rdSex;
    RadioButton rdSelected;
    RadioButton rdMale;
    RadioButton rdFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        app = (ApplicationMy) getApplication();

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etUsername = findViewById(R.id.etUsername);
        etAge = findViewById(R.id.etAge);
        rdSex = findViewById(R.id.radioSex);
        rdMale = findViewById(R.id.radioMale);
        rdFemale= findViewById(R.id.radioFemale);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
        etHeight.setText(String.valueOf(sp.getFloat(HEIGHT,0)));
        etWeight.setText(String.valueOf(sp.getFloat(WEIGHT,0)));
        etUsername.setText(sp.getString(USERNAME,"DEFAULT VALUE "));
        etAge.setText(String.valueOf(sp.getInt(AGE,0)));
        if(sp.getString(GENDER, "Male").equals("Male")){
            rdSex.check(rdMale.getId());
        }
        else rdSex.check(rdFemale.getId());


    }

    /*public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }*/



    @Override
    protected void onResume(){
        super.onResume();
    }

    public void onClickSave(View view) {
        SharedPreferences.Editor editor = sp.edit();
        rdSelected = (RadioButton) findViewById(rdSex.getCheckedRadioButtonId());
        if (!etHeight.getText().toString().equals("")){
            editor.putFloat(HEIGHT,Float.parseFloat(etHeight.getText().toString()));
            editor.apply();
            //app.thisUser.setHeight(Double.parseDouble(etHeight.getText().toString()));
        }
        if (!etWeight.getText().toString().equals("")){
            editor.putFloat(WEIGHT,Float.parseFloat(etWeight.getText().toString()));
            editor.apply();
            //app.thisUser.setWeight(Double.parseDouble(etWeight.getText().toString()));
        }
        if (!etUsername.getText().toString().equals("")){
            editor.putString(USERNAME,etUsername.getText().toString());
            editor.apply();
            //app.thisUser.username = etUsername.getText().toString();
        }
        if (!etAge.getText().toString().equals("")){
            editor.putInt(AGE,Integer.parseInt(etAge.getText().toString()));
            editor.apply();
            //app.thisUser.setAge(Integer.parseInt(etWeight.getText().toString()));
        }
        editor.putString(GENDER,rdSelected.getText().toString());
        editor.apply();

        finish();
    }

    public void onClickBack(View view) {
        finish();
    }
}