package com.prm392.assignment.productsale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.pets.PetModel;

import java.util.List;

public class PetSpinnerAdapter extends ArrayAdapter<PetModel> {

    public PetSpinnerAdapter(@NonNull Context context, @NonNull List<PetModel> pets) {
        super(context, 0, pets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        PetModel pet = getItem(position);
        if (pet != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(pet.getDisplayName());
        }

        return convertView;
    }
} 