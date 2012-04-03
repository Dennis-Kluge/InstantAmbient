package instantambient.ambientclient.util;

import instantambeint.ambientclient.R;
import instantambient.ambientclient.db.RoomAmbientDB;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomAmbientListAdapter extends BaseAdapter{

	
	ArrayList<RoomAmbientDB> data;
	LayoutInflater inflater;
	
	public RoomAmbientListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<RoomAmbientDB> data) {
		this.data = data;
	}
	
	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.ambient_list_item, null);
		TextView ambientItem = (TextView)convertView.findViewById(R.id.ambientItemTitle);
		ambientItem.setText(data.get(position).getAmbientName());
		return convertView;
	}

}
