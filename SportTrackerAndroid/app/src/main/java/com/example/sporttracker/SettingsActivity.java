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

import com.example.sporttracker.ApplicationMy;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity {
    public static final int ACTIVITY_ID=606;
    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";
    public static final String AGE = "AGE";
    public static final String ADULT = "ADULT";
    public static final String GENDER = "GENDER";

    ApplicationMy app;
    SharedPreferences sp;

    TextView tvUUID;
    EditText etEmail;
    EditText etUsername;
    EditText etAge;
    Switch switchOne;
    RadioGroup rdSex;
    RadioButton rdSelected;
    RadioButton rdMale;
    RadioButton rdFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        app = (ApplicationMy) getApplication();

        tvUUID = findViewById(R.id.tvUUID);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etAge = findViewById(R.id.etAge);
        switchOne = findViewById(R.id.switch1);
        rdSex = findViewById(R.id.radioSex);
        rdMale = findViewById(R.id.radioMale);
        rdFemale= findViewById(R.id.radioFemale);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
        tvUUID.setText(sp.getString(ApplicationMy.APP_ID,"DEFAULT VALUE "));
        etEmail.setText(sp.getString(EMAIL,"DEFAULT VALUE "));
        etUsername.setText(sp.getString(USERNAME,"DEFAULT VALUE "));
        etAge.setText(sp.getString(AGE,"DEFAULT VALUE "));
        switchOne.setChecked(sp.getBoolean(ADULT, false));
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
        if (!etEmail.getText().toString().equals("")){
            editor.putString(EMAIL,etEmail.getText().toString());
            editor.apply();
        }
        if (!etUsername.getText().toString().equals("")){
            editor.putString(USERNAME,etUsername.getText().toString());
            editor.apply();
        }
        if (!etAge.getText().toString().equals("")){
            editor.putString(AGE,etAge.getText().toString());
            editor.apply();
        }
        editor.putString(GENDER,rdSelected.getText().toString());
        editor.apply();
        editor.putBoolean(ADULT, switchOne.isChecked());
        editor.apply();
        finish();
    }

    public void onClickBack(View view) {
        finish();
    }
}