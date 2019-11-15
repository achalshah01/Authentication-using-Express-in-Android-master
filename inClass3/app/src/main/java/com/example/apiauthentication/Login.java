package com.example.apiauthentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static android.widget.Toast.LENGTH_SHORT;


public class Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Button login;
    Button signUp;
    Button forget;
    EditText email;
    EditText password;
    Context context;
    String Token;
    SharedPreferences sharedpreferences;

    public Login() {
        // Required empty public constructor
    }


    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
       View view=inflater.inflate(R.layout.fragment_login, container, false);
        login = view.findViewById(R.id.login);

        signUp = view.findViewById(R.id.signup);
        email = view.findViewById(R.id.editLastName);
        password = view.findViewById(R.id.editPassword);
        context = view.getContext();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("signUp")
                        .replace(R.id.container, new SignUp(), "New User")
                        .commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email", LENGTH_SHORT).show();
                    email.setError("This field can not be blank");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Password", LENGTH_SHORT).show();
                    password.setError("This field can not be blank");
                } else {

                    String logInEmail = email.getText().toString();
                    String logInPassword = password.getText().toString();
                    StringBuilder sb = new StringBuilder();
                    sb.append("https://authentication123.herokuapp.com/api/auth");
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                    final OkHttpClient client = new OkHttpClient();

                    JSONObject postdata=new JSONObject();
                    try {

                        postdata.put (	"email",logInEmail);
                        postdata.put (	"password",logInPassword);

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

                }
            }
        });



        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("x-auth-token", Token);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
   void changeToHomeFragment(String Token);
    }
}
