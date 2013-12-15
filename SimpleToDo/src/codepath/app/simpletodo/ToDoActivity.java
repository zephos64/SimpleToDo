package codepath.app.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoActivity extends Activity {
	private ArrayList<String> toDoItems;
	private ArrayAdapter<String> aToDoItems;
	private ListView lvItems;
	private EditText etNewItem;
	private TextView txCurrProf;
	private Button btnAddItem;
	
	private final int EDIT_REQUEST_CODE = 20;
	private final int PROFILE_REQUEST_CODE = 21;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        
        lvItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        txCurrProf = (TextView) findViewById(R.id.txCurrProf);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setEnabled(false);
        
        loadPrevProfile();
        readItems(txCurrProf.getText().toString());
        
        setupListViewListener();
    }
    
    private void loadPrevProfile() {
    	File filesDir = getFilesDir();
    	File propFile = new File(filesDir, "ToDo.prop");
    	
    	try {
    		FileUtils.touch(propFile);
    		
    		// First time creating, so create property file and populate it
    		// Should write, so solve edge case if they do not change profile
    		//  then no profile written
    		if(FileUtils.sizeOf(propFile) == 0) {
    			FileUtils.write(propFile, "Default");
    		}
    		
    		String profile = FileUtils.readLines(propFile).get(0);
    		
    		// This is for edge case when profile deleted, but leave
    		// app on profile screen. Should choose new random profile
    		if(checkProfExists(profile)) {
    			txCurrProf.setText(profile);
    		} else {
    			for (String fileName : filesDir.list()) {
    				if(fileName.contains("todo.txt")) {
    					txCurrProf.setText(fileName.substring(0, fileName.indexOf("todo.txt")));
    				}
    			}
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}
    
    private void saveCurrProfile() {
    	File filesDir = getFilesDir();
    	File propFile = new File(filesDir, "ToDo.prop");
    	
    	try {
    		FileUtils.write(propFile, txCurrProf.getText().toString());
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private boolean checkProfExists(String prof) {
    	File filesDir = getFilesDir();
    	String[] listOfProf = filesDir.list();
    	
    	for (String profile : listOfProf) {
    		if((profile+"todo.txt").equals(prof)) {
    			return true;
    		}
    	}
    	
    	return false;
    }

	private void readItems(String profile) {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, profile+"todo.txt");
    	
    	try {
    		toDoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e) {
    		toDoItems = new ArrayList<String>();
    	}
    	
    	// Move here so profiles will properly update lvItems
    	aToDoItems = new ArrayAdapter<String>(
        		this,
        		android.R.layout.simple_list_item_1,
        		toDoItems);
        lvItems.setAdapter(aToDoItems);
    }

    private void writeItems(String profile) {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, profile+"todo.txt");
    	
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
    	writeItems(txCurrProf.getText().toString());
    }
    
    public void onProfileChange(View v) {
    	Intent prof = new Intent(ToDoActivity.this, ProfileActvity.class);
    	startActivityForResult(prof, PROFILE_REQUEST_CODE);
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				toDoItems.remove(pos);
				aToDoItems.notifyDataSetChanged();
				writeItems(txCurrProf.getText().toString());
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
				startActivityForResult(edit, EDIT_REQUEST_CODE);
			}
		
    	});
    	etNewItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				if(etNewItem.getText().length() == 0) {
					btnAddItem.setEnabled(false);
				} else {
					btnAddItem.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
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
    	if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
    		toDoItems.set(data.getIntExtra("itemLoc", -1),
    				data.getStringExtra("newItem"));
    		aToDoItems.notifyDataSetChanged();
    		writeItems(txCurrProf.getText().toString());
    	} else if (resultCode == RESULT_OK && requestCode == PROFILE_REQUEST_CODE) {
    		txCurrProf.setText(data.getStringExtra("profile"));
    		
    		readItems(txCurrProf.getText().toString());
    		saveCurrProfile();
    	}
    }
}
