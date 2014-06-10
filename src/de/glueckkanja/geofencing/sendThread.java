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
import android.widget.Toast;

public class sendThread extends AsyncTask<Void, Void, JSONArray> {
	private ArrayList<BeaconItem> beaconList = new ArrayList<BeaconItem>(); 
	private final String URL="HTTP://172.27.1.157:3000/users/updatedata";
	private String Mac;
	
	private Context classBeaconList;
	private String data;
	
	public sendThread(Context context, String URL, String Mac){
		//this.URL = URL;
		this.Mac = Mac;
		this.classBeaconList =context;
	}
	public sendThread(String URL, String Mac, String data){
		//this.URL = URL;
		this.Mac = Mac;
		this.data = data;
	}
	public void run(ArrayList<BeaconItem> beaconList){
		this.beaconList = beaconList;
		createData();
		new sendThread(URL, Mac, data).execute();
	}
	public void createData(){
		data = "";
		for(int i = 0; i<beaconList.size(); i++){
			if(i== 0){
				//no "#" in first position
				data += beaconList.get(i).getMAC_Address()+"#"+beaconList.get(i).getRange();
			}else{
				data += "#"+beaconList.get(i).getMAC_Address()+"#"+beaconList.get(i).getRange();
			}						
		}
	}
	
	protected JSONArray doInBackground(Void... params) {
	        try {
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(URL);

	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	            nameValuePairs.add(new BasicNameValuePair("macadresse", Mac));
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
	            // do something
	        } else {
	            // error occured
	        	//Toast.makeText(classBeaconList, "An Error occured", Toast.LENGTH_LONG).show();
	        }
	    }
}
