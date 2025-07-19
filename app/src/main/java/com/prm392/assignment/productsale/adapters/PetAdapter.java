package com.prm392.assignment.productsale.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.pets.PetModel;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<PetModel> pets = new ArrayList<>();
    private OnPetClickListener listener;

    public interface OnPetClickListener {
        void onEditClick(PetModel pet);
        void onDeleteClick(PetModel pet);
    }

    public PetAdapter(OnPetClickListener listener) {
        this.listener = listener;
    }

    public void setPets(List<PetModel> pets) {
        this.pets = pets != null ? pets : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_item_layout, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetModel pet = pets.get(position);
        holder.bind(pet);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    class PetViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPetImage;
        private TextView tvPetName;
        private TextView tvPetType;
        private TextView tvPetAge;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPetImage = itemView.findViewById(R.id.ivPetImage);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvPetType = itemView.findViewById(R.id.tvPetType);
            tvPetAge = itemView.findViewById(R.id.tvPetAge);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(pets.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(pets.get(position));
                }
            });
        }

        public void bind(PetModel pet) {
            tvPetName.setText(pet.getName());
            
            // Show pet type with color
            String petTypeWithColor = pet.getPetTypeString();
            if (pet.getColor() != null && !pet.getColor().isEmpty()) {
                petTypeWithColor += " - " + pet.getColor();
            }
            tvPetType.setText(petTypeWithColor);
            
            // Format age display - show date of birth
            if (pet.getDob() != null && !pet.getDob().isEmpty()) {
                // Extract only date part (YYYY-MM-DD) if there's time part
                String dob = pet.getDob();
                if (dob.contains("T")) {
                    dob = dob.split("T")[0]; // Take only the date part before 'T'
                }
                tvPetAge.setText("Tuổi: " + dob);
            } else {
                tvPetAge.setText("Tuổi: Chưa xác định");
            }

            // TODO: Load pet image using Glide or similar library
            // For now, using default pet icon
            ivPetImage.setImageResource(R.drawable.ic_pet);
        }
    }
} 