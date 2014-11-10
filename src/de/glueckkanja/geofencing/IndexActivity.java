/*
 * Author : Jan-Niklas Vierheller
 * Date: 25.06.2014
 * 
 * Summary: This is creating a Activity which displays Settings and gives access to other Activitys and Services
 * 
 * Uses Estimote Technology and their Code!
 */
package de.glueckkanja.geofencing;

import com.estimote.sdk.BeaconManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class IndexActivity extends Activity{
	//Displays
	private static final int REQUEST_ENABLE_BT = 1;
	private String MAC_Address;
	//Adapter
	private BluetoothAdapter myBluetoothAdapter;
	private BeaconManager myOverviewBeaconManager;
	private Intent intentService;
	private final BroadcastReceiver BTReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			Log.d("BluetoothState", context.toString());
			if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
				switch(state){
					case BluetoothAdapter.STATE_OFF:
						Log.d("BluetoothState","Received: Bluetooth Disconnected");
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					break;
					case BluetoothAdapter.STATE_ON:
						Log.d("BluetoothState","Received: Bluetooth Connected");
						tv_bluetoothState.setTextColor(Color.GREEN);
						tv_bluetoothState.setText(R.string.bluetoothState_enabled);
					break;							
				}
			}
			
		}
 		
 	};
	//Widgets
	private TextView tv_bluetoothState;
	private TextView tv_macAddress;
	private EditText e_URL;
	private Button b_beacons;
	private Button b_bluetooth;
	private Button b_ServiceON;
	private Button b_ServiceOFF;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		//Widgets
	 	tv_bluetoothState = (TextView) findViewById(R.id.tv_bluetoothState);
	 	tv_macAddress = (TextView) findViewById(R.id.tv_macAddress);
	 	e_URL = (EditText) findViewById(R.id.e_URL);
	 	b_beacons=(Button) findViewById(R.id.b_beacons);	 	
	 	b_bluetooth=(Button) findViewById(R.id.b_bluet);
	 	b_ServiceON=(Button) findViewById(R.id.b_ServiceON);
	 	b_ServiceOFF=(Button) findViewById(R.id.bServiceOFF); 
		
		//BluetoothAdapter
	 	myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	 	myOverviewBeaconManager = new BeaconManager(this);
		MAC_Address = myBluetoothAdapter.getAddress();
	 	tv_macAddress.setText(MAC_Address);
		 	 	
	 	//check Bluetooth state
	 	bluetoothState();
	 	
	 	if(android.os.Build.VERSION.SDK_INT >= 18){
	 		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            this.registerReceiver(BTReceiver, filter);
	 	}
	 	
	 	
	 	
	}
	
	public void bluetoothState(){
		//Called if Device does not support Bluetooth
		if (myBluetoothAdapter == null) {
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText(R.string.bluetoothState_none);
		}
		//Called if Bluetooth is enabled
		if (myBluetoothAdapter.isEnabled()) {
			tv_bluetoothState.setTextColor(Color.GREEN);
			tv_bluetoothState.setText(R.string.bluetoothState_enabled);

		}else{ // Called if not
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText(R.string.bluetoothState_disabled);
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		//Called if Device does not support Bluetooth low energy
		if (!myOverviewBeaconManager.hasBluetooth()) {
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText(R.string.bluetoothState_noLowEnergy);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //accepted Request
        if(resultCode == RESULT_OK){
        	tv_bluetoothState.setTextColor(Color.GREEN);
        	tv_bluetoothState.setText(R.string.bluetoothState_enabled);
        }//didn't accept Request
        if(resultCode == RESULT_CANCELED){
        	tv_bluetoothState.setTextColor(Color.RED);
        	tv_bluetoothState.setText(R.string.bluetoothState_disabled);
        }
    }
	
	
	protected void onResume(){
		super.onResume();
		if(!myBluetoothAdapter.isEnabled()){
			Toast.makeText(getApplicationContext(), R.string.bluetoothState_disabled, Toast.LENGTH_SHORT).show();
			//make Button Visible
			//b_bluetooth.setVisibility();
		}
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_index, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true; 
		}
		if(id == R.id.action_mode){
			
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void oc_Beacons(View v) {
		//Calls ListingActivity
		if(myBluetoothAdapter.isEnabled()){
			Intent i_beacon = new Intent(this, BeaconListingActivity.class);
			i_beacon.putExtra("MAC", MAC_Address);
			i_beacon.putExtra("URL", "HTTP://"+e_URL.getText().toString()+":3000/functions/update");
			startActivity(i_beacon);
		}else{
			bluetoothState();
		}
	}
	
	public void oc_bluetooth(View v) {
		//Calls bluetoothState function
		bluetoothState();	
	}
	
	public void oc_serviceON(View v) {
		//Calls SendingService
		if(intentService == null){
		intentService = new Intent(getBaseContext(), SendingService.class);
		intentService.putExtra("MAC", MAC_Address);
		intentService.putExtra("url", "HTTP://"+e_URL.getText().toString()+":3000/functions/update");
		startService(intentService);
		}else{
			Toast.makeText(getBaseContext(), "Service already running", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onNewIntent(Intent i){
		// Intent i filtern
		if(intentService==null){
			Log.d("ServiceIntent", "Kein Service");
			if(i.getClass().equals(SendingService.class)){
				intentService=i;
				Log.d("ServiceIntent", "neuer Service");
			}
		}
	}
	
	public void oc_serviceOFF(View v) {
		//Stops Service
		if(intentService != null){
			stopService(intentService);	
			intentService = null;
		}else{
			intentService = new Intent(getBaseContext(), SendingService.class);
			stopService(intentService);
		}
	}
}
