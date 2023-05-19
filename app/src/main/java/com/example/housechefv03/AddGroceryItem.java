package com.example.housechefv03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddGroceryItem extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listView;
    Button button;
    TextView text, listTitleView;
    DatabaseReference databaseReference;
    String listTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_item);

        listTitle = getIntent().getStringExtra("ListTitle");

        listView = findViewById(R.id.groceryListView);
        button = findViewById(R.id.btn_addItem);
        text = findViewById(R.id.backText);
        listTitleView = findViewById(R.id.listTitle);

        listTitleView.setText(listTitle);

        databaseReference = FirebaseDatabase.getInstance().getReference("GroceryItems");

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
//        setUplistViewListener();
    }



    private void setUplistViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context,"Item Removed", Toast.LENGTH_LONG).show();

                // Remove the item from list view

                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                // Remove the item from the database
                removeItemFromDatabase(i);

                return true;
            }
        });
    }


    private void addItem(View view) {
        EditText input = view.getRootView().findViewById(R.id.groceryListItem);
        String itemText = input.getText().toString();

        if(!itemText.isEmpty()){
            itemsAdapter.add(itemText);
            input.setText("");

            saveItemToDatabase(itemText);
        }else{
            Toast.makeText(getApplicationContext(),"Please enter text ...", Toast.LENGTH_LONG).show();
        }
    }

    private void saveItemToDatabase(String itemText) {
        // Generate a unique key for the item
        String itemId = databaseReference.child(listTitle).push().getKey();

        // Create a GroceryItem object with the item name
        GroceryItem groceryItem = new GroceryItem(itemText);

        // Save the item to the database using the generated key
        databaseReference.child(listTitle).child(itemId).setValue(groceryItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Item saved to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to save item to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void removeItemFromDatabase(int position) {
        // Get the item text at the specified position
        String itemText = items.get(position);

        // Find the item key in the database based on the item text
        Query query = databaseReference.orderByChild("itemName").equalTo(itemText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemKey = snapshot.getKey();

                    // Remove the item from the database using the key
                    databaseReference.child(itemKey).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Item removed from database", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to remove item from database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error querying database", Toast.LENGTH_SHORT).show();
            }
        });
    }

}