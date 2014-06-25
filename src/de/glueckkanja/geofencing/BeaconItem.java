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

public class BeaconItem {
	//Attributes
	private String[]childList;
	private String name;
	private String mac_Address;
	private String range; 
	private int minor;
	private int major;
	private int mPower;
	private int rssi;	
	//end Attributes
	
	public BeaconItem(String name, String mac_Address, String range, int minor, int major, int mPower, int rssi){
		this.name=name;
		this.mac_Address = mac_Address;
		this.range = range;
		this.minor = minor;
		this.major = major;
		this.mPower = mPower;
		this.rssi = rssi;
		this.childList = createChildList(name, mac_Address, range, minor, major, mPower, rssi);
	}
	
	public String[] createChildList(String name, String mac_Address, String range, int minor, int major, int mPower, int rssi){
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
	
	public String getRange(){
		return this.range;
	}
	
}
