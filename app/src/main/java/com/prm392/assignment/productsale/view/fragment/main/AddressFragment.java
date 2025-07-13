package com.prm392.assignment.productsale.view.fragment.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.adapters.AddressAdapter;
import com.prm392.assignment.productsale.databinding.FragmentAddressBinding;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.CreateAddressModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;
import com.prm392.assignment.productsale.util.DialogsProvider;
import com.prm392.assignment.productsale.util.UserAccountManager;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.view.fragment.dialogs.AddressDialog;
import com.prm392.assignment.productsale.viewmodel.fragment.main.AddressViewModel;

import java.util.ArrayList;

import retrofit2.Response;

public class AddressFragment extends Fragment implements AddressAdapter.OnAddressClickListener {
    private FragmentAddressBinding vb;
    private AddressViewModel viewModel;
    private AddressAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vb = FragmentAddressBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vb = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupRecyclerView();
        setupClickListeners();
        loadAddresses();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                vb.addressLoadingState.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.clearError();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new AddressAdapter(getContext());
        adapter.setOnAddressClickListener(this);
        vb.addressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.addressRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        vb.addressBackButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).getOnBackPressedDispatcher().onBackPressed();
            }
        });

        vb.addressAddButton.setOnClickListener(v -> {
            showAddressDialog(null);
        });
    }

    private void loadAddresses() {
        viewModel.getAddresses().observe(getViewLifecycleOwner(), response -> {
            viewModel.setLoading(false);

            if (response == null) {
                showError("Failed to load addresses");
                return;
            }

            switch (response.code()) {
                case BaseResponseModel.SUCCESSFUL_OPERATION:
                    handleAddressesLoaded(response.body());
                    break;

                case BaseResponseModel.FAILED_AUTH:
                    UserAccountManager.signOut(requireActivity(), true);
                    break;

                case BaseResponseModel.FAILED_REQUEST_FAILURE:
                    showError("Failed to load addresses");
                    break;

                default:
                    showError("Server error: " + response.code());
                    break;
            }
        });
    }

    private void handleAddressesLoaded(GetAllAddressResponseModel response) {
        if (response != null && response.getAddresses() != null) {
            ArrayList<AddressModel> addresses = response.getAddresses();
            adapter.setAddresses(addresses);
            
            if (addresses.isEmpty()) {
                vb.addressEmptyState.setVisibility(View.VISIBLE);
                vb.addressRecyclerView.setVisibility(View.GONE);
            } else {
                vb.addressEmptyState.setVisibility(View.GONE);
                vb.addressRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            vb.addressEmptyState.setVisibility(View.VISIBLE);
            vb.addressRecyclerView.setVisibility(View.GONE);
        }
    }

    private void showAddressDialog(AddressModel address) {
        AddressDialog dialog = new AddressDialog(requireContext(), address);
        dialog.setOnAddressSavedListener(newAddress -> {
            if (address == null) {
                // Create new address
                createAddress(newAddress);
            } else {
                // Update existing address
                updateAddress(address.getId(), newAddress);
            }
        });
        dialog.show();
    }

    private void createAddress(CreateAddressModel model) {
        viewModel.createAddress(model).observe(getViewLifecycleOwner(), response -> {
            viewModel.setLoading(false);

            if (response == null) {
                showError("Failed to create address");
                return;
            }
            Toast.makeText(getContext(), "Address created successfully", Toast.LENGTH_SHORT).show();
            loadAddresses(); // Reload the list
        });
    }

    private void updateAddress(String id, CreateAddressModel model) {
        viewModel.updateAddress(id, model).observe(getViewLifecycleOwner(), response -> {
            viewModel.setLoading(false);

            if (response == null) {
                showError("Failed to update address");
                return;
            }
            Toast.makeText(getContext(), "Address updated successfully", Toast.LENGTH_SHORT).show();
            loadAddresses(); // Reload the list
        });
    }

    private void deleteAddress(String id) {
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Address")
            .setMessage("Are you sure you want to delete this address?")
            .setPositiveButton("Delete", (dialog, which) -> {
                viewModel.deleteAddress(id).observe(getViewLifecycleOwner(), response -> {
                    viewModel.setLoading(false);

                    if (response == null) {
                        showError("Failed to delete address");
                        return;
                    }
                    Toast.makeText(getContext(), "Address deleted successfully", Toast.LENGTH_SHORT).show();
                    loadAddresses(); // Reload the list
                });
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void setDefaultAddress(AddressModel address) {
        CreateAddressModel model = new CreateAddressModel();
        model.setStreet(address.getStreet());
        model.setCity(address.getCity());
        model.setWard(address.getWard());
        model.setDistrict(address.getDistrict());
        model.setPhoneNumber(address.getPhoneNumber());
        model.setName(address.getName());
        model.setDefault(true);

        updateAddress(address.getId(), model);
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // AddressAdapter.OnAddressClickListener implementations
    @Override
    public void onAddressClick(AddressModel address) {
        // Handle address selection if needed
        viewModel.setSelectedAddress(address);
    }

    @Override
    public void onEditClick(AddressModel address) {
        showAddressDialog(address);
    }

    @Override
    public void onDeleteClick(AddressModel address) {
        deleteAddress(address.getId());
    }

    @Override
    public void onSetDefaultClick(AddressModel address) {
        setDefaultAddress(address);
    }
} 