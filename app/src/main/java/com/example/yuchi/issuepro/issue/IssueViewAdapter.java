package com.example.yuchi.issuepro.issue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Issue;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

class IssueViewAdapter extends RecyclerView.Adapter<IssueViewAdapter.ViewHolder> {

    @NonNull private final Listener listener;
    @NonNull private List<Issue> issues;

    IssueViewAdapter(@NonNull Listener listener) {
        this.listener = listener;
        this.issues = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull private TextView body;
        @NonNull private TextView title;

        ViewHolder(@NonNull View view) {
            super(view);

            this.body = (TextView) view.findViewById(R.id.body);
            this.title = (TextView) view.findViewById(R.id.title);
        }
    }

    @Override
    public IssueViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue, parent, false);
        return new ViewHolder(View);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Issue issue = issues.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(issue);
            }
        });

        viewHolder.title.setText(issue.getTitle());

        String body = issue.getBody();
        if (body == null || body.isEmpty()) {
            viewHolder.body.setVisibility(GONE);
        } else {
            viewHolder.body.setText(body);
        }
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    /**
     * @param issues a list of {@link Issue}.
     */
    void setViewModels(@NonNull List<Issue> issues) {
        int size = issues.size();
        this.issues = new ArrayList<>(issues.subList(0, size));
        notifyDataSetChanged();
    }

    /**
     * Callback listener.
     */
    interface Listener {

        /**
         * On item selected.
         *
         * @param issue issue view model.
         */
        void onItemSelected(@NonNull Issue issue);
    }
}
