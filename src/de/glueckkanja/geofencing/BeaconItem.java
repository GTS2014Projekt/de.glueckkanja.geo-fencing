/*
 * 
 * Author : Jan-Niklas Vierheller
 * Date: 25.06.2014
 * 
 * Summary: This Code helps to store Informations from Estimote Beacons in a List
 * 
 * Uses Estimote Technology and their Code!
 */

package de.glueckkanja.geofencing;

import android.util.Log;

public class BeaconItem {
	//Attributes
	private String[]childList;
	private String name;
	private String mac_Address;
	private double initRange; 
	private int minor;
	private int major;
	private int mPower;
	private int rssi;	
	
	private int counter=0;
	private double sumRange=0;
	//end Attributes
	
	public BeaconItem(String name, String mac_Address, double range, int minor, int major, int mPower, int rssi){
		this.name=name;
		this.mac_Address = mac_Address;
		this.initRange = range;
		this.sumRange+=range;
		this.counter++;
		this.minor = minor;
		this.major = major;
		this.mPower = mPower;
		this.rssi = rssi;
		this.childList = createChildList(name, mac_Address, range, minor, major, mPower, rssi);
	}
	
	public void setNewRange(double range){
		sumRange+=range;
		this.counter++;
	}
	
	public double computeAverageRange(){
		double retrn = this.sumRange/this.counter;
		Log.d("SendingService", String.valueOf(retrn));		
		this.counter=0;
		return retrn;
	}
	
	public String[] createChildList(String name, String mac_Address, double range, int minor, int major, int mPower, int rssi){
		String[] childList = {"Hersteller: "+name, "MAC-Adresse: "+ mac_Address,"Distanz: "+range, "Minor: "+minor, "Major: "+ major, "Measured Power: "+ mPower, "Rssi: "+rssi};		
		return childList;
	}
	
	public String[] getChildList(){
		return this.childList;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getMAC_Address(){
		return this.mac_Address;
	}
	
	public int getMinor(){
		return this.minor ;
	}
	
	public int getMajor(){
		return this.major ;
	}
	
	public int getMPower(){
		return this.mPower;
	}
	
	public int getRssi(){
		return this.rssi ;
	}
	
	public double getRange(){
		return this.initRange;
	}
	
}
