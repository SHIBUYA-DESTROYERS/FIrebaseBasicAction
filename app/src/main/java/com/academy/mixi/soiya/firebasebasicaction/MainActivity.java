package com.academy.mixi.soiya.firebasebasicaction;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    static final String TAG = "Firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Databaseへの参照
        mAuth = FirebaseAuth.getInstance();

        String email = "";
        String password = "";

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // アカウント作成に成功
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    // アカウント作成に失敗
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                }

                // ...
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ユーザーがログイン(non null)しているかどうかの確認
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // この後ログインしているかによってUIを変えたりすることが考えられる
    }

}
