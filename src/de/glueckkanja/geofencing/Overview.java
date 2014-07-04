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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Overview extends Activity{
	private static final int REQUEST_ENABLE_BT = 1;
	private TextView tv_bluetoothState;
	private TextView tv_macAddress;
	private EditText e_URL;
	private Button b_beacons;
	private Button b_bluetooth;
	private BluetoothAdapter myBluetoothAdapter;
	private BeaconManager myOverviewBeaconManager;
	private String MAC_Address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		//Widgets
	 	tv_bluetoothState = (TextView) findViewById(R.id.tv_bluetoothState);
	 	tv_macAddress = (TextView) findViewById(R.id.tv_macAddress);
	 	e_URL = (EditText) findViewById(R.id.e_URL);
	 	b_beacons=(Button) findViewById(R.id.b_beacons);	 	
	 	b_bluetooth=(Button) findViewById(R.id.b_bluetooth);
		
		//BluetoothAdapter
	 	myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	 	myOverviewBeaconManager = new BeaconManager(this);
	 	MAC_Address = myBluetoothAdapter.getAddress();
	 	tv_macAddress.setText(myBluetoothAdapter.getAddress());
		 
	 	//check Bluetooth state	
	 	bluetoothState();	
	}
	
	public void bluetoothState(){
		//Called if Device does not support Bluetooth
		if (myBluetoothAdapter == null) {
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText("Gerät unterstützt kein Bluetooth!");
		}
		//Called if Bluetooth is enabled
		if (myBluetoothAdapter.isEnabled()) {
			tv_bluetoothState.setTextColor(Color.GREEN);
			tv_bluetoothState.setText("Bluetooth aktiviert!");
			
		}else{ // Called if not
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText("Bluetooth ist aus!");
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		//Called if Device does not support Bluetooth low energy
		if (!myOverviewBeaconManager.hasBluetooth()) {
			tv_bluetoothState.setTextColor(Color.RED);
			tv_bluetoothState.setText("Gerät unterstützt kein Bluetooth-Low-Energy");
		}
	}
	
	
	//Called after Bluetooth turned on
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //accepted Request
        if(resultCode == RESULT_OK){
        	tv_bluetoothState.setTextColor(Color.GREEN);
        	tv_bluetoothState.setText("Bluetooth aktiviert!");
        }//didn't accept Request
        if(resultCode == RESULT_CANCELED){
        	tv_bluetoothState.setTextColor(Color.RED);
        	tv_bluetoothState.setText("Bluetooth ist aus!");
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
		if(myBluetoothAdapter.isEnabled()){
			Intent i_beacon = new Intent(this, BeaconList.class);
			i_beacon.putExtra("MAC", MAC_Address);
			i_beacon.putExtra("URL", "HTTP://"+e_URL.getText().toString()+":3000/functions/update");
			startActivity(i_beacon);
		}else{
			bluetoothState();
		}
	}
	public void oc_bluetooth(View v) {
		// TODO Auto-generated method stub
		bluetoothState();	
	}
}
