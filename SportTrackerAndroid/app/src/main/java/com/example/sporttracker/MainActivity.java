package com.example.sporttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final int ACTIVITY_ID=606;
    public static final String HEIGHT = "HEIGHT";
    public static final String WEIGHT = "WEIGHT";
    public static final String USERNAME = "USERNAME";
    public static final String AGE = "AGE";
    public static final String GENDER = "GENDER";

    ApplicationMy app;
    SharedPreferences sp;
    private Button logout;
    private TextView textViewUser;
    private String userID;

    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (ApplicationMy) getApplication();

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        textViewUser = findViewById(R.id.userTxt);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userPRofile = snapshot.getValue(User.class);

                if(userPRofile != null){
                    String username = userPRofile.username;

                    textViewUser.setText("Welcome " + username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void OpenLeaderboards(View view) {

    }

    public void OpenMyPreviousRuns(View view) {

    }

    public void onClick_OpenSettings(MenuItem item) {
        Intent i = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(i);
    }

    public void OpenNewRun(View view) {
        Intent i = new Intent(getBaseContext(), RunningActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }
}