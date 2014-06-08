package de.glueckkanja.geofencing;

import com.estimote.sdk.BeaconManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Overview extends Activity{
	private static final int REQUEST_ENABLE_BT = 1;
	private TextView tv_bluetoothState;
	private TextView tv_macAddress;
	private Button b_beacons;
	private Button b_bluetooth;
	private BluetoothAdapter myBluetoothAdapter;
	private BeaconManager myOverviewBeaconManager;
	private String MAC_Address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		//BluetoothAdapter
	 	myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	 	myOverviewBeaconManager = new BeaconManager(this);
		//Widgets
	 	tv_bluetoothState = (TextView) findViewById(R.id.tv_bluetoothState);
	 	tv_macAddress = (TextView) findViewById(R.id.tv_macAddress);
	 	b_beacons=(Button) findViewById(R.id.b_beacons);
	 	
	 	b_bluetooth=(Button) findViewById(R.id.b_bluetooth);
	 	
	 	//end Widgets  
	 	MAC_Address = myBluetoothAdapter.getAddress();
	 	tv_macAddress.setText(myBluetoothAdapter.getAddress());
		 
	 	//check Bluetooth state	
	 	bluetoothState();
	
	}
	
	public void bluetoothState(){
		if (myBluetoothAdapter == null) {
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText("Gerät unterstützt kein Bluetooth!");
		}
		if (myBluetoothAdapter.isEnabled()) {
			tv_bluetoothState.setTextColor(Color.GREEN);
			tv_bluetoothState.setText("Bluetooth aktiviert!");
			
		}else{
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText("Bluetooth ist aus!");
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		   
		    
		}
		if (!myOverviewBeaconManager.hasBluetooth()) {
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText("gerät unterstützt kein Bluetooth-Low-Energy");
		}
	}
	
	
	
	
	
	protected void onStart(){
		super.onStart();
	}
	
	protected void onRestart(){
		super.onRestart();	
		
	}
	protected void onResume(){
		super.onResume();
		if(!myBluetoothAdapter.isEnabled()){
			Toast.makeText(getApplicationContext(), "Bluetooth ist Aus!", Toast.LENGTH_SHORT).show();
			//make Button Visible
			b_bluetooth.setVisibility(0);
		}
		
	}

	protected void onPause(){
		super.onPause();
	}
	
	protected void onStop(){
		super.onStop();
	}
	
	protected void onDestroy(){
		super.onDestroy();
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overview, menu);
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
		return super.onOptionsItemSelected(item);
	}

	
	public void oc_Beacons(View v) {
		// TODO Auto-generated method stub
		Intent i_beacon = new Intent(this, BeaconList.class);
		i_beacon.putExtra("MAC", MAC_Address);
		startActivity(i_beacon);	
	}
	public void oc_bluetooth(View v) {
		// TODO Auto-generated method stub
		bluetoothState();	
	}
}
