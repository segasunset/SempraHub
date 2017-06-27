package com.softmiracle.githubmvp.presentation.modules.repo.detail.forks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.softmiracle.githubmvp.R;
import com.softmiracle.githubmvp.common.SempraApplication;
import com.softmiracle.githubmvp.data.models.Repo;
import com.softmiracle.githubmvp.presentation.adapters.RepoListAdapter;
import com.softmiracle.githubmvp.presentation.modules.settings.SettingsActivity;
import com.softmiracle.githubmvp.presentation.modules.user.ProfilePagerActivity;
import com.softmiracle.githubmvp.utils.Constants;
import com.softmiracle.githubmvp.utils.listeners.EndlessRecyclerViewScrollListener;
import com.softmiracle.githubmvp.utils.listeners.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForksActivity extends AppCompatActivity implements ForksContract.ForksView, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener<Repo> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_watchers_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_watchers_list)
    SwipeRefreshLayout mRefreshLayout;

    private RepoListAdapter mAdapter;
    private ForksContract.ForksPresenter mPresenter;
    private LinearLayoutManager mLayoutManager;
    private int page = 1;

    public static Intent newIntent(Context context, String login, String repo) {
        Intent intent = new Intent(context, ForksActivity.class);
        intent.putExtra(Constants.EXTRA_USERNAME, login);
        intent.putExtra(Constants.EXTRA_REPO_ITEM, repo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsActivity.getTheme(this, SettingsActivity.THEME_TYPE_GLOBAL));
        setContentView(R.layout.activity_forks);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.forks);
        }

        setLayoutManager();
        setAdapter();

        mPresenter = new ForksPresenterImpl(this);
        mPresenter.loadForks(getIntent().getStringExtra(Constants.EXTRA_USERNAME),
                getIntent().getStringExtra(Constants.EXTRA_REPO_ITEM), page);

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount) {
                mPresenter.loadForks(getIntent().getStringExtra(Constants.EXTRA_USERNAME),
                        getIntent().getStringExtra(Constants.EXTRA_REPO_ITEM), ++currentPage);
            }
        });
    }

    public void setAdapter() {
        mAdapter = new RepoListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setLayoutManager() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onRefresh() {
        mAdapter.restoreData();
        mAdapter.notifyDataSetChanged();
        mPresenter.loadForks(getIntent().getStringExtra(Constants.EXTRA_USERNAME),
                getIntent().getStringExtra(Constants.EXTRA_REPO_ITEM), page);
    }

    @Override
    public void onItemClick(Repo item) {
        startActivity(new Intent(ProfilePagerActivity.newIntent(SempraApplication.getInstance(), item.getOwner().getLogin())));
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void hideProgressIndicator() {

    }

    @Override
    public void showForks(List<Repo> repoList) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setData(repoList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Throwable error) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}