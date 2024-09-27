package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class Login_phn_no extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phn_no;
    Button Send_OTP;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phn_no);

        countryCodePicker = findViewById(R.id.country_code);
        phn_no = findViewById(R.id.mb_num);
        Send_OTP = findViewById(R.id.otp_btn);
        progressBar = findViewById(R.id.pg_1);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phn_no);
        Send_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!countryCodePicker.isValidFullNumber()){
                    phn_no.setError("Phone number is not valid..!!");
                    return;
                }
                else {
                    Intent i =  new Intent(Login_phn_no.this, Login_OTP.class);
                    i.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
                    startActivity(i);
                }
            }
        });
    }
}