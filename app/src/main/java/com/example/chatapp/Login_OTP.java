package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.utils.AndroidUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Login_OTP extends AppCompatActivity {

    String Phn_no;
    EditText OTP_Input;
    Button nxt_btn;
    ProgressBar progressBar;
    TextView resend_otp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Long time_out_sec = 60L;
    String verification_code;
    PhoneAuthProvider.ForceResendingToken ResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        Phn_no = getIntent().getExtras().getString("phone");

        OTP_Input = findViewById(R.id.otp);
        nxt_btn = findViewById(R.id.next_btn);
        progressBar = findViewById(R.id.pg_2);
        resend_otp = findViewById(R.id.resend_otp);

        Send_OTP(Phn_no, false);

        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entered_otp = OTP_Input.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, entered_otp);
                SignIn(credential);
                set_in_progress(true);
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_OTP(Phn_no, true);
            }
        });
    }

    void Send_OTP(String Phn_no, boolean isResend)
    {
        start_resend_timer();
        set_in_progress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(Phn_no)
                .setTimeout(time_out_sec, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        SignIn(phoneAuthCredential);
                        set_in_progress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtils.showToast(getApplicationContext(), "OTP Verification Failed..!!");
                        set_in_progress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verification_code = s;
                        ResendingToken = forceResendingToken;
                        AndroidUtils.showToast(getApplicationContext(), "OTP Sent Successfully..!!");
                        set_in_progress(false);
                    }
                });
        if(isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        }
        else
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void set_in_progress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nxt_btn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            nxt_btn.setVisibility(View.VISIBLE);
        }
    }

    void SignIn(PhoneAuthCredential phoneAuthCredential)
    {
        set_in_progress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    set_in_progress(false);
                    Intent intent = new Intent(Login_OTP.this, Login_username.class);
                    intent.putExtra("phone", Phn_no);
                    startActivity(intent);
                }
                else
                {
                    AndroidUtils.showToast(getApplicationContext(), "OTP Verification Failed..!!");
                }
            }
        });
    }

    void start_resend_timer()
    {
        resend_otp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time_out_sec--;
                resend_otp.setText("Resend OTP in "+time_out_sec+" Seconds");
                if(time_out_sec<=0)
                {
                    time_out_sec = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resend_otp.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }
}