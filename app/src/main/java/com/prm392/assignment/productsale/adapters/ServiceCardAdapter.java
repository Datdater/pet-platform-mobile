package com.prm392.assignment.productsale.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.prm392.assignment.productsale.R;
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
    private ItemInteractionListener itemInteractionListener;


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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            //Default ViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sale_card_layout, parent, false);
            return new ServiceCardAdapter.DataViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_load_more, parent, false);
            return new ServiceCardAdapter.FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ServiceCardAdapter.DataViewHolder) {
            ServiceCardAdapter.DataViewHolder holder = (ServiceCardAdapter.DataViewHolder) viewHolder;

            holder.name.setText(data.get(position).getName());
            holder.category.setText(data.get(position).getCategoryName());
            holder.price.setText(data.get(position).getPrice() + "");


            //Image
            Glide.with(context)
                    .load(data.get(position).getImage())
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(250))
                    .into(holder.image);

            holder.itemView.setOnClickListener(view -> {
                if (itemInteractionListener != null)
                    itemInteractionListener.onServiceClicked(data.get(holder.getBindingAdapterPosition()).getId());
            });

        } else if (viewHolder instanceof ServiceCardAdapter.FooterViewHolder) {
            ServiceCardAdapter.FooterViewHolder holder = (ServiceCardAdapter.FooterViewHolder) viewHolder;
            holder.loadMoreButton.setOnClickListener(v -> {
                if (itemInteractionListener != null) {
                    itemInteractionListener.onLoadMoreClicked(); // New callback for "Load More"
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasMore && position == data.size()) {
            return VIEW_TYPE_FOOTER; // Footer is the last item when hasMore is true
        }
        return VIEW_TYPE_ITEM; // Regular product item
    }


    public interface ItemInteractionListener {
        void onServiceClicked(String serviceId);
        void onLoadMoreClicked();
    }

    @Override
    public int getItemCount() {
        return data.size() + (hasMore ? 1 : 0); // Add 1 for footer if hasMore is true
    }

    public void addServices(ArrayList<ServiceModel> products) {

        recyclerView.post(() -> {
            int startPosition = data.size();
            data.addAll(products);
            notifyItemRangeInserted(startPosition, products.size());
        });
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView category, name, price;
        ImageView image;

        public DataViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.product_sale_card_category);
            name = view.findViewById(R.id.product_sale_card_Name);
            price = view.findViewById(R.id.product_sale_card_price);
            image = view.findViewById(R.id.product_sale_card_image);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        Button loadMoreButton;

        public FooterViewHolder(View view) {
            super(view);
            loadMoreButton = view.findViewById(R.id.loadMoreButton);
        }
    }
}
