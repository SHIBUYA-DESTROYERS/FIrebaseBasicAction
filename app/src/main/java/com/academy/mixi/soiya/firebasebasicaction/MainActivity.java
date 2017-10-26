package com.academy.mixi.soiya.firebasebasicaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;
    ChildEventListener cel;
    ValueEventListener vel;
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
        // mode = 1
        vel= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // getChildren()メソッドで各要素をコレクションとして取り出す
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    String sender = chatMessage.sender;
                    String body = chatMessage.body;
                    Log.d(TAG, String.format("onDataChange(), sender:%s, body:%s", sender, body));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // 読み込みに失敗したら呼び出される
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        // 細かい変更を把握することができる
        // mode = 2
        cel = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                String sender = chatMessage.sender;
                String body = chatMessage.body;
                Log.d(TAG, String.format("onChildAdded(), sender:%s, body:%s, previousChildName:%s", sender, body, s));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 読み込みに失敗したら呼び出される
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };


        int mode = 4;

        switch (mode){
            case 1:
                myRef.addValueEventListener(vel);
                break;
            case 2:
                myRef.addChildEventListener(cel);
                break;
            case 3:
                // 1回だけ呼び出される
                myRef.addListenerForSingleValueEvent(vel);
                break;
            case 4:
                /*
                  case3と同じ結果になると思ったが，Databaseから
                  値の受け渡しは確認できなかった．
                 */
                myRef.addValueEventListener(vel);
                myRef.removeEventListener(vel);
                break;
        }

    }
}
