package de.glueckkanja.geofencing;

public class BeaconItem {
	private static int index;
	private int i;
	private String name;
	private String[]childList;
	
	public BeaconItem(String name, String[]childList){
		this.i = index;
		index++;
		this.name=name;
		this.childList=childList;
	}
	
	public int getIndex(){
		return(this.i);
	}
	public String getName(){
		return this.name;
	}
	
	public String[] getChildList(){
		return this.childList;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setChildList(String[]childList){
		this.childList = childList;
	}
	
}
