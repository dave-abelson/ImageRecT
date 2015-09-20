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

public class RegisterActivity extends AppCompatActivity {

    //UI
    private EditText email;
    private EditText user;
    private EditText pass;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Set up UI
        email = (EditText)findViewById(R.id.registerEmail);
        user = (EditText)findViewById(R.id.registerUser);
        pass = (EditText)findViewById(R.id.registerPass);
        submit = (Button)findViewById(R.id.registerRegister);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 Send info to next page
                 */

                String mEmail = email.getText().toString();
                String mUser = user.getText().toString();
                String mPass = pass.getText().toString();

                //Check that none are null
                if(mEmail.isEmpty() || mUser.isEmpty() || mPass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Pass info
                Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                i.putExtra("email", email.getText().toString());
                i.putExtra("user", user.getText().toString());
                i.putExtra("pass", pass.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
