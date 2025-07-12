package com.prm392.assignment.productsale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.orders.OrderDetailModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.ProductViewHolder> {
    private List<OrderDetailModel> products;
    private Context context;

    public OrderProductsAdapter(Context context) {
        this.context = context;
        this.products = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        OrderDetailModel product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<OrderDetailModel> products) {
        this.products = products != null ? products : new ArrayList<>();
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productNameText;
        private TextView productAttributesText;
        private TextView productQuantityText;
        private TextView productPriceText;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productNameText = itemView.findViewById(R.id.productNameText);
            productAttributesText = itemView.findViewById(R.id.productAttributesText);
            productQuantityText = itemView.findViewById(R.id.productQuantityText);
            productPriceText = itemView.findViewById(R.id.productPriceText);
        }

        public void bind(OrderDetailModel product) {
            productNameText.setText(product.getProductName() != null ? product.getProductName() : "Unknown Product");
            productQuantityText.setText(String.valueOf(product.getQuantity() != null ? product.getQuantity() : 0));
            
            if (product.getPrice() != null) {
                productPriceText.setText(String.format(Locale.US, "%.0f₫", product.getPrice()));
            } else {
                productPriceText.setText("N/A");
            }

            // Load product image
            if (product.getPictureUrl() != null && !product.getPictureUrl().isEmpty()) {
                Glide.with(context)
                        .load(product.getPictureUrl())
                        .placeholder(R.drawable.profile_placeholder)
                        .error(R.drawable.profile_placeholder)
                        .centerCrop()
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.profile_placeholder);
            }

            // Display attributes if available
            if (product.getAttribute() != null && !product.getAttribute().isEmpty()) {
                StringBuilder attributes = new StringBuilder();
                for (Map.Entry<String, String> entry : product.getAttribute().entrySet()) {
                    if (attributes.length() > 0) {
                        attributes.append(", ");
                    }
                    attributes.append(entry.getKey()).append(": ").append(entry.getValue());
                }
                productAttributesText.setText(attributes.toString());
                productAttributesText.setVisibility(View.VISIBLE);
            } else {
                productAttributesText.setVisibility(View.GONE);
            }
        }
    }
} 