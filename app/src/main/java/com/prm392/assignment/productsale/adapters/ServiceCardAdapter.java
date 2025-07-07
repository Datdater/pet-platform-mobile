package com.prm392.assignment.productsale.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392.assignment.productsale.model.products.ProductSaleModel;
import com.prm392.assignment.productsale.model.services.ServiceModel;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class ServiceCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    private final ArrayList<ServiceModel> data;
    private final RecyclerView recyclerView;
    private final Context context;
    @Getter
    private boolean hasMore = false; // Flag to show/hide the "Load More" button
    //    private final boolean noResultsFound = false;
    @Setter
    private boolean hideFavButton = false;

    public ServiceCardAdapter(Context context, RecyclerView recyclerView) {
        this.data = new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        notifyDataSetChanged(); // Or use notifyItemInserted/Removed for efficiency
    }

    @Override
    public int getItemViewType(int position) {
        if (hasMore && position == data.size()) {
            return VIEW_TYPE_FOOTER; // Footer is the last item when hasMore is true
        }
        return VIEW_TYPE_ITEM; // Regular product item
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
