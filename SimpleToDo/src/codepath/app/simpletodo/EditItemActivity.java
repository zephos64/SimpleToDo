package codepath.app.simpletodo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	private EditText etItemInfo;
	private int itemLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		Intent mainActivity = getIntent();
		
		etItemInfo = (EditText) findViewById(R.id.etItemInfo);
		
		etItemInfo.setText(mainActivity.getStringExtra("item"));
		itemLoc = mainActivity.getIntExtra("itemLoc", -1);
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

}
