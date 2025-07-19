package com.prm392.assignment.productsale.view.fragment.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.data.repository.LocationRepository;
import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.CreateAddressModel;
import com.prm392.assignment.productsale.model.location.LocationModel;
import com.prm392.assignment.productsale.model.location.LocationResponseModel;

public class AddressDialog extends Dialog {
    private Context context;
    private AddressModel address;
    private OnAddressSavedListener listener;
    private LocationRepository locationRepository;

    private TextInputLayout nameLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout streetLayout;
    private TextInputLayout wardLayout;
    private TextInputLayout districtLayout;
    private TextInputLayout cityLayout;
    private CheckBox defaultCheckBox;

    private TextInputEditText nameEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText streetEditText;
    private Spinner wardSpinner;
    private Spinner districtSpinner;
    private Spinner citySpinner;

    private ArrayAdapter<LocationModel> cityAdapter;
    private ArrayAdapter<LocationModel> districtAdapter;
    private ArrayAdapter<LocationModel> wardAdapter;

    private LocationModel selectedCity;
    private LocationModel selectedDistrict;
    private LocationModel selectedWard;
    
    private boolean isUpdatingDistrict = false;
    private boolean isUpdatingWard = false;

    public interface OnAddressSavedListener {
        void onAddressSaved(CreateAddressModel address);
    }

    public AddressDialog(@NonNull Context context, @Nullable AddressModel address) {
        super(context);
        this.context = context;
        this.address = address;
        this.locationRepository = new LocationRepository();
    }

    public void setOnAddressSavedListener(OnAddressSavedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_address);

