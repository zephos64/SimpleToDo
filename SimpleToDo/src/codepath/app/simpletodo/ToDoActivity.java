package codepath.app.simpletodo;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity {
	private ArrayList<String> toDoItems;
	private ArrayAdapter<String> aToDoItems;
	private ListView lvItems;
	private EditText etNewItem;

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
    	toDoItems = new ArrayList<String>();
    	toDoItems.add("Item 1");
    	toDoItems.add("Item 2");
    	toDoItems.add("Item 3");
    }

    private void writeItems() {
    	
    }
    
    /*
     * Corresponds to btnNewItem, add item from etNewItem to lvItems
     */
    public void onAddedItem(View v) {
    	String itemText = etNewItem.getText().toString();
    	aToDoItems.add(itemText);
    	etNewItem.setText("");
    	
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				toDoItems.remove(pos);
				aToDoItems.notifyDataSetChanged();
				return true;
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }
}
