package de.glueckkanja.geofencing;

public class BeaconItem {
	//Attributes
	private String[]childList;
	private String name;
	private String mac_Address;
	private int minor;
	private int major;
	private int mPower;
	private int rssi;	
	//end Attributes
	
	public BeaconItem(String name, String mac_Address, int minor, int major, int mPower, int rssi){
		this.name=name;
		this.mac_Address = mac_Address;
		this.minor = minor;
		this.major = major;
		this.mPower = mPower;
		this.rssi = rssi;
		this.childList = createChildList(name, mac_Address, minor, major, mPower, rssi);
	}
	
	public String[] createChildList(String name, String mac_Address, int minor, int major, int mPower, int rssi){
		String[] childList = {"Hersteller: "+name, "MAC-Adresse: "+ mac_Address, "Minor: "+minor, "Major: "+ major, "Measured Power: "+ mPower, "Rssi: "+rssi};		
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
	
}
