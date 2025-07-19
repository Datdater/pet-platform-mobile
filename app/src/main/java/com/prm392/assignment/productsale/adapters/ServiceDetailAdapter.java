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
import com.prm392.assignment.productsale.model.services.ServiceDetailResponseModel;

import java.util.List;

public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.ViewHolder> {
    private final List<ServiceDetailResponseModel.PetServiceDetail> data;
    private final Context context;
    private ItemInteractionListener itemInteractionListener;

    public ServiceDetailAdapter(Context context, List<ServiceDetailResponseModel.PetServiceDetail> data) {
        this.context = context;
        this.data = data;
    }

    public void setItemInteractionListener(ItemInteractionListener listener) {
        this.itemInteractionListener = listener;
    }

    public interface ItemInteractionListener {
        void onServiceDetailClicked(String detailId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceDetailResponseModel.PetServiceDetail detail = data.get(position);
        holder.name.setText(detail.getName());
        holder.description.setText(detail.getDescription());
        holder.weight.setText("Cân nặng: " + detail.getPetWeightMin() + " - " + detail.getPetWeightMax() + " kg");
        holder.price.setText("Giá: " + detail.getAmount() + " VNĐ");
        holder.itemView.setOnClickListener(v -> {
            if (itemInteractionListener != null) {
                itemInteractionListener.onServiceDetailClicked(detail.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, weight, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.service_detail_name);
            description = itemView.findViewById(R.id.service_detail_description);
            weight = itemView.findViewById(R.id.service_detail_weight);
            price = itemView.findViewById(R.id.service_detail_price);
        }
    }
} 