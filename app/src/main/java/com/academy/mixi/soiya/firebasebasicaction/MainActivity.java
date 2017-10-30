package com.academy.mixi.soiya.firebasebasicaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;
    static final String TAG = "Firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Databaseへの参照
        database = FirebaseDatabase.getInstance();
        // jsonのトップレベルにある"messages"というノードを参照してます
        myRef = database.getReference("/messages");


        int mode = 4;

        switch (mode)
        {
            // デフォな追加方法
            case 1:
                myRef.child("01").setValue(new ChatMessage("デフォな追加方法で追加", "俺", System.currentTimeMillis()));
                break;

            // 完了コールバック
            case 2:
                myRef.child("02").setValue(new ChatMessage("完了コールバック付きで追加", "俺", System.currentTimeMillis()),
                        new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null){
                                    Log.d(TAG, "書き込みに失敗‥ :"+databaseError.getMessage(), databaseError.toException());
                                }
                                else{
                                    Log.d(TAG, "書き込みに成功したよ！");
                                }
                            }
                        });

            // 一部だけ更新の場合 (今回は"body"だけを更新している)
            case 3:
                Map<String, Object> sender = new HashMap<>();
                sender.put("body", "一部だけ更新した");
                myRef.child("01").updateChildren(sender);

            // myRef.child("指定のkey")だとコンフリクト起こした際上書きになるのでそれを防ぐ
            // 指定のkeyの代わりに適当な文字列が入る
            case 4:
                myRef.push().setValue(new ChatMessage("コンフリクトを起こさない投稿", "admin", System.currentTimeMillis()));

            // 以下のような使い方もできる
            case 5:
                String key = myRef.push().getKey();
                Log.d(TAG,"key: "+key);
                myRef.child(key).setValue(new ChatMessage("事前にkeyを把握してコンフリクトを起こさない投稿", "admin", System.currentTimeMillis()));

            // runTransaction()ってのもあるけど挫折
            // https://firebase.google.com/docs/database/android/read-and-write#updating_or_deleting_data#データの更新または削除
        }

    }
}
