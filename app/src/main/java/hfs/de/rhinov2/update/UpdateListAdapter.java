package hfs.de.rhinov2.update;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import hfs.de.rhinov2.R;

public class UpdateListAdapter extends RecyclerView.Adapter<UpdateListAdapter.ViewHolder> {

    private List<Update> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public UpdateListAdapter(Context context, List<Update> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void removeLast(){
        if(!mData.isEmpty()){
            mData.remove(0);
            this.notifyDataSetChanged();
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the list_item in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        String desc = mData.get(position).getDesc();
        holder.textViewTitle.setText(title);
        holder.textViewDesc.setText(desc);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setEmpty() {
        mData.clear();
        mData.add(new Update("No threats!", "no desc"));
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(Update update){
        mData.add(update);
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;

        public TextView textViewDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewDesc = (TextView) itemView.findViewById(R.id.desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) try {
                mClickListener.onItemClick(view, getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // convenience method for getting data at click position
    public Update getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position) throws IOException;
    }
}