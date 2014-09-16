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
import android.os.AsyncTask;
import android.util.Log;

public class BackGroundSending extends AsyncTask<String, Void, JSONArray> {
	//Is the Asyncronous Task that runs in Background
	protected JSONArray doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		String mac = params[1];
		String data = params[2];
		Log.d("SendingService", url+" "+mac+" "+data);
		return jsonSending(url, mac, data);
	}
	
	private JSONArray jsonSending(String url, String mac, String data){
		String result11="";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("macAdress", mac));
			nameValuePairs.add(new BasicNameValuePair("beacons", data));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Execute HTTP Post Request
			Log.d("SendingService", "Vor senden");
			HttpResponse response = httpclient.execute(httppost);
			Log.d("SendingService", "Nach senden");
			
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
			Log.d("SendingService", e.toString());    
			e.printStackTrace();
			    return null;
		}
	}
		
	@Override
	protected void onPostExecute(JSONArray result) {
		if (result != null) {
			// sending worked fine
		}else{
			// error occured
			//Toast.makeText(classBeaconList, "An Error occured", Toast.LENGTH_LONG).show();
		}
	}	
}
