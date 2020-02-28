package com.example.firebaserecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    StorageReference reference;
    FirebaseRecyclerOptions<User> options;
    FirebaseRecyclerAdapter<User,ArtistViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.rec);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("Register");
        reference = FirebaseStorage.getInstance().getReference();
        options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(databaseReference, User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, ArtistViewHolder>(options) {
            @NonNull
            @Override
            public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null);
                return new ArtistViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ArtistViewHolder holder, int position, @NonNull User model) {
                holder.Name.setText(model.getName());
                holder.major.setText(model.getMajor());
                holder.Year.setText(model.getYear());
                Picasso.with(Main2Activity.this).load(model.getImageUrl()).into(holder.img);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    @Override

    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }
}
