package com.SampleApp.Sample.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.SampleApp.Sample.Adapters.PaginationListener;
import com.SampleApp.Sample.R;
import com.SampleApp.Sample.Adapters.UserRecyclerAdapter;
import com.SampleApp.Sample.helper.RequiredFunction;

import com.SampleApp.Sample.model.PageModel;
import com.SampleApp.Sample.network.APIClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

import static com.SampleApp.Sample.Adapters.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private UserRecyclerAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 3;
    private boolean isLoading = false;
    int itemCount = 0;
    RequiredFunction rf=new RequiredFunction();
    ProgressBar progressBar;
    TextView tv_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        tv_internet=findViewById(R.id.tv_internet);
        mRecyclerView.setHasFixedSize(true);  //setHasFixedSize() is used to let the RecyclerView keep the same size.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new UserRecyclerAdapter(MainActivity.this,new ArrayList<>());
        mRecyclerView.setAdapter(adapter);

        if(rf.isConnected(MainActivity.this)){
            doApiCall();
            tv_internet.setVisibility(View.GONE);
        }else {

            Toast.makeText(MainActivity.this,R.string.no_internet,Toast.LENGTH_LONG).show();
            swipeRefresh.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            tv_internet.setVisibility(View.VISIBLE);
        }


        //to add scroll listener while user reach in bottom load more will call
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                //Increment page index to load the next one
                currentPage++;
                doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    //do api call here to fetch data from server
    private void doApiCall() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Call<PageModel> call = APIClient.getInstance().getPageDetails

                        (

                                currentPage,
                                5
                        );

                call.enqueue(new Callback<PageModel>() {
                    @Override
                    public void onResponse(Call<PageModel> call, Response<PageModel> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {
                            PageModel pageModel = response.body();
                            Log.d("TAG", "1v :" + pageModel.getPage());


                            totalPage = pageModel.getTotal_pages();
                            if (currentPage != PAGE_START) adapter.removeLoading(); //To stop loader if current page size and
                            adapter.addItems(pageModel.getData());
                            swipeRefresh.setRefreshing(false);

                            // check weather is last page or not
                            if (currentPage < totalPage) {
                                adapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                        } else {
                            Toast.makeText(MainActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PageModel> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "NetworkCallFailure : " + t, Toast.LENGTH_LONG).show();
                    }
                });

            }
        }, 1500); //1.5 sec
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();

        if (rf.isConnected(MainActivity.this))
        {
            doApiCall();
            tv_internet.setVisibility(View.GONE);
        }else
        {
            Toast.makeText(MainActivity.this,R.string.no_internet,Toast.LENGTH_LONG).show();
            swipeRefresh.setRefreshing(false);
            tv_internet.setVisibility(View.VISIBLE);
        }

    }
}
