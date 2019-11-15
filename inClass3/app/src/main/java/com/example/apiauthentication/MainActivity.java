package com.example.apiauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SignUp.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,UserProfile.OnFragmentInteractionListener{

    String x_auth_token;
    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
         x_auth_token=preferences.getString("x-auth-token","");
     ///   Toast.makeText(this, x_auth_token, Toast.LENGTH_SHORT).show();

        if(x_auth_token.contentEquals("")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Login())
                    .commit();
        }else
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new UserProfile())
                .commit();

    }



    @Override
    public void changeToHomeFragment(String token) {
        x_auth_token=token;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("x-auth-token",token);
        editor.apply();
      //  Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new UserProfile())
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
