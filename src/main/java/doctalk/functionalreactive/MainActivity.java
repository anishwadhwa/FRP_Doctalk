package doctalk.functionalreactive;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import doctalk.functionalreactive.Retrofit.GitHubInterface;
import doctalk.functionalreactive.Retrofit.GithubService;
import doctalk.functionalreactive.Retrofit.Issue;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.rv_results)
    RecyclerView rvResults;
    @BindView(R.id.tv_empty_results)
    TextView tvEmptyResults;
    @BindView(R.id.rl_issues_layout)
    RelativeLayout rlIssuesLayout;

    private ResultsAdapter adapter;
    private GitHubInterface gitHubInterface;
    private CompositeDisposable disposables;
    private ArrayList<Issue> issues;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String gitHubToken = getResources().getString(R.string.github_oauth_token);
        gitHubInterface = GithubService.createGithubService(gitHubToken);
        disposables = new CompositeDisposable();
        issues = new ArrayList<Issue>();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResults.setLayoutManager(layoutManager);
        adapter = new ResultsAdapter(issues);
        rvResults.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery(query);
        return false;
    }

    private void searchQuery(String searchQuery) {
        //TODO refind this
        String ownerName = searchQuery.substring(0,searchQuery.indexOf('/'));
        String repoName = searchQuery.substring(searchQuery.indexOf('/')+1, searchQuery.length());
        Log.d(TAG,"Search String : " + ownerName + repoName);

        disposables.add(
                gitHubInterface
                        .issues(ownerName, repoName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableObserver<List<Issue>>() {

                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG,"Retrofit call completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "error while making the call");
                                        rlIssuesLayout.setVisibility(GONE);
                                        tvEmptyResults.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onNext(List<Issue> issues) {
                                        rlIssuesLayout.setVisibility(View.VISIBLE);
                                        tvEmptyResults.setVisibility(View.GONE);
                                        for (Issue issue : issues) {
                                            adapter.issues.add(issue);
                                            adapter.notifyDataSetChanged();
                                            /*_adapter.add(
                                                    format(
                                                            "%s has made %d contributions to %s",
                                                            c.login, c.contributions, _repo.getText().toString()));*/
                                        }
                                    }
                                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
