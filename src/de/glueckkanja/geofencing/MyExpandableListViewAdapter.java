/*
 * Author : Jan-Niklas Vierheller
 * Date: 25.06.2014
 * 
 * Summary: This is a Listview Adapter that handles the ExpandableListView, which contains listed Beacons
 * 
 * Uses Estimote Technology and their Code!
 */

package de.glueckkanja.geofencing;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    //Attributes
	private Context context;
    private ArrayList<BeaconItem> parentList = new ArrayList<BeaconItem>();
         
    
    public MyExpandableListViewAdapter(Context context) {   	
    	this.context = context;
    }
    
    public void refreshList(Collection<BeaconItem> list ){
        this.parentList.clear();
        this.parentList.addAll(list);
        this.notifyDataSetChanged();
    }   
	                          
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public int getGroupCount() {
		return parentList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return parentList.get(groupPosition).getChildList().length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView tv=new TextView(context);
		tv.setText(parentList.get(groupPosition).getMAC_Address());
		tv.setPadding(90, 0, 0, 0);
		tv.setTextSize(20);
		return tv;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		TextView tv=new TextView(context);
		String text = parentList.get(groupPosition).getChildList()[childPosition];
		tv.setText(text);
		tv.setPadding(120, 0, 0, 0);
		tv.setTextSize(15);
		return tv;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


	

}
