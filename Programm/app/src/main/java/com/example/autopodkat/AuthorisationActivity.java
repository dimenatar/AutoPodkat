package com.example.autopodkat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorisationActivity extends AppCompatActivity
{

    private boolean isCorrectFirst = false;
    private boolean isCorrectSecond = false;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private EditText UserName, Pass;
    private Button Submit;
    private ColorStateList colorStateList;
    private TextView regText, userNameHeader;
    // Volley Variables
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mToolbar = findViewById(R.id.auth_ToolBar);
        setSupportActionBar(mToolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_button));

        UserName = findViewById(R.id.AuthUserNameEdit);
        Pass = findViewById(R.id.AuthPassEdit);
        Submit = findViewById(R.id.Confirm_auth_button);
        regText = findViewById(R.id.NotHaveAcc);
        regText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AuthorisationActivity.this, RegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                AuthorisationActivity.this.startActivity(intent);
            }
        });
        UserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                UserName = CheckEditCorrectness(UserName, 1);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        Pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pass = CheckEditCorrectness(Pass, 2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isCorrectFirst && isCorrectSecond)
                {
                    signIn(UserName.getText().toString(), Pass.getText().toString());
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private EditText CheckEditCorrectness(EditText text, int number)
    {

        if (text.getText().toString() != "")
        {
            colorStateList = ColorStateList.valueOf(Color.GREEN);
            text.setBackgroundTintList(colorStateList);
            isCorrectFirst = true;
            if (number == 1) isCorrectFirst = true;
            else isCorrectSecond = true;
        }
        else
        {
            colorStateList = ColorStateList.valueOf(Color.RED);
            text.setBackgroundTintList(colorStateList);
            if (number == 1) isCorrectFirst = false;
            else isCorrectSecond = false;
            isCorrectFirst = false;
        }
        return text;
    }

    private void signIn( final String userName, final String password) {

        // Initializing Request queue
        mRequestQueue = Volley.newRequestQueue(AuthorisationActivity.this);

        mStringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.102/auth.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1"))
                    {
                        Toast.makeText(AuthorisationActivity.this,message,Toast.LENGTH_SHORT).show();
                        MainActivity.user = new User(jsonObject.getInt("UserID"), jsonObject.getString("UserName"),jsonObject.getString("Pass"),jsonObject.getString("Passport"),jsonObject.getString("Telephone"));
                        MainActivity.iSetName.SetName(jsonObject.getString("UserName"), true);
                        SaveManager.SaveAcc(MainActivity.user, getApplicationContext());
                        finish();
                    }
                    if (success.equals("0"))
                    {
                        Toast.makeText(AuthorisationActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(AuthorisationActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AuthorisationActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("userName",userName);
                params.put("pass",password);
                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }
}
