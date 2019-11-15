package com.example.apiauthentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText gender;
    EditText city;
    Button logout;
    String FirstName, LastName, Email, Password, Gender="Male", City,dateOfBirth;
    String Token;
    public UserProfile() {
        // Required empty public constructor
    }


    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_user_profile, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Token=preferences.getString("x-auth-token","");
       // Toast.makeText(getContext(), Token, Toast.LENGTH_SHORT).show();

        firstName=view.findViewById(R.id.UserFirstName);
        lastName=view.findViewById(R.id.UserLastName);
        email=view.findViewById(R.id.UserEmailId);
        gender=view.findViewById(R.id.UserGender);
        city=view.findViewById(R.id.Usercty);
        logout=view.findViewById(R.id.LogOut);



        StringBuilder sb = new StringBuilder();
        sb.append("https://authentication123.herokuapp.com/api/auth");
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(sb.toString())
                .header("Content-Type", "application/json")
                .header("x-auth-token",Token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Log.d("DEMO",response.body().string());
                String responseData=response.body().string();
                try {
                    final JSONObject token=new JSONObject(responseData);
                    //Token=token.getString("token");
                    Log.d("DEMO123=",token.toString());
                    // onComplete(Token);
                    client.dispatcher().executorService().shutdown();
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                firstName.setText(token.getString("firstName"));
                                lastName.setText((token.getString("lastName")));
                                email.setText((token.getString("email")));
                                gender.setText((token.getString("gender")));
                                city.setText((token.getString("city")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("x-auth-token","");
                editor.apply();
                getActivity().recreate();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
