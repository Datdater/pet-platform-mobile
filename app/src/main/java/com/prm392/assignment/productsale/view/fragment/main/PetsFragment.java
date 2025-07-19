package com.prm392.assignment.productsale.view.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.adapters.PetAdapter;
import com.prm392.assignment.productsale.model.pets.PetModel;
import com.prm392.assignment.productsale.util.UserAccountManager;
import com.prm392.assignment.productsale.viewmodel.fragment.main.PetsViewModel;
import com.google.gson.Gson;

public class PetsFragment extends Fragment implements PetAdapter.OnPetClickListener {

    private PetsViewModel viewModel;
    private PetAdapter adapter;
    private RecyclerView rvPets;
    private TextView tvEmpty;
    private FloatingActionButton fabAddPet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        
        // Load pets
        String token = UserAccountManager.getToken(requireContext(), UserAccountManager.TOKEN_TYPE_BEARER);
        viewModel.loadPets(token);
    }

    private void initViews(View view) {
        rvPets = view.findViewById(R.id.rvPets);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        fabAddPet = view.findViewById(R.id.fabAddPet);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(PetsViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new PetAdapter(this);
        rvPets.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPets.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
            android.util.Log.d("PetAdd", "Pets list updated, size: " + (pets != null ? pets.size() : 0));
            adapter.setPets(pets);
            updateEmptyState(pets != null && pets.isEmpty());
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // TODO: Show/hide loading indicator
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupUpdateObserver() {
        viewModel.getUpdateResult().observe(getViewLifecycleOwner(), response -> {
            Log.d("PetUpdate", "Response: " + (response != null ? response.code() : "null"));
            if (response != null && response.isSuccessful()) {
                Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                // Reload pets
                String token = com.prm392.assignment.productsale.util.UserAccountManager.getToken(requireContext(), com.prm392.assignment.productsale.util.UserAccountManager.TOKEN_TYPE_BEARER);
                viewModel.loadPets(token);
            } else if (response != null) {
                Toast.makeText(requireContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        fabAddPet.setOnClickListener(v -> showAddPetDialog());
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            rvPets.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvPets.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEditClick(PetModel pet) {
        showEditPetDialog(pet);
    }

    private void showEditPetDialog(PetModel pet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_edit_pet, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText edtName = dialogView.findViewById(R.id.edtPetName);
        EditText edtDob = dialogView.findViewById(R.id.edtPetDob);
        EditText edtWeight = dialogView.findViewById(R.id.edtPetWeight);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerPetType);
        EditText edtColor = dialogView.findViewById(R.id.edtPetColor);
        EditText edtSpecial = dialogView.findViewById(R.id.edtSpecialRequirement);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Set current values
        edtName.setText(pet.getName());
        String dob = pet.getDob();
        if (dob != null && dob.contains("T")) {
            dob = dob.split("T")[0];
        }
        edtDob.setText(dob);
        edtWeight.setText(String.valueOf(pet.getWeight()));
        edtColor.setText(pet.getColor());
        edtSpecial.setText(pet.getSpecialRequirement());
        spinnerType.setSelection(pet.isPetType() ? 0 : 1);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String dobInput = edtDob.getText().toString().trim();
            String weightStr = edtWeight.getText().toString().trim();
            String color = edtColor.getText().toString().trim();
            String special = edtSpecial.getText().toString().trim();
            boolean petType = spinnerType.getSelectedItemPosition() == 0;
            int weight = 0;
            try { weight = Integer.parseInt(weightStr); } catch (Exception ignored) {}

            PetModel updatedPet = new PetModel(
                pet.getId(), name, pet.getImage(), dobInput, weight, petType, pet.getAppUserId(), color, special
            );
            String token = com.prm392.assignment.productsale.util.UserAccountManager.getToken(requireContext(), com.prm392.assignment.productsale.util.UserAccountManager.TOKEN_TYPE_BEARER);
            Log.d("PetUpdate", "Token: " + token);
            Log.d("PetUpdate", "ID: " + pet.getId());
            Log.d("PetUpdate", "Body: " + new com.google.gson.Gson().toJson(updatedPet));
            viewModel.updatePet(token, pet.getId(), updatedPet);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDeleteClick(PetModel pet) {
        new android.app.AlertDialog.Builder(requireContext())
            .setTitle("Xóa thú cưng")
            .setMessage("Bạn có chắc muốn xóa " + pet.getName() + "?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                String token = com.prm392.assignment.productsale.util.UserAccountManager.getToken(requireContext(), com.prm392.assignment.productsale.util.UserAccountManager.TOKEN_TYPE_BEARER);
                android.util.Log.d("PetDelete", "Token: " + token);
                android.util.Log.d("PetDelete", "ID: " + pet.getId());
                viewModel.deletePet(token, pet.getId());
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void setupDeleteObserver() {
        viewModel.getDeleteResult().observe(getViewLifecycleOwner(), response -> {
            android.util.Log.d("PetDelete", "Response: " + (response != null ? response.code() : "null"));
            if (response != null && response.isSuccessful()) {
                Toast.makeText(requireContext(), "Xóa thú cưng thành công!", Toast.LENGTH_SHORT).show();
            } else if (response != null) {
                Toast.makeText(requireContext(), "Xóa thú cưng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddPetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_pet, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText edtName = dialogView.findViewById(R.id.edtPetName);
        EditText edtDob = dialogView.findViewById(R.id.edtPetDob);
        EditText edtWeight = dialogView.findViewById(R.id.edtPetWeight);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerPetType);
        EditText edtColor = dialogView.findViewById(R.id.edtPetColor);
        EditText edtSpecial = dialogView.findViewById(R.id.edtSpecialRequirement);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String dob = edtDob.getText().toString().trim();
            String weightStr = edtWeight.getText().toString().trim();
            String color = edtColor.getText().toString().trim();
            String special = edtSpecial.getText().toString().trim();
            boolean petType = spinnerType.getSelectedItemPosition() == 0;
            int weight = 0;
            try { weight = Integer.parseInt(weightStr); } catch (Exception ignored) {}

            PetModel newPet = new PetModel(name, "", dob, weight, petType, color, special);
            String token = com.prm392.assignment.productsale.util.UserAccountManager.getToken(requireContext(), com.prm392.assignment.productsale.util.UserAccountManager.TOKEN_TYPE_BEARER);
            android.util.Log.d("PetAdd", "Body: " + new com.google.gson.Gson().toJson(newPet));
            viewModel.addPet(token, newPet);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setupAddObserver() {
        viewModel.getAddResult().observe(getViewLifecycleOwner(), response -> {
            android.util.Log.d("PetAdd", "Response: " + (response != null ? response.code() : "null"));
            if (response != null && response.isSuccessful()) {
                Toast.makeText(requireContext(), "Thêm thú cưng thành công!", Toast.LENGTH_SHORT).show();
                // Reload pets
                String token = com.prm392.assignment.productsale.util.UserAccountManager.getToken(requireContext(), com.prm392.assignment.productsale.util.UserAccountManager.TOKEN_TYPE_BEARER);
                viewModel.loadPets(token);
            } else if (response != null) {
                Toast.makeText(requireContext(), "Thêm thú cưng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 