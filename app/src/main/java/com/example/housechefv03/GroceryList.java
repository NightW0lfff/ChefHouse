package com.example.housechefv03;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroceryList extends Fragment {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<GroceryItem> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    GroceryListAdapter adapter;
    ArrayList<String> titleList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list, container, false);

        fab = view.findViewById(R.id.fab_groceryList);
        recyclerView = view.findViewById(R.id.groceryRCView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        titleList = new ArrayList<>();

        adapter = new GroceryListAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("GroceryLists");


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();
                titleList.clear();
                for (DataSnapshot listSnapshot : snapshot.getChildren()){
                    ArrayList<String> items = new ArrayList<>();
                    ArrayList<String> itemKeys = new ArrayList<>();
                    String key = listSnapshot.getKey();
                    String title = listSnapshot.child("title").getValue().toString();
                    titleList.add(title);
                    for (DataSnapshot itemSnapshot : listSnapshot.child("items").getChildren()){
                        String item = itemSnapshot.getValue().toString();
                        String itemKey = itemSnapshot.getKey();
                        items.add(item);
                        itemKeys.add(itemKey);
                    }
                    GroceryItem groceryItem = new GroceryItem(title, items);
                    groceryItem.setKey(key);
                    groceryItem.setItemKey(itemKeys);
                    dataList.add(groceryItem);
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListTitleDialog();
            }
        });

        return view;
    }

    private void showListTitleDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Enter List Title");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_list_title, null);
        builder.setView(dialogView);

        EditText listTitleEditText = dialogView.findViewById(R.id.editTextListTitle);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = listTitleEditText.getText().toString().trim();
                if (!title.isEmpty() && !titleList.contains(title)) {
                    Intent intent = new Intent(getContext(), AddGroceryItem.class);
                    intent.putExtra("ListTitle", title);
                    startActivity(intent);
                } else if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a list title", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "The list name " + title + " is existed!\nPlease enter another name!", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}