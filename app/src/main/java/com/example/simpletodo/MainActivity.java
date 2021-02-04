package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        List<String> items;
        Button BtnAdd;
        EditText ETitem;
        RecyclerView RVitem;
        itemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnAdd = findViewById(R.id.BtnAdd);
        ETitem = findViewById(R.id.ETitem);
        RVitem = findViewById(R.id.RVitems);


        LoadItems();

        itemsAdapter.OnLongClickListener onLongClickListener = new itemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);
                //notify the position of the deleted item
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
         itemsAdapter = new itemsAdapter(items, onLongClickListener );
        RVitem.setAdapter(itemsAdapter);
        RVitem.setLayoutManager(new LinearLayoutManager(this));

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When button is pressed, the text within the user's text box is grabbed and stored as a string
                String todoItem = ETitem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify Adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                ETitem.setText("");
                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    // This function will load items by reading every line of the data file
    private void LoadItems() {

    try {
        items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
        // This function saves items by writing them into the data file
        private void saveItems() {
            try {
                FileUtils.writeLines(getDataFile(), items);
            } catch (IOException e) {
                Log.e("Main Activity", "Error writing items");
            }
        }

}