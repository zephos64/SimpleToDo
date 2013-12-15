package codepath.app.simpletodo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	private EditText etItemInfo;
	private Button btnSaveEdit;
	private int itemLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		Intent mainActivity = getIntent();
		itemLoc = mainActivity.getIntExtra("itemLoc", -1);
		
		btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);
		
		etItemInfo = (EditText) findViewById(R.id.etItemInfo);
		etItemInfo.setText(mainActivity.getStringExtra("item"));
		etItemInfo.setSelection(etItemInfo.getText().length());
		etItemInfo.requestFocus();
		
		setUpViewListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	public void onSaveEdit(View V) {
		Intent newData = new Intent();
		newData.putExtra("newItem", etItemInfo.getText().toString());
		newData.putExtra("itemLoc", itemLoc);
		setResult(RESULT_OK, newData);
		
		finish();
	}
	
	private void setUpViewListener() {
		etItemInfo.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (etItemInfo.getText().length() == 0) {
					btnSaveEdit.setEnabled(false);
				} else {
					btnSaveEdit.setEnabled(true);
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
