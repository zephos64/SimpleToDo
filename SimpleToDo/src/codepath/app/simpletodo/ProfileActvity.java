package codepath.app.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ProfileActvity extends Activity {
	private EditText etProfName;
	private ListView lvProfiles;
	private Button btnAddProf;
	private ArrayList<String> profiles;
	private ArrayAdapter<String> aProfiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_actvity);

		etProfName = (EditText) findViewById(R.id.etProfName);
		btnAddProf = (Button) findViewById(R.id.btnAddProf);
		btnAddProf.setEnabled(false);

		lvProfiles = (ListView) findViewById(R.id.lvProfiles);
		readProfiles();
		aProfiles = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, profiles);
		lvProfiles.setAdapter(aProfiles);
		setupListViewListener();
	}

	private void readProfiles() {
		File filesDir = getFilesDir();
		profiles = new ArrayList<String>();

		for (String fileName : filesDir.list()) {
			if (fileName.contains("todo.txt")) {
				profiles.add(fileName.substring(0, fileName.indexOf("todo.txt")));
			}
		}
	}

	public void onProfileAdd(View v) {
		File filesDir = getFilesDir();
		String profName = etProfName.getText().toString();

		try {
			FileUtils.touch(new File(filesDir, profName + "todo.txt"));
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
			public void onItemClick(AdapterView<?> adapter, View item, int pos,
					long id) {
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
				String deleteProfMsg = getResources().getString(
						R.string.deleteProfMsg);
				deleteProfMsg = deleteProfMsg.replace(
						getResources().getString(R.string.temp),
						profiles.get(pos).toString());

				final int profPos = pos;
				new AlertDialog.Builder(ProfileActvity.this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.deleteProf)
						.setMessage(deleteProfMsg)
						.setPositiveButton(R.string.yes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (profiles.size() <= 1) {
											Toast.makeText(
													ProfileActvity.this,
													"Cannot delete last profile",
													Toast.LENGTH_SHORT).show();
										} else {
											File filesDir = getFilesDir();
											FileUtils.deleteQuietly(new File(
													filesDir, profiles
															.get(profPos)
															+ "todo.txt"));
											profiles.remove(profPos);
											aProfiles.notifyDataSetChanged();
										}
									}
								}).setNegativeButton(R.string.no, null).show();
				return true;
			}
		});

		etProfName.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (etProfName.getText().length() == 0) {
					btnAddProf.setEnabled(false);
				} else {
					btnAddProf.setEnabled(true);
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
}
