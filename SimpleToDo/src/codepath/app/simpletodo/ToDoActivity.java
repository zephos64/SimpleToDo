package codepath.app.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity {
	private ArrayList<String> toDoItems;
	private ArrayAdapter<String> aToDoItems;
	private ListView lvItems;
	private EditText etNewItem;
	
	private final int REQUEST_CODE = 20;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        
        lvItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        
        readItems();
        aToDoItems = new ArrayAdapter<String>(
        		this,
        		android.R.layout.simple_list_item_1,
        		toDoItems);
        lvItems.setAdapter(aToDoItems);
        setupListViewListener();
    }
    
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	
    	try {
    		toDoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e) {
    		toDoItems = new ArrayList<String>();
    	}
    }

    private void writeItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	
    	try {
    		FileUtils.writeLines(todoFile, toDoItems);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /*
     * Corresponds to btnNewItem, add item from etNewItem to lvItems
     */
    public void onAddedItem(View v) {
    	String itemText = etNewItem.getText().toString();
    	aToDoItems.add(itemText);
    	etNewItem.setText("");
    	writeItems();
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				toDoItems.remove(pos);
				aToDoItems.notifyDataSetChanged();
				writeItems();
				return true;
			}
		});
    	
    	lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				Intent edit = new Intent(ToDoActivity.this, EditItemActivity.class);
				edit.putExtra("item", toDoItems.get(pos));
				edit.putExtra("itemLoc", pos);
				startActivityForResult(edit, REQUEST_CODE);
			}
		
    	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    		toDoItems.set(data.getIntExtra("itemLoc", -1),
    				data.getStringExtra("newItem"));
    		aToDoItems.notifyDataSetChanged();
    		writeItems();
    	}
    }
}
