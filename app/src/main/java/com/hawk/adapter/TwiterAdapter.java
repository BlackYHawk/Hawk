package com.hawk.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.activity.R;
import com.hawk.data.model.Twiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 15/5/17.
 */
public class TwiterAdapter extends RecyclerView.Adapter<TwiterAdapter.ViewHolder> {

    private Context context;
    private List<Twiter> twiters;

    public TwiterAdapter(Context context, List<Twiter> twiters ) {
        this.context = context;

        if(twiters != null) {
            this.twiters = twiters;
        } else {
            this.twiters = new ArrayList<Twiter>();
        }
    }

    public void setDataSource(List<Twiter> twiters) {

        this.twiters.clear();

        if(twiters != null && twiters.size() > 0) {
            this.twiters.addAll(twiters);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_twiter_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Twiter twiter = twiters.get(position);

        holder.content.setText(twiter.content);
        TwiterSubAdapter twiterSubAdapter = new TwiterSubAdapter(context, twiter.imgPaths);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        holder.recyclerView.setAdapter(twiterSubAdapter);

    }

    @Override
    public int getItemCount() {
        return twiters != null ? twiters.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content;
        public RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.twiter_content);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.twiter_photo);
        }
    }
}
