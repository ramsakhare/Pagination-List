package com.SampleApp.Sample.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {

  public static final int PAGE_START = 1;

  @NonNull
  private LinearLayoutManager layoutManager;

 //Set scrolling threshold here (for now i'm assuming 5 item in one page)
  private static final int PAGE_SIZE = 3;

  //Supporting only LinearLayoutManager for now.
  public PaginationListener(@NonNull LinearLayoutManager layoutManager) {
    this.layoutManager = layoutManager;
  }

  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    int visibleItemCount = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();
    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();


    //if isLoading is equal to true i.e means not false and isLastPage is equal to true then
    if (!isLoading() && !isLastPage()) {

        //if its visibleItemCount + firstVisibleItemPosition is greater than total item count then load more data.
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        loadMoreItems();
      }
    }
  }

  protected abstract void loadMoreItems();

  public abstract boolean isLastPage();

  public abstract boolean isLoading();
}