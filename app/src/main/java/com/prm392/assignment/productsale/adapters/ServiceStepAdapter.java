package com.prm392.assignment.productsale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.services.ServiceDetailResponseModel;

import java.util.List;

public class ServiceStepAdapter extends RecyclerView.Adapter<ServiceStepAdapter.ViewHolder> {
    private final List<ServiceDetailResponseModel.PetServiceStep> data;
    private final Context context;

    public ServiceStepAdapter(Context context, List<ServiceDetailResponseModel.PetServiceStep> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceDetailResponseModel.PetServiceStep step = data.get(position);
        holder.name.setText("Bước " + step.getPriority() + ": " + step.getName());
        holder.description.setText(step.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.service_step_name);
            description = itemView.findViewById(R.id.service_step_description);
        }
    }
} 