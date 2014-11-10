/*
 * Author : Jan-Niklas Vierheller
 * Date: 25.06.2014
 * 
 * Summary: This is creating a Service which sends ranged Beacons to a Server in a constant timerate
 * 
 * Uses Estimote Technology and their Code!
 */
package de.glueckkanja.geofencing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class SendingService extends Service {
	//debug
	private int counter=0;
	protected static final String TAG = "SendingService";
	//To manipulate update time
	final private int timerDuration= 15000;
	//Attributes
	private Handler handler = new Handler();
	boolean running = true;
	private String url;
	private String mac;
	private ArrayList<BeaconItem> beaconList = new ArrayList<BeaconItem>();
	private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
	

	private BeaconManager beaconManager = new BeaconManager(this);

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		url = intent.getStringExtra("url");
		mac = intent.getStringExtra("MAC");
		Toast.makeText(getBaseContext(), "Started Service", Toast.LENGTH_LONG).show();
		Log.d(TAG, "Started Service");
		//initialize Ranging Listener
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region, List<Beacon> pulledBeacons) {
				// TODO Auto-generated method stub
				//Log.d(TAG, "Ranged beacons: " + pulledBeacons.toString());
		    	
				if (pulledBeacons.isEmpty()){
					Toast.makeText(getApplicationContext(), "Keine Beacons gefunden!", Toast.LENGTH_SHORT).show();
				}else{
					//Log.d(TAG, "Pulling beacons");
					for(int i=0 ;i < pulledBeacons.size();i++){	
    					//Adding pulled informations into own List
						//Returns distance in meters based on beacon's RSSI and measured power. http://estimote.github.io/Android-SDK/JavaDocs/
						String currentMAC = pulledBeacons.get(i).getMacAddress();
						boolean isInside = false;
						int index=0;
						for(int j=0;j<beaconList.size();j++){
							if(beaconList.get(j).getMAC_Address().compareTo(currentMAC)==0){
								isInside=true;
								index=j;
							}
						}
						if(isInside){
							double range = Utils.computeAccuracy(pulledBeacons.get(i));
							beaconList.get(index).setNewRange(range);
						}else{
							double range = Utils.computeAccuracy(pulledBeacons.get(i));						
	    					beaconList.add(new BeaconItem(pulledBeacons.get(i).getName(), pulledBeacons.get(i).getMacAddress(), range, pulledBeacons.get(i).getMinor(), pulledBeacons.get(i).getMajor(), pulledBeacons.get(i).getMeasuredPower(), pulledBeacons.get(i).getRssi()));	    			
						}						
					}  
				}
			}
		});
		
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
					Log.d(TAG, "Starting Ranging");
				} catch (RemoteException e) {
					Log.e(TAG, "Cannot start ranging", e);
				}
		    }
		});
		
		
		handler.postDelayed(new Runnable(){
			@Override
			public void run(){
				Log.d("SendingService", "Durchlauf" + counter++);
				Toast.makeText(getBaseContext(), String.valueOf(counter), Toast.LENGTH_SHORT).show();
				
				String data;
				if(!beaconList.isEmpty()){
					data = createData(beaconList);
					Log.d("SendingService", "Start Backgroundtask");
					Log.d("SendingService", data);
					new BackGroundSending().execute(url, mac, data);
				}else{
					data=null;
				}			
				beaconList.clear();					
				if(running){
					handler.postDelayed(this, timerDuration);
				}
			}
			
		}, timerDuration);
		return START_STICKY;
	}	
	
	public void onDestroy(){
		super.onDestroy();
		running=false;
		try {
			beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
		} catch (RemoteException e) {
			Log.e(TAG, "Cannot stop but it does not matter now", e);
		}
		Toast.makeText(getBaseContext(), "Service stopped", Toast.LENGTH_LONG).show();
	}
	
	
	public String createData(ArrayList<BeaconItem> beaconList){
		String data = "";
		for(int i = 0; i<beaconList.size(); i++){
			if(i== 0){
				//no "#" in first position
				data += beaconList.get(i).getMAC_Address()+"#"+beaconList.get(i).computeAverageRange();
			}else{
				data += "#"+beaconList.get(i).getMAC_Address()+"#"+beaconList.get(i).computeAverageRange();
			}						
		}
		return data;
	}

}
