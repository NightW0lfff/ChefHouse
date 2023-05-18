package com.example.housechefv03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddGroceryItem extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listView;
    Button button;
    TextView text;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_item);

        listView = findViewById(R.id.groceryListView);
        button = findViewById(R.id.btn_addItem);
        text = findViewById(R.id.backText);

        databaseReference = FirebaseDatabase.getInstance().getReference("GroceryItems");

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnsavedChangesDialog();
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
        setUplistViewListener();
    }

    private void setUplistViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context,"Item Removed", Toast.LENGTH_LONG).show();

                // Remove the item from list view
//                removeItemFromDatabase(i);
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();

                // Remove the item from the database
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
        String itemId = databaseReference.push().getKey();

        // Create a GroceryItem object with the item name
        GroceryItem groceryItem = new GroceryItem(itemText);

        // Save the item to the database using the generated key
        databaseReference.child(itemId).setValue(groceryItem)
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


/*
    private void removeItemFromDatabase(int position) {
        // Get the item key at the specified position
        String itemKey = databaseReference.getKey(position);

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
*/


    private void showUnsavedChangesDialog() {
        if (!items.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You have unsaved changes. Are you sure you want to navigate back?")
                    .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
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
        } else {
            finish();
        }
    }


}