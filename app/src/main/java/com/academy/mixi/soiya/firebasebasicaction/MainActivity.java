package com.academy.mixi.soiya.firebasebasicaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Databaseへの参照
        database = FirebaseDatabase.getInstance();
        // jsonのトップレベルにある"message"というノードを参照してます
        myRef = database.getReference("/message");

        // 初回orDBが更新されるたびに呼び出される
        myRef.addValueEventListener(new ValueEventListener() {
            static final String TAG = "Firebase";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 今回はstringのみしか降ってこないので以下の書き方でもエラーが出ない
                // モデルクラスを書くのが最適
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // 読み込みに失敗したら呼び出される
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
