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

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class sendingService extends Service {
	//debug
		private int counter=0;
	//To manipulate update time
		final private int timerDuration= 15000;
	//Attributes
	private Handler handler = new Handler();
	private String url;
	private String mac;
	private ArrayList<BeaconItem> beaconList;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		Toast.makeText(getBaseContext(), "Started Service", Toast.LENGTH_LONG).show();
		handler.postDelayed(new Runnable(){
			@Override
			public void run(){
				Log.d("Handler", "Durchlauf" + counter++);
				if(beaconList != null){
					String data = createData(beaconList);
					if(data != null && !data.isEmpty()){				
							new DoBackgroundTask().execute(url, mac, data);
					}else{
						Toast.makeText(getBaseContext(), "no Data", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getBaseContext(), "beaconList not initialized", Toast.LENGTH_SHORT).show();
				}
				handler.postDelayed(this, timerDuration);
			}
			
		}, timerDuration);
		return START_STICKY;
	}	
	
	public void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
	}
	
	
	public String createData(ArrayList<BeaconItem> beaconList){
		String data = "";
		for(int i = 0; i<beaconList.size(); i++){
			if(i== 0){
				//no "#" in first position
				data += beaconList.get(i).getMAC_Address()+"#"+beaconList.get(i).getRange();
			}else{
				data += "#"+beaconList.get(i).getMAC_Address()+"#"+beaconList.get(i).getRange();
			}						
		}
		return data;
	}
	
	//Is the Asyncronous Task that runs in Background
	private class DoBackgroundTask extends AsyncTask<String, Void, JSONArray>{

		@Override
		protected JSONArray doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			String mac = params[1];
			String data = params[2];			
			return jsonSending(url, mac, data);
		}
		
		private JSONArray jsonSending(String url, String mac, String data){
			String result11="";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("macadresse", mac));
				nameValuePairs.add(new BasicNameValuePair("beacons", data));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");
				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				reader.close();
				Log.d("Sending", "Sendet daten!");
				result11 = sb.toString();
				return new JSONArray(result11);
				// parsing data
			} catch (Exception e) {
				    e.printStackTrace();
				    return null;
			}
				
		}		
	}//end Class
	
	

}
