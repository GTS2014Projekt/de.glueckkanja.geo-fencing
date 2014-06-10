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

public class BeaconList extends Activity {
	private ExpandableListView elv_beaconList;
	private ArrayList<BeaconItem> beaconList = new ArrayList<BeaconItem>();
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	private static final String TAG = BeaconList.class.getSimpleName();
	private static String serverURL;
	private BeaconManager myBeaconManager;
	private myExpandableListViewAdapter myAdapter;
	private Intent myIntent;
	private String MAC_Address;
	private sendThread sendThread;
	DecimalFormat f = new DecimalFormat("#0.00"); 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_list);
		//get Extra information from Intent
		myIntent = getIntent();
		MAC_Address = myIntent.getStringExtra("MAC");
		serverURL = myIntent.getStringExtra("URL");
		//Widgets
		elv_beaconList = (ExpandableListView) findViewById(R.id.elv_beaconList);
		myAdapter = new myExpandableListViewAdapter(getApplicationContext());
		elv_beaconList.setAdapter(myAdapter);	
		//set up sendThread
		sendThread = new sendThread(this, serverURL, MAC_Address);		
		//Initialize BeaconManager
		beaconManager();
	}
	
	public BeaconList(){
		
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
		    					double range = Utils.computeAccuracy(pulledBeacons.get(i));
		    					beaconList.add(new BeaconItem(pulledBeacons.get(i).getName(), pulledBeacons.get(i).getMacAddress(), f.format(range), pulledBeacons.get(i).getMinor(), pulledBeacons.get(i).getMajor(), pulledBeacons.get(i).getMeasuredPower(), pulledBeacons.get(i).getRssi()));	    				
	    					}
	    				//call function to send	  
	    				sendThread.run(beaconList);  
	    				}
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
	
	
/*	
	//sorting the Beaconlist
	public void sortBeaconList(ArrayList<BeaconItem> sortList){
		qSort(sortList, 0, sortList.size()-1);
		
	}
	
	public void qSort(ArrayList<BeaconItem> sortList, int links, int rechts) {
	      if (links < rechts) {
	         int i = partition(sortList,links,rechts);
	         qSort(sortList,links,i-1);
	         qSort(sortList,i+1,rechts);
	      }
	}
	
	 public int partition(ArrayList<BeaconItem> sortList, int links, int rechts) {
	      int  i, j;
	      String pivot;
	      pivot = sortList.get(rechts).getName();               
	      i     = links;
	      j     = rechts-1;
	      while(i<=j) {
	         if (sortList.get(i).getName().compareTo(pivot) > 0) {     
	            // tausche x[i] und x[j]
	            Collections.swap(sortList, i, j);                             
	            j--;
	         } else i++;            
	      }
	      // tausche x[i] und x[rechts]
	      Collections.swap(sortList, i, j);
	        
	      return i;
	   }
*/	
	
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
		getMenuInflater().inflate(R.menu.beacon_list, menu);
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



