package de.glueckkanja.geofencing;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class myExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<BeaconItem> parentList = new ArrayList<BeaconItem>();
  
   
    						
    public myExpandableListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
    	
    	this.context = context;
    	
    	
    }
    
    public void refreshList(Collection<BeaconItem> list ){
        this.parentList.clear();
        this.parentList.addAll(list);
        this.notifyDataSetChanged();
    }
    
   
	                          
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parentList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return parentList.get(groupPosition).getChildList().length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return parentList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv=new TextView(context);
		tv.setText(parentList.get(groupPosition).getName());
		tv.setPadding(90, 0, 0, 0);
		tv.setTextSize(20);
		return tv;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv=new TextView(context);
		String text = parentList.get(groupPosition).getChildList()[childPosition];
		tv.setText(text);
		tv.setPadding(120, 0, 0, 0);
		tv.setTextSize(15);
		return tv;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}


	

}
