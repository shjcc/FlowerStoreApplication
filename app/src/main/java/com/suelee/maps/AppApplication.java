package com.suelee.maps;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suelee.maps.data.DataHelper;
import com.suelee.maps.data.FlowerShop;

import java.lang.reflect.Type;
import java.util.List;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mapMarkData");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d("database", "Value is: " + value);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<FlowerShop>>() {
                    }.getType();
                    List<FlowerShop> data = gson.fromJson(value, listType);
                    if (data != null){
                        DataHelper.getFlowerShopList().clear();
                        DataHelper.getFlowerShopList().addAll(data);
                    }
                }catch (Exception e){
                    Log.d("database", "exception: " +e.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("database", "Failed to read value.", error.toException());
            }
        });

    }
}