        initViews();
        setupViews();
        setupClickListeners();
        setupLocationDropdowns();
        populateFields();
    }

    private void initViews() {
        nameLayout = findViewById(R.id.dialog_address_name_layout);
        phoneLayout = findViewById(R.id.dialog_address_phone_layout);
        streetLayout = findViewById(R.id.dialog_address_street_layout);
        wardLayout = findViewById(R.id.dialog_address_ward_layout);
        districtLayout = findViewById(R.id.dialog_address_district_layout);
        cityLayout = findViewById(R.id.dialog_address_city_layout);
        defaultCheckBox = findViewById(R.id.dialog_address_default_checkbox);

        nameEditText = findViewById(R.id.dialog_address_name);
        phoneEditText = findViewById(R.id.dialog_address_phone);
        streetEditText = findViewById(R.id.dialog_address_street);
        wardSpinner = findViewById(R.id.dialog_address_ward);
        districtSpinner = findViewById(R.id.dialog_address_district);
        citySpinner = findViewById(R.id.dialog_address_city);

        Button saveButton = findViewById(R.id.dialog_address_save);
        Button cancelButton = findViewById(R.id.dialog_address_cancel);

        saveButton.setOnClickListener(v -> saveAddress());
        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void setupViews() {
        // Set dialog title based on whether we're editing or creating
        if (address != null) {
            setTitle("Edit Address");
        } else {
            setTitle("Add New Address");
        }
    }

    private void setupClickListeners() {
        // Add any additional click listeners if needed
    }

    private void populateFields() {
        if (address != null) {
            // Populate fields with existing address data
            nameEditText.setText(address.getName());
            phoneEditText.setText(address.getPhoneNumber());
            streetEditText.setText(address.getStreet());
            defaultCheckBox.setChecked(address.isDefault());
            
            // For location fields, we'll need to find the matching objects
            // This is a simplified approach - in a real app you might want to store IDs
            // We'll set the text after the spinners are loaded
        }
    }

    private void saveAddress() {
        // Clear previous errors
        clearErrors();

        // Get values from input fields
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        boolean isDefault = defaultCheckBox.isChecked();

        // Get location values from selected objects
        String city = selectedCity != null ? selectedCity.getName() : "";
        String district = selectedDistrict != null ? selectedDistrict.getName() : "";
        String ward = selectedWard != null ? selectedWard.getName() : "";

        // Validate inputs
        if (!validateInputs(name, phone, street, ward, district, city)) {
            return;
        }

        // Create address model
        CreateAddressModel addressModel = new CreateAddressModel();
        addressModel.setName(name);
        addressModel.setPhoneNumber(phone);
        addressModel.setStreet(street);
        addressModel.setWard(ward);
        addressModel.setDistrict(district);
        addressModel.setCity(city);
        addressModel.setDefault(isDefault);

        // Call listener
        if (listener != null) {
            listener.onAddressSaved(addressModel);
        }

        dismiss();
    }

    private boolean validateInputs(String name, String phone, String street, String ward, String district, String city) {
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("Name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneLayout.setError("Phone number is required");
            isValid = false;
        } else if (!isValidPhoneNumber(phone)) {
            phoneLayout.setError("Invalid phone number format");
            isValid = false;
        }

        if (TextUtils.isEmpty(street)) {
            streetLayout.setError("Street is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(ward)) {
            wardLayout.setError("Ward is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(district)) {
            districtLayout.setError("District is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(city)) {
            cityLayout.setError("City is required");
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidPhoneNumber(String phone) {
        // Basic phone number validation for Vietnamese format
        return phone.matches("^[+]?[0-9]{10,15}$");
    }

    private void clearErrors() {
        nameLayout.setError(null);
        phoneLayout.setError(null);
        streetLayout.setError(null);
        wardLayout.setError(null);
        districtLayout.setError(null);
        cityLayout.setError(null);
    }

    private void setupLocationDropdowns() {
        // Initialize adapters with better layout
        cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        districtAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        wardAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);

        // Set dropdown layout
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySpinner.setAdapter(cityAdapter);
        districtSpinner.setAdapter(districtAdapter);
        wardSpinner.setAdapter(wardAdapter);

        // Load provinces/cities
        loadProvinces();

        // Set up listeners
        citySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Skip the first item if it's a placeholder
                    selectedCity = cityAdapter.getItem(position);
                    selectedDistrict = null;
                    selectedWard = null;
                    
                    // Reset district and ward spinners
                    isUpdatingDistrict = true;
                    districtAdapter.clear();
                    districtAdapter.add(new LocationModel());
                    districtAdapter.notifyDataSetChanged();
                    districtSpinner.setSelection(0);
                    isUpdatingDistrict = false;
                    
                    isUpdatingWard = true;
                    wardAdapter.clear();
                    wardAdapter.add(new LocationModel());
                    wardAdapter.notifyDataSetChanged();
                    wardSpinner.setSelection(0);
                    isUpdatingWard = false;
                    
                    if (selectedCity != null) {
                        loadDistricts(selectedCity.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedCity = null;
            }
        });

        districtSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && !isUpdatingDistrict) { // Skip the first item if it's a placeholder
                    selectedDistrict = districtAdapter.getItem(position);
                    selectedWard = null;
                    
                    // Reset ward spinner
                    isUpdatingWard = true;
                    wardAdapter.clear();
                    wardAdapter.add(new LocationModel());
                    wardAdapter.notifyDataSetChanged();
                    wardSpinner.setSelection(0);
                    isUpdatingWard = false;
                    
                    if (selectedDistrict != null) {
                        loadWards(selectedDistrict.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedDistrict = null;
            }
        });

        wardSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && !isUpdatingWard) { // Skip the first item if it's a placeholder
                    selectedWard = wardAdapter.getItem(position);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedWard = null;
            }
        });
    }

    private void loadProvinces() {
        Toast.makeText(context, "Loading provinces...", Toast.LENGTH_SHORT).show();
        
        locationRepository.getProvinces(1000).observeForever(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                cityAdapter.clear();
                cityAdapter.add(new LocationModel());
                cityAdapter.addAll(response.body().getData());
                cityAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Loaded " + response.body().getData().size() + " provinces", Toast.LENGTH_SHORT).show();
            } else {
                String errorMsg = "Failed to load provinces";
                if (response != null) {
                    errorMsg += " - Code: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - Error: " + response.errorBody().string();
                        } catch (Exception e) {
                            errorMsg += " - Error body could not be read";
                        }
                    }
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadDistricts(String provinceId) {
        if (provinceId == null || provinceId.isEmpty()) {
            districtAdapter.clear();
            districtAdapter.add(new LocationModel());
            districtAdapter.notifyDataSetChanged();
            return;
        }

        Toast.makeText(context, "Loading districts for province: " + provinceId, Toast.LENGTH_SHORT).show();
        
        locationRepository.getDistricts(provinceId, 1000).observeForever(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                districtAdapter.clear();
                districtAdapter.add(new LocationModel());
                districtAdapter.addAll(response.body().getData());
                districtAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Loaded " + response.body().getData().size() + " districts", Toast.LENGTH_SHORT).show();
            } else {
                String errorMsg = "Failed to load districts";
                if (response != null) {
                    errorMsg += " - Code: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - Error: " + response.errorBody().string();
                        } catch (Exception e) {
                            errorMsg += " - Error body could not be read";
                        }
                    }
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadWards(String districtId) {
        if (districtId == null || districtId.isEmpty()) {
            wardAdapter.clear();
                wardAdapter.add(new LocationModel());
            wardAdapter.notifyDataSetChanged();
            return;
        }

        Toast.makeText(context, "Loading wards for district: " + districtId, Toast.LENGTH_SHORT).show();
        
        locationRepository.getWards(districtId, 1000).observeForever(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                wardAdapter.clear();
                wardAdapter.add(new LocationModel());
                wardAdapter.addAll(response.body().getData());
                wardAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Loaded " + response.body().getData().size() + " wards", Toast.LENGTH_SHORT).show();
            } else {
                String errorMsg = "Failed to load wards";
                if (response != null) {
                    errorMsg += " - Code: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - Error: " + response.errorBody().string();
                        } catch (Exception e) {
                            errorMsg += " - Error body could not be read";
                        }
                    }
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
} 