package com.prm392.assignment.productsale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.address.AddressModel;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<AddressModel> addresses;
    private Context context;
    private OnAddressClickListener listener;

    public interface OnAddressClickListener {
        void onAddressClick(AddressModel address);
        void onEditClick(AddressModel address);
        void onDeleteClick(AddressModel address);
        void onSetDefaultClick(AddressModel address);
    }

    public AddressAdapter(Context context) {
        this.context = context;
        this.addresses = new ArrayList<>();
    }

    public void setOnAddressClickListener(OnAddressClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item_layout, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressModel address = addresses.get(position);
        holder.bind(address);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void setAddresses(List<AddressModel> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    public void addAddress(AddressModel address) {
        addresses.add(address);
        notifyItemInserted(addresses.size() - 1);
    }

    public void updateAddress(AddressModel address) {
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).getId().equals(address.getId())) {
                addresses.set(i, address);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeAddress(AddressModel address) {
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).getId().equals(address.getId())) {
                addresses.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView phoneText;
        private TextView addressText;
        private TextView defaultText;
        private ImageButton editButton;
        private ImageButton deleteButton;
        private ImageButton setDefaultButton;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.address_item_name);
            phoneText = itemView.findViewById(R.id.address_item_phone);
            addressText = itemView.findViewById(R.id.address_item_address);
            defaultText = itemView.findViewById(R.id.address_item_default);
            editButton = itemView.findViewById(R.id.address_item_edit);
            deleteButton = itemView.findViewById(R.id.address_item_delete);
            setDefaultButton = itemView.findViewById(R.id.address_item_set_default);
        }

        public void bind(AddressModel address) {
            nameText.setText(address.getName());
            phoneText.setText(address.getPhoneNumber());
            addressText.setText(address.toString());

            // Show/hide default indicator
            if (address.isDefault()) {
                defaultText.setVisibility(View.VISIBLE);
                setDefaultButton.setVisibility(View.GONE);
            } else {
                defaultText.setVisibility(View.GONE);
                setDefaultButton.setVisibility(View.VISIBLE);
            }

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddressClick(address);
                }
            });

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(address);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(address);
                }
            });

            setDefaultButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSetDefaultClick(address);
                }
            });
        }
    }
} 