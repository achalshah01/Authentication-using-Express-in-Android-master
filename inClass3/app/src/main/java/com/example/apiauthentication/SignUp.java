package com.example.apiauthentication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.TokenWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public  class SignUp extends Fragment {
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
    EditText password;
    EditText confirmPassword;
    EditText city;
    static EditText dob;
    Button signUp;
    RadioGroup genderRadioGroup;
    String FirstName, LastName, Email, Password, Gender="Male", City,dateOfBirth;
    String Token;

    public SignUp() {
    }



    // TODO: Rename and change types and number of parameters


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
        View view=inflater.inflate(R.layout.fragment_sign_up, container, false);

        firstName=view.findViewById(R.id.editFirstName);
        lastName=view.findViewById(R.id.editLastName);
        email=view.findViewById(R.id.editEmailId);
        password=view.findViewById(R.id.editPassword);
        confirmPassword=view.findViewById(R.id.confirmPassword);
        city=view.findViewById(R.id.cty);
        signUp=view.findViewById(R.id.button2);
        genderRadioGroup = view.findViewById(R.id.radioGroup);
      //  dob = view.findViewById(R.id.dob);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.maleRadioButton:
                        Gender = "Male";
                        break;
                    case R.id.femaleRadioButton:
                        Gender = "Female";
                        break;
                    default:
                        break;
                }
            }
        });


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (firstName.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getContext(), "Enter First Name", Toast.LENGTH_SHORT).show();
                        firstName.setError("This field can not be blank");
                    } else if (lastName.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getContext(), "Enter Last Name", Toast.LENGTH_SHORT).show();
                        lastName.setError("This field can not be blank");
                    } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getContext(), "Enter Email ID", Toast.LENGTH_SHORT).show();
                        email.setError("This field can not be blank");
                    } else if ((password.getText().toString().trim().equalsIgnoreCase(""))) {
                        Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                        password.setError("This field can not be blank");
                    } else if ((confirmPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                        Toast.makeText(getContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                        confirmPassword.setError("This field can not be blank");
                    } else if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                            FirstName = firstName.getText().toString().trim();
                            LastName = lastName.getText().toString().trim();
                            Email = email.getText().toString().trim();
                            Password = password.getText().toString().trim();
                            City = city.getText().toString().trim();
                           // dateOfBirth=dob.getText().toString();
                            StringBuilder sb = new StringBuilder();
                            sb.append("https://authentication123.herokuapp.com/api/users");
                            Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();

                            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                            final OkHttpClient client = new OkHttpClient();

                            JSONObject postdata=new JSONObject();
                            try {
                                postdata.put ("firstName",FirstName);
                                  postdata.put ("lastName",LastName);
                                postdata.put (	"email",Email);
                                postdata.put (	"password",Password);
                                postdata.put (	"gender",Gender);
                                postdata.put (		"city",City);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RequestBody body =RequestBody.create(JSON,postdata.toString());
                            Request request = new Request.Builder()
                                    .url(sb.toString())
                                    .header("Content-Type", "application/json")
                                    .post(body)
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
                                        JSONObject token=new JSONObject(responseData);
                                        Token=token.getString("token");
                                        Log.d("DEMO123=",Token);

                                       // onComplete(Token);
                                        client.dispatcher().executorService().shutdown();
                                        Handler mainHandler = new Handler(Looper.getMainLooper());

                                        mainHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mListener.changeToHomeFragment(Token);

                                            }
                                        });



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                          //  onComplete(Token);

                        } else {
                            password.setError("Passwords do not match");
                            confirmPassword.setError("Passwords do not match");
                        }
                    }


                }
            });
        }

        return view;
    }

public  void  onComplete(String Token){

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



//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        dob.setText(month+":"+dayOfMonth+":"+year);
//    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name


        void changeToHomeFragment(String token);
    }




}
