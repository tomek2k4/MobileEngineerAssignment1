package com.pum.tomasz.mobileengineerassignment1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pum.tomasz.mobileengineerassignment1.R;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoryViewHolder> {

    private List<RepositoryItem> reposList = new ArrayList<>();
    private Context context;

    private final PublishSubject<RepositoryItem> onClickSubject = PublishSubject.create();

    public RepositoriesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.view_repository, parent, false);

        RepositoryViewHolder repositoryViewHolder = new RepositoryViewHolder(itemView);
        return repositoryViewHolder;
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        final RepositoryItem repoItem = reposList.get(position);
        holder.name.setText(getString(R.string.repo_name, repoItem.getName()));
        holder.description.setText(getString(R.string.repo_desc, repoItem.getDescription()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(repoItem);
            }
        });
    }

    public Observable<RepositoryItem> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }

    private String getString(int id, String... values) {
        return context.getString(id, values);
    }

    public void addEvent(RepositoryItem repositoryItem) {
        reposList.add(repositoryItem);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addEvents(Collection<RepositoryItem> repositoryItems) {
        reposList.addAll(repositoryItems);
        notifyDataSetChanged();
    }

    public void replaceEvents(Collection<RepositoryItem> repositoryItems) {
        reposList.clear();
        reposList.addAll(repositoryItems);
        notifyDataSetChanged();
    }

}
