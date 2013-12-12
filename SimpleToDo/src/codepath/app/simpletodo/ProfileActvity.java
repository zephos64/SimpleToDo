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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ProfileActvity extends Activity {
	private EditText etProfName;
	private ListView lvProfiles;
	private ArrayList<String> profiles;
	private ArrayAdapter<String> aProfiles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_actvity);
		
		etProfName = (EditText) findViewById(R.id.etProfName);
		
		lvProfiles = (ListView) findViewById(R.id.lvProfiles);
		readProfiles();
		aProfiles = new ArrayAdapter<String>(
        		this,
        		android.R.layout.simple_list_item_1,
        		profiles);
        lvProfiles.setAdapter(aProfiles);
        setupListViewListener();
	}
	
	private void readProfiles() {
    	File filesDir = getFilesDir();
    	profiles = new ArrayList<String>();
    	
    	for (String fileName : filesDir.list()) {
			if(fileName.contains("todo.txt")) {
				int temp = fileName.indexOf("todo.txt");
				profiles.add(fileName.substring(0, temp));
			}
		}
    }
	
	public void onProfileAdd(View v) {
		File filesDir = getFilesDir();
		String profName = etProfName.getText().toString();
		if(profName.isEmpty()) {
			Toast.makeText(this, "Please enter a name for the profile", Toast.LENGTH_SHORT).show();
			return;
		}
		
    	try {
			FileUtils.touch(new File(filesDir, profName+"todo.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	aProfiles.add(profName);
    	etProfName.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_actvity, menu);
		return true;
	}
	
	 private void setupListViewListener() {
	    	lvProfiles.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapter, View item,
						int pos, long id) {
					Intent newData = new Intent();
					newData.putExtra("profile", profiles.get(pos));
					setResult(RESULT_OK, newData);
					
					finish();
				}
	    	});
	    	
	    	lvProfiles.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> adapter, View item,
						int pos, long id) {
					if(profiles.get(pos).equals("Default")) {
						Toast.makeText(ProfileActvity.this, "Cannot delete default profile", Toast.LENGTH_SHORT).show();
						return true;
					} 
					
					File filesDir = getFilesDir();
					FileUtils.deleteQuietly(new File(filesDir, profiles.get(pos)+"todo.txt"));
					profiles.remove(pos);
					aProfiles.notifyDataSetChanged();

					return true;
				}
	    	});
	    }
}
