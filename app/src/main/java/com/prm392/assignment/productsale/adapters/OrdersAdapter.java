package com.prm392.assignment.productsale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.orders.OrderModel;
import com.prm392.assignment.productsale.adapters.OrderProductsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<OrderModel> orders;
    private Context context;
    private SimpleDateFormat dateFormat;

    public OrdersAdapter(Context context) {
        this.context = context;
        this.orders = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIdText;
        private TextView orderDateText;
        private TextView orderStatusText;
        private TextView orderTotalText;
        private TextView orderAddressText;
        private TextView orderDeliveryPriceText;
        private androidx.recyclerview.widget.RecyclerView orderProductsRecyclerView;
        private OrderProductsAdapter productsAdapter;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            orderTotalText = itemView.findViewById(R.id.orderTotalText);
            orderAddressText = itemView.findViewById(R.id.orderAddressText);
            orderDeliveryPriceText = itemView.findViewById(R.id.orderDeliveryPrice);
            orderProductsRecyclerView = itemView.findViewById(R.id.orderProductsRecyclerView);
            
            // Setup products adapter
            productsAdapter = new OrderProductsAdapter(context);
            orderProductsRecyclerView.setAdapter(productsAdapter);
            orderProductsRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(context));
        }

        public void bind(OrderModel order) {
            orderIdText.setText("Order #" + shortOrderId(order.getId()));
            
            if (order.getCreatedTime() != null) {
                orderDateText.setText(dateFormat.format(order.getCreatedTime()));
            } else {
                orderDateText.setText("N/A");
            }
            
            orderStatusText.setText(order.getOrderStatus());
            
            // Set delivery price
            if (order.getDeliveryPrice() != null) {
                orderDeliveryPriceText.setText(String.format(Locale.US, "%.0f₫", order.getDeliveryPrice()));
            } else {
                orderDeliveryPriceText.setText("0₫");
            }
            
            // Set total amount
            double totalAmount = (order.getPrice() != null ? order.getPrice() : 0.0) + 
                               (order.getDeliveryPrice() != null ? order.getDeliveryPrice() : 0.0);
            orderTotalText.setText(String.format(Locale.US, "%.0f₫", totalAmount));
            
            // Show store name as address for now, or you can modify this based on your needs
            orderAddressText.setText(order.getStoreName() != null ? order.getStoreName() : "N/A");
            
            // Set products
            if (order.getOrderDetailDTOs() != null && !order.getOrderDetailDTOs().isEmpty()) {
                productsAdapter.setProducts(order.getOrderDetailDTOs());
            } else {
                productsAdapter.setProducts(new ArrayList<>());
            }
        }

        private String shortOrderId(String orderId) {
            if (orderId == null || orderId.length() < 6) {
                return orderId != null ? orderId.toUpperCase() : "";
            }
            return orderId.substring(orderId.length() - 6).toUpperCase();
        }
    }
} 