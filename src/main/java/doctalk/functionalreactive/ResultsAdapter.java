package doctalk.functionalreactive;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import doctalk.functionalreactive.Retrofit.Issue;

/**
 * Created by anish wadhwa on 5/13/2017.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {

    ArrayList<Issue> issues;

    ResultsAdapter(ArrayList<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item_layout, parent, false);
        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {
        holder.tvResultName.setText(issues.get(position).getTitle());
        holder.tvResultBody.setText(issues.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder {

        TextView tvResultName;
        TextView tvResultBody;

        public ResultsViewHolder(View itemView) {
            super(itemView);
            tvResultName = (TextView)itemView.findViewById(R.id.tv_result_name);
            tvResultBody = (TextView)itemView.findViewById(R.id.tv_result_body);
        }
    }
}
