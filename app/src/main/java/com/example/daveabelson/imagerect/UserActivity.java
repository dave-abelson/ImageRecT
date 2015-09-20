package com.example.daveabelson.imagerect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by stanc on 9/19/15.
 */
public class UserActivity extends AppCompatActivity {

    //UI
    private EditText user;
    private EditText password;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Set up UI
        user = (EditText)findViewById(R.id.userUsername);
        password = (EditText)findViewById(R.id.userPass);
        signIn = (Button)findViewById(R.id.userSignIn);

        //Set up event listener
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String pass = password.getText().toString();
                Toast.makeText(UserActivity.this, "Submitting...", Toast.LENGTH_LONG).show();

                /*************************
                Verify with server
                 *************************/
                ParseUser.logInInBackground(username, pass, new LogInCallback(){

                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e == null){
                            //User now logged in

                            Intent i = new Intent(UserActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            //Something went wrong
                            //Check e.getCode() and e.getMessage()
                            Toast.makeText(UserActivity.this, "Error logging in! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
