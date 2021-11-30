package com.example.autopodkat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private Button regButton;
    private String orderRequest = "select userid, carid, startdate, enddate, amounthoures,totalprice from orders";
    private EditText userName, pass, telephone, passport;
    private TextWatcher mTextWatcher;
    private boolean isUserCorrect = false;
    private boolean isPassCorrect = false;
    private boolean isTelephoneCorrect = false;
    private boolean isPassportCorrect = false;
    private ColorStateList colorStateList;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mToolbar = findViewById(R.id.reg_toolBar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_button));

        userName = findViewById(R.id.UserNameEdit);
        pass = findViewById(R.id.PassEdit);
        telephone = findViewById(R.id.TelephoneEdit);
        passport = findViewById(R.id.PassportEdit);
        regButton = findViewById(R.id.Confirm_reg_button);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = CheckCorrectness(userName, 1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass = CheckCorrectness(pass, 2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passport = CheckCorrectness(passport, 3);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        telephone.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                return keyCode == KeyEvent.KEYCODE_DEL && telephone.getText().toString().equals("+375 ");
            }
        });
        telephone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if (telephone.getText().toString().length() == 13)
                {
                    colorStateList = ColorStateList.valueOf(Color.GREEN);
                    isTelephoneCorrect = true;
                }
                else
                {
                    colorStateList = ColorStateList.valueOf(Color.RED);
                    isTelephoneCorrect = false;
                }
                telephone.setBackgroundTintList(colorStateList);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (telephone.getText().toString().length() == 13)
                {
                    colorStateList = ColorStateList.valueOf(Color.GREEN);
                    isTelephoneCorrect = true;
                }
                else
                {
                    colorStateList = ColorStateList.valueOf(Color.RED);
                    isTelephoneCorrect = false;
                }
                telephone.setBackgroundTintList(colorStateList);
            }
        });
        regButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isPassCorrect && isPassportCorrect  && isUserCorrect && isTelephoneCorrect)
                createUser(userName.getText().toString(), pass.getText().toString(), passport.getText().toString(), telephone.getText().toString());
                else Toast.makeText(RegistrationActivity.this, "Insert correct information:(", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private EditText CheckCorrectness(EditText text, int number) {
        if (text.getText().toString().length() >= 4)
        {
            colorStateList = ColorStateList.valueOf(Color.GREEN);
            text.setBackgroundTintList(colorStateList);
            switch (number) {
                case 1: {
                    isUserCorrect = true;
                }
                case 2: {
                    isPassCorrect = true;
                }
                case 3: {
                    isPassportCorrect = true;
                }
            }
        } else {
            colorStateList = ColorStateList.valueOf(Color.RED);
            text.setBackgroundTintList(colorStateList);
            switch (number) {
                case 1: {
                    isUserCorrect = false;
                }
                case 2: {
                    isPassCorrect = false;
                }
                case 3: {
                    isPassportCorrect = false;
                }
            }

        }
        return text;
    }
    private void createUser(final String userName, final String pass, final String passport, final String telephone){

        RequestQueue mRequestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        // Progress

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.102/createUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1"))
                    {
                        MainActivity.user = new User(jsonObject.getInt("UserID"), userName, pass, passport, telephone);
                        SaveManager.SaveAcc(MainActivity.user, getApplicationContext());
                        MainActivity.iSetName.SetName(MainActivity.user.UserName, true);
                        SaveManager.EraseOrders(getApplicationContext());
                        finish();
                    }
                    Toast.makeText(RegistrationActivity.this,message,Toast.LENGTH_SHORT).show();

                } catch (JSONException e)
                {
                    Toast.makeText(RegistrationActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(RegistrationActivity.this,"Unable connect to server", Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID","default");
                params.put("userName",userName);
                params.put("pass",pass);
                params.put("passport",passport);
                params.put("telephone",telephone.trim());

                return params;
            }
        };
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }
}
