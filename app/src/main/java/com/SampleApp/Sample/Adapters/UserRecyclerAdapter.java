package com.SampleApp.Sample.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SampleApp.Sample.R;
import com.SampleApp.Sample.model.UserDetailsModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

  //Create two ViewHolder class for ProgressHolder and ViewHolder
  private static final int VIEW_TYPE_LOADING = 0;
  private static final int VIEW_TYPE_NORMAL = 1;
  private boolean isLoaderVisible = false;

  private List<UserDetailsModel> mUserDetails;
  private Context context;

  public UserRecyclerAdapter(Context context,List<UserDetailsModel> userDetailsModelList) {
    this.mUserDetails = userDetailsModelList;
    this.context = context;
  }

  @NonNull @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

   // onCreateViewHolder() method inflate two view
   //First views for VIEW_TYPE_NORMAL that inflate item_users.xml and second for VIEW_TYPE_LOADING is inflate item_loading.xml
// if the loader is visible and item position is equal to the list size then return VIEW_TYPE_LOADING otherwise VIEW_TYPE_NORMAL )


    switch (viewType) {
      case VIEW_TYPE_NORMAL:
        return new ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false));
      case VIEW_TYPE_LOADING:
        return new ProgressHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
      default:
        return null;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
    holder.onBind(position);
  }


  //getItemViewType() is the return type of view for each position based on this layout is inflated.
  @Override
  public int getItemViewType(int position) {
    if (isLoaderVisible) {
      return position == mUserDetails.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
    } else {
      return VIEW_TYPE_NORMAL;
    }
  }

  @Override
  public int getItemCount() {
    return mUserDetails == null ? 0 : mUserDetails.size();
  }


  //Adding and Deleting items in the list by using method addItems() or clear()
  public void addItems(List<UserDetailsModel> userDetailsModelList) {
    mUserDetails.addAll(userDetailsModelList);
    notifyDataSetChanged();

  }

  //to show the loader
  public void addLoading() {
    isLoaderVisible = true;
    mUserDetails.add(new UserDetailsModel());
    notifyItemInserted(mUserDetails.size() - 1);
    //to add the item to the END
  }

  //to remove loader
  public void removeLoading() {
    isLoaderVisible = false;
    int position = mUserDetails.size() - 1;
    //to get last item position
    UserDetailsModel item = getItem(position);
    if (item != null) {
      mUserDetails.remove(position);
      notifyItemRemoved(position);
    }
  }

  //To clear all items from list
  public void clear() {
    mUserDetails.clear();
    notifyDataSetChanged();
  }

  UserDetailsModel getItem(int position) {
    return mUserDetails.get(position);
  }



  public class ViewHolder extends BaseViewHolder {
    TextView tv_Name;
    ImageView iv_user_profile;
    TextView tv_email;

    ViewHolder(View itemView) {
      super(itemView);
      iv_user_profile = itemView.findViewById(R.id.iv_user_profile);
      tv_Name = itemView.findViewById(R.id.tv_Name);
      tv_email = itemView.findViewById(R.id.tv_email);
    }

    protected void clear() {

    }


    //To bind view to data according to item position
    public void onBind(int position) {
      super.onBind(position);
      UserDetailsModel item = mUserDetails.get(position);

      tv_Name.setText(item.getFirst_name() +" "+item.getLast_name());
      tv_email.setText(item.getEmail());


      Glide.with(context)
              .load(item.getAvatar())
              .placeholder(R.drawable.user_placeholder)
              .into(iv_user_profile);

    }
  }

  //to handel progress barr
  public class ProgressHolder extends BaseViewHolder {
    ProgressHolder(View itemView) {
      super(itemView);

    }

    @Override
    protected void clear() {
    }
  }
}
