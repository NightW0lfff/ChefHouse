package com.example.housechefv03;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddGroceryItem extends AppCompatActivity {

    ArrayList<String> items, itemKeys, oldItems;
    ArrayAdapter<String> itemsAdapter;
    ListView listView;
    Button button;
    TextView text, textDone, listTitleView;
    DatabaseReference databaseReference;
    String listTitle;
    String key = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_item);



        listView = findViewById(R.id.groceryListView);
        button = findViewById(R.id.btn_addItem);
        text = findViewById(R.id.backText);
        textDone = findViewById(R.id.doneText);
        listTitleView = findViewById(R.id.listTitle);


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeList();
            }
        });

        textDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveListToDatabase(items);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        key = getIntent().getStringExtra("Key");
        itemKeys = getIntent().getStringArrayListExtra("ItemKey");

        listTitle = getIntent().getStringExtra("ListTitle");
        listTitleView.setText(listTitle);

        ArrayList<String> data = getIntent().getStringArrayListExtra("ListItems");

        items = new ArrayList<>();


        if(data != null) {
            items = new ArrayList<>(data);
        }

        oldItems = new ArrayList<>(items);

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();

        setUplistViewListener();
    }

    private void removeList() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("GroceryLists");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete it?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(key != null){
                            reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Context context = getApplicationContext();
                                    Toast.makeText(context, "List Removed!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing, stay on the current screen
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setUplistViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context,"Item Removed", Toast.LENGTH_LONG).show();

                items.remove(i);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText input = view.getRootView().findViewById(R.id.groceryListItem);
        String itemText = input.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        DatabaseReference listReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("GroceryLists");
        String listkey = listReference.getKey();

        if(key != null){
            listkey = key;
        }

        if(!itemText.isEmpty()){
            itemsAdapter.add(itemText);
//            DatabaseReference newRef = listReference.child(listkey);
//            newRef.child("items").push().setValue(itemText);
            input.setText("");
        }else{
            Toast.makeText(getApplicationContext(),"Please enter text ...", Toast.LENGTH_LONG).show();
        }
    }


    private void saveListToDatabase(ArrayList<String> items){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        DatabaseReference listReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("GroceryLists");

        // Create a new list entry with a unique key
        String listKey = listReference.push().getKey();
        if(key != null) {
            listKey = key;
        }
        DatabaseReference newListRef = listReference.child(listKey);

                    // Save the list title under the "title" child
        newListRef.child("title").setValue(listTitle)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean itemChanged = false;
                        if (task.isSuccessful()) {
                            // Save each item in the list under the "items" child
//                            for (String item : items) {
//                                if(!oldItems.contains(item)) {
//                                    newListRef.child("items").push().setValue(item);
//                                    itemChanged = true;
//                                }
//                            }

                            newListRef.child("items").removeValue();

                            for(String item: items){
                                newListRef.child("items").push().setValue(item);
                            }
                            if(itemChanged) {
                                Toast.makeText(getApplicationContext(), "List saved to database", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to save list to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}