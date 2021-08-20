package bms.helper.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListViewHelper extends BaseAdapter {

	public View inflate(int function_grid_table, ViewGroup p1) {
		return layoutInflater.inflate(function_grid_table, p1);

	}
	@Override
	public Object getItem(int p1) {
		return null;
	}
	private LayoutInflater layoutInflater;
	protected ListViewHelper.Adepter adp;
	public ListViewHelper(Context context, ListViewHelper.Adepter adp) {
		this.adp = adp;
		layoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return adp.getCount();
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//new RoundRectLayout(v.findViewById(R.id.functiongridtableLinearLayout1));
		return adp.getView(this, position, convertView, parent);
	}
	public static abstract class Adepter {
		public abstract View getView(ListViewHelper adp, int position, View convertView, ViewGroup parent)
	   	public abstract int getCount();
	}
}
