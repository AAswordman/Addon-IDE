package bms.helper.android.v7;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

	//public List<RecyclerDataBean> recyclerDataBeans = new ArrayList<>();

	private OnItemClickListener onItemClickListener;

	private OnItemLongClickListener onItemLongClickListener;

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener;
	}
	public View getItemByPosition(int position){
		return null;
	}
	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public interface OnItemLongClickListener {
		void onItemLongClick(View view, int position);
	}
    
    public View CreateView(ViewGroup viewGroup,int id){
        return LayoutInflater.from(viewGroup.getContext()).inflate(id, viewGroup, false);
    }
    
	//创建新View界面
	@Override
	public abstract ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType);
	//将数据与界面进行绑定的操作
	@Override
	public abstract void onBindViewHolder(final ViewHolder viewHolder, final int position);
	//获取数据的数量
	@Override
	public abstract int getItemCount()
	//自定义的ViewHolder，持有每个Item的的所有界面元素
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public View v;
		public ViewHolder(View view) {
			super(view);
			v=view;
		}
	}

}
