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

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class sendThread extends AsyncTask<Void, Void, JSONArray> {
	//Attributes
	private ArrayList<BeaconItem> beaconList = new ArrayList<BeaconItem>(); 
	private BeaconList myclassBeaconList;
	private String URL;
	private String Mac;
	private String data;
	
	//Needed for SendTimer
	private int duration=15000;
	private boolean Running = true;
	private Thread sceduler;
	
	public sendThread(BeaconList classbeaconList, String URL, String Mac, String data){
		this.myclassBeaconList = classbeaconList;
		this.URL = URL;
		this.Mac = Mac;
		this.data = data;
		//set up Sceduler
		sceduler = new Thread(new Runnable(){
			@Override
			public void run(){
				while(Running){
					try {
						Thread.sleep(duration);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sending(myclassBeaconList.getBeaconList());
				}
				
			}
		});
	}
	
	public sendThread(String URL, String Mac, String data){
		this.URL = URL;
		this.Mac = Mac;
		this.data = data;
	}
	
	public void startSceduler(){
		sceduler.start();
	}
	public void stopSceduler(){
		sceduler.interrupt();
	}
	
	public void sending(ArrayList<BeaconItem> beaconList){
		data = createData(beaconList);
		if(data != null && !data.isEmpty()){
			Log.d("Sending", "Sending Method");
			new sendThread(URL, Mac, data).execute();
		}
	}
	
	public String createData(ArrayList<BeaconItem> beaconList){
		data = "";
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
	
	public void setRunning(boolean b){
		this.Running = b;
	}
	
	protected JSONArray doInBackground(Void... params) {
	        try {
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(URL);

	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	            nameValuePairs.add(new BasicNameValuePair("macAdress", Mac));
	            nameValuePairs.add(new BasicNameValuePair("beacons", data));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	            // Execute HTTP Post Request
	            Log.d("Sending", "Before execute");
	            HttpResponse response = httpclient.execute(httppost);
	            Log.d("Sending", "After execute");
	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            sb.append(reader.readLine() + "\n");
	            String line = "0";
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            reader.close();
	            String result11 = sb.toString();

	            // parsing data
	            return new JSONArray(result11);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    @Override
	    protected void onPostExecute(JSONArray result) {
	        if (result != null) {
	            // sending worked fine
	        } else {
	            // error occured
	        	//Toast.makeText(classBeaconList, "An Error occured", Toast.LENGTH_LONG).show();
	        }
	    }
}
