/*
 * Author : Jan-Niklas Vierheller
 * Date: 25.06.2014
 * 
 * Summary: This is creating a Activity which displays all Estimote Beacons in range
 * 
 * Uses Estimote Technology and their Code!
 */

package de.glueckkanja.geofencing;

import java.text.DecimalFormat;
import java.util.*;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class BeaconListingActivity extends Activity {
	private ExpandableListView elv_beaconList;
	private ArrayList<BeaconItem> beaconList = new ArrayList<BeaconItem>();
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	private static final String TAG = BeaconListingActivity.class.getSimpleName();
	private static String serverURL;
	private BeaconManager myBeaconManager;
	private MyExpandableListViewAdapter myAdapter;
	private Intent myIntent;
	private String MAC_Address;
	private SendThread sendThread;
	private Sorter mySorter;
	DecimalFormat format = new DecimalFormat("#0.00"); 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beaconlisting);
		//get Extra information from Intent
		myIntent = getIntent();
		MAC_Address = myIntent.getStringExtra("MAC");
		serverURL = myIntent.getStringExtra("URL");
		//Widgets
		elv_beaconList = (ExpandableListView) findViewById(R.id.elv_beaconList);
		myAdapter = new MyExpandableListViewAdapter(getApplicationContext());
		elv_beaconList.setAdapter(myAdapter);			
		//Initialize Sorter
		mySorter = new Sorter();
		//Initialize BeaconManager
		beaconManager();
	}
	
	public BeaconListingActivity(){
		
	}
	public void beaconManager(){
		//set default BeaconManager
		myBeaconManager = new BeaconManager(this);
	    myBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
	    	@Override
	    	public void onBeaconsDiscovered(Region region, final List<Beacon> pulledBeacons) {
	    		runOnUiThread(new Runnable() {
	    			@Override
	    			//
	    			public void run() {
	    				beaconList.clear();
	    				if (pulledBeacons.isEmpty()){
	    					Toast.makeText(getApplicationContext(), "Keine Beacons gefunden!", Toast.LENGTH_SHORT).show();
	    				}else{
	    					for(int i=0 ;i < pulledBeacons.size();i++){	
		    					//Adding pulled informations into own List
	    						//Returns distance in meters based on beacon's RSSI and measured power. http://estimote.github.io/Android-SDK/JavaDocs/
	    						double range = Utils.computeAccuracy(pulledBeacons.get(i));
		    					beaconList.add(new BeaconItem(pulledBeacons.get(i).getName(), pulledBeacons.get(i).getMacAddress(), range, pulledBeacons.get(i).getMinor(), pulledBeacons.get(i).getMajor(), pulledBeacons.get(i).getMeasuredPower(), pulledBeacons.get(i).getRssi()));	    				
	    					}  
	    				}
	    				mySorter.InsertionSort(beaconList);
	    				myAdapter.refreshList(beaconList);
	    			}
	    		});
	    	}
	    });	
	
	}
	
	
	private void connectToService() {
	    myBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
	      @Override
	      public void onServiceReady() {
	        try {
	          myBeaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
	        } catch (RemoteException e) {
	        	Log.e(TAG, "Cannot start ranging", e);
	        }
	      }
	    });
	}
	
	public ArrayList<BeaconItem> getBeaconList(){
		return (beaconList);
	}
	
	public String getURL(){
		return(serverURL);
	}

	protected void onStart(){
		super.onStart();
		
		
		connectToService();
		
	}
	
	protected void onStop(){
		super.onStop();
		try {
		      myBeaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
		    } catch (RemoteException e) {
		     
		    }
	}
	
	protected void onDestroy(){
		super.onDestroy();
		myBeaconManager.disconnect();
	}
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_beaconlisting, menu);
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

	

}



