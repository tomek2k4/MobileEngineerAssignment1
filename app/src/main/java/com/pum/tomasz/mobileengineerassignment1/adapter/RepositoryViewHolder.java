package com.pum.tomasz.mobileengineerassignment1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pum.tomasz.mobileengineerassignment1.R;

public class RepositoryViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView description;

    public RepositoryViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.vr_name);
        description = (TextView) itemView.findViewById(R.id.vr_desc);
    }
}
