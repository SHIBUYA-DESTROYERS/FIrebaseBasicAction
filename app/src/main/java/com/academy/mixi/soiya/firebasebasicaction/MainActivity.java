package com.academy.mixi.soiya.firebasebasicaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;
    ValueEventListener vel;
    Query orderByChildQuery;
    Query orderByKeyQuery;
    static final String TAG = "Firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Databaseへの参照
        database = FirebaseDatabase.getInstance();
        // jsonのトップレベルにある"messages"というノードを参照してます
        myRef = database.getReference("/messages");

        // 変更が起こるとノードの要素を全部読み込む
        final int BASIC = 1;
        vel= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange ok");
                // getChildren()メソッドで各要素をコレクションとして取り出す
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    String sender = chatMessage.sender;
                    String body = chatMessage.body;
                    long timestamp = chatMessage.timestamp;
                    Log.d("Firebase", String.format("onChildAdded, sender:%s, body:%s, timestamp:%d", sender, body, timestamp));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // 読み込みに失敗したら呼び出される
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        // timestampを基準にソートしてくれた結果を吐く (昇順)
        final int ORDER_BY_CHILD = 2;
        orderByChildQuery = myRef.orderByChild("timestamp");

        // 子要素のkey名でソートする (昇順)
        // 今回参照されるのは 01, 02, 03...の部分でもともと昇順なので結果は変わらない
        final int ORDER_BY_KEY = 3;
        orderByKeyQuery = myRef.orderByKey();



        int mode = BASIC;

        switch (mode){
            case BASIC:
                myRef.addValueEventListener(vel);
                break;
            case ORDER_BY_CHILD:
                orderByChildQuery.addValueEventListener(vel);
                break;
            case ORDER_BY_KEY:
                orderByKeyQuery.addValueEventListener(vel);
                break;
        }

    }
}
