package com.hawk.adapter;

import android.content.Context;
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
    private int resourceId;
    private List<Twiter> twiters;

    public TwiterAdapter(Context context, int resourceId, List<Twiter> twiters ) {
        this.context = context;
        this.resourceId = resourceId;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Twiter twiter = twiters.get(position);

        holder.title.setText(twiter.title);
        holder.content.setText(twiter.content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return twiters != null ? twiters.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.twiter_title);
            content = (TextView) itemView.findViewById(R.id.twiter_content);
        }
    }
}
