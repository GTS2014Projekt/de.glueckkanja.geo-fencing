package de.glueckkanja.geofencing;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ListView.FixedViewInfo;


public class BeaconList extends Activity {
	private ExpandableListView elv_beaconList;
	private ArrayList<BeaconItem> beaconList = new ArrayList<BeaconItem>();
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	private static final String TAG = BeaconList.class.getSimpleName();
	private static final String serverURL = "HTTP://172.27.1.100:3000/users/updatedata";
	private BeaconManager myBeaconManager;
	private Beacon beacon;
	private Region region;
	private sendThread mySendThread;
	private myExpandableListViewAdapter myAdapter;
	private BluetoothManager myBM;
	private Intent myIntent;
	private String MAC_Address;
	private sendThread sendThread;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_list);
		//get Extra information from Intent
		myIntent = getIntent();
		MAC_Address = myIntent.getStringExtra("MAC");
		//Widgets
		elv_beaconList = (ExpandableListView) findViewById(R.id.elv_beaconList);
		myAdapter = new myExpandableListViewAdapter(getApplicationContext());
		elv_beaconList.setAdapter(myAdapter);	
		//set up sendThread
		sendThread = new sendThread(serverURL, MAC_Address);		
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
	    	public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
	    		runOnUiThread(new Runnable() {
	    			@Override
	    			//
	    			public void run() {
	    				if (beacons.isEmpty()){
	    					Toast.makeText(getApplicationContext(), "Keine Beacons gefunden!", Toast.LENGTH_SHORT).show();
	    				}
	    				//beaconList.clear();
	    				for(int i=0 ;i < beacons.size();i++){
	    					String MAC = beacons.get(i).getMacAddress();
	    					String name = beacons.get(i).getName();
	    					int minor = beacons.get(i).getMinor();
	    					int major = beacons.get(i).getMajor();
	    					int mPower = beacons.get(i).getMeasuredPower();
	    					int rssi = beacons.get(i).getRssi();
	    					String[] items = {String.valueOf(minor),String.valueOf(major) , String.valueOf(mPower), String.valueOf(rssi)};
	    					beaconList.add(new BeaconItem(MAC, items));	    				
	    				}	    				
	    				sendThread.run(beaconList);
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
	
	void beaconItemlist() {
		// TODO Auto-generated method stub
		String[] macadr = {"44-6D-57-1F-B4-32", "F1-G3-60-D4-14-12", "B1-21-C9-77-E1-F3", "D9-E4-9C-G1-B4-D3", "36-6D-13-60-A1-23"};
		for(int i=0; i<5; i++){
			String name = macadr[i];
			String[] child = {"Range: "+Math.round(10 * Math.random())+"m"};
			beaconList.add(new BeaconItem(name, child));
		}
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



