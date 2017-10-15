package com.pum.tomasz.mobileengineerassignment1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pum.tomasz.mobileengineerassignment1.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepositoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.vr_name)
    public TextView name;

    @Bind(R.id.vr_desc)
    public TextView description;

    public RepositoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
