package de.glueckkanja.geofencing;


import java.util.ArrayList;

public class Sorter {
	

	public Sorter(){
		
	}
	
	public ArrayList<BeaconItem> InsertionSort(ArrayList<BeaconItem> list) {
		int temp;
		int i,j;
		
		for(i=1; i<list.size();i++){
			temp = i;
			j = i-1;
			while(j >=0){
				if(list.get(temp).getMAC_Address().compareTo(list.get(j).getMAC_Address()) > 0){
					break;
				}
				list.set(j+1, list.get(j));
				j--;
			}
			list.set(j+1, list.get(temp));
		}		
		return list;
	}
}
