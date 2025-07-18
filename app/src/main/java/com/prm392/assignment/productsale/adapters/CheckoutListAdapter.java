package com.prm392.assignment.productsale.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.ProductModel;
import com.prm392.assignment.productsale.model.cart.CartItemModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Setter;

// Add new classes for header and subtotal
class StoreHeader {
    String storeName;
    int deliveryFee;
    StoreHeader(String storeName, int deliveryFee) {
        this.storeName = storeName;
        this.deliveryFee = deliveryFee;
    }
}
class StoreSubtotal {
    String storeName;
    int subtotal;
    StoreSubtotal(String storeName, int subtotal) {
        this.storeName = storeName;
        this.subtotal = subtotal;
    }
}

public class CheckoutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> items = new ArrayList<>();
    private final RecyclerView recyclerView;
    private final Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_SUBTOTAL = 2;

    public CheckoutListAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void setGroupedCartItems(List<CartItemModel> cartItems) {
        items.clear();
        // Group by store
        LinkedHashMap<String, List<CartItemModel>> grouped = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> deliveryFees = new LinkedHashMap<>();
        for (CartItemModel item : cartItems) {
            grouped.computeIfAbsent(item.getStoreName(), k -> new ArrayList<>()).add(item);
            deliveryFees.put(item.getStoreName(), 30000); // hardcoded, or get from item if available
        }
        for (String store : grouped.keySet()) {
            items.add(new StoreHeader(store, deliveryFees.get(store)));
            int subtotal = 0;
            for (CartItemModel item : grouped.get(store)) {
                items.add(item);
                subtotal += item.getPrice() * item.getQuantity();
            }
            items.add(new StoreSubtotal(store, subtotal + deliveryFees.get(store)));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof StoreHeader) return TYPE_HEADER;
        if (item instanceof CartItemModel) return TYPE_PRODUCT;
        if (item instanceof StoreSubtotal) return TYPE_SUBTOTAL;
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View v = inflater.inflate(R.layout.checkout_store_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_PRODUCT) {
            View v = inflater.inflate(R.layout.checkout_item_layout, parent, false);
            return new DataViewHolder(v);
        } else if (viewType == TYPE_SUBTOTAL) {
            View v = inflater.inflate(R.layout.checkout_store_subtotal, parent, false);
            return new SubtotalViewHolder(v);
        }
        throw new IllegalStateException("Unknown view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof HeaderViewHolder) {
            StoreHeader header = (StoreHeader) item;
            ((HeaderViewHolder) holder).bind(header);
        } else if (holder instanceof DataViewHolder) {
            CartItemModel product = (CartItemModel) item;
            ((DataViewHolder) holder).bind(product);
        } else if (holder instanceof SubtotalViewHolder) {
            StoreSubtotal subtotal = (StoreSubtotal) item;
            ((SubtotalViewHolder) holder).bind(subtotal);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder for header
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvDeliveryFee;
        HeaderViewHolder(View v) {
            super(v);
            tvStoreName = v.findViewById(R.id.tvStoreName);
            tvDeliveryFee = v.findViewById(R.id.tvDeliveryFee);
        }
        void bind(StoreHeader header) {
            tvStoreName.setText(header.storeName);
            tvDeliveryFee.setText("Phí vận chuyển: " + String.format("%,d₫", header.deliveryFee));
        }
    }
    // ViewHolder for subtotal
    static class SubtotalViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreSubtotalLabel, tvStoreSubtotal;
        SubtotalViewHolder(View v) {
            super(v);
            tvStoreSubtotalLabel = v.findViewById(R.id.tvStoreSubtotalLabel);
            tvStoreSubtotal = v.findViewById(R.id.tvStoreSubtotal);
        }
        void bind(StoreSubtotal subtotal) {
            tvStoreSubtotalLabel.setText("Tổng " + subtotal.storeName);
            tvStoreSubtotal.setText(String.format("%,d₫", subtotal.subtotal));
        }
    }
    // ViewHolder for product (reuse your DataViewHolder, but add a bind method)
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity, productCategory;
        ImageView productImage;
        public DataViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.product_list_item_Name);
            productPrice = view.findViewById(R.id.product_list_item_price);
            productQuantity = view.findViewById(R.id.product_list_item_quantity);
            productImage = view.findViewById(R.id.product_list_item_image);
            productCategory = view.findViewById(R.id.product_list_item_brand);
        }
        void bind(CartItemModel item) {
            productName.setText(item.getProductName());
            productCategory.setText(item.getStoreName());
            productPrice.setText(String.format("%,.0f₫", item.getPrice()));
            productQuantity.setText("x" + item.getQuantity());
            Glide.with(itemView.getContext())
                    .load(Uri.parse(item.getPictureUrl()))
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(250))
                    .into(productImage);
        }
    }
}
