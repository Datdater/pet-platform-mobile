package com.prm392.assignment.productsale.view.fragment.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.adapters.PetSpinnerAdapter;
import com.prm392.assignment.productsale.model.pets.PetModel;
import com.prm392.assignment.productsale.util.SharedPrefManager;
import com.prm392.assignment.productsale.viewmodel.fragment.main.BookingViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.google.gson.Gson;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.content.Context;
import android.Manifest;
import android.content.DialogInterface;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BookingFragment extends Fragment {

    private AutoCompleteTextView serviceDetailSpinner;
    private AutoCompleteTextView petSpinner;
    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText notesEditText;
    private MaterialButton confirmBookingButton;
    private ImageButton backButton;

    private Calendar selectedDate;
    private Calendar selectedTime;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    
    private BookingViewModel viewModel;
    private List<PetModel> petsList = new ArrayList<>();
    
    // Thêm biến để lưu trữ dữ liệu service details từ ServiceDetailAdapter
    private List<String> serviceDetailNames = new ArrayList<>();
    private List<String> serviceDetailIds = new ArrayList<>();
    private List<Integer> serviceDetailPrices = new ArrayList<>();
    private String selectedServiceId;
    private String selectedServiceName;

    private PetModel selectedPetModel;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1002;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this, BookingViewModel.initializer).get(BookingViewModel.class);
        
        // Nhận dữ liệu từ ServicePageFragment
        receiveServiceDetailsData();
        
        initViews(view);
        setupServiceDetailSpinner();
        setupDatePicker();
        setupTimePicker();
        setupClickListeners();
        
        // Load pets from API
        loadPets();
        
        // Test spinner click
        petSpinner.setOnClickListener(v -> {
            Log.d("BookingFragment", "Pet spinner clicked. Adapter count: " + 
                (petSpinner.getAdapter() != null ? petSpinner.getAdapter().getCount() : "null"));
        });
    }
    
    private void receiveServiceDetailsData() {
        Bundle args = getArguments();
        if (args != null) {
            // Nhận danh sách service detail names từ ServiceDetailAdapter
            ArrayList<String> names = args.getStringArrayList("serviceDetailNames");
            ArrayList<String> ids = args.getStringArrayList("serviceDetailIds");
            ArrayList<Integer> prices = args.getIntegerArrayList("serviceDetailPrices");
            
            if (names != null && ids != null && prices != null) {
                serviceDetailNames = names;
                serviceDetailIds = ids;
                serviceDetailPrices = prices;
                
                Log.d("BookingFragment", "Received " + serviceDetailNames.size() + " service details from ServiceDetailAdapter");
                for (int i = 0; i < serviceDetailNames.size(); i++) {
                    Log.d("BookingFragment", "Service Detail: " + serviceDetailNames.get(i) + 
                        " (ID: " + serviceDetailIds.get(i) + ", Price: " + serviceDetailPrices.get(i) + ")");
                }
            } else {
                Log.w("BookingFragment", "No service details received, using default data");
                setupDefaultServiceDetails();
            }
        } else {
            Log.w("BookingFragment", "No arguments received, using default data");
            setupDefaultServiceDetails();
        }
    }
    
    private void setupDefaultServiceDetails() {
        // Fallback to default service details if no data received
        serviceDetailNames.clear();
        serviceDetailIds.clear();
        serviceDetailPrices.clear();
        
        // Generate default IDs and prices
        for (int i = 0; i < serviceDetailNames.size(); i++) {
            serviceDetailIds.add("default_" + i);
            serviceDetailPrices.add(100000 + (i * 50000)); // Default prices
        }
    }

    private void initViews(View view) {
        serviceDetailSpinner = view.findViewById(R.id.service_detail_spinner);
        petSpinner = view.findViewById(R.id.pet_spinner);
        dateEditText = view.findViewById(R.id.date_edit_text);
        timeEditText = view.findViewById(R.id.time_edit_text);
        notesEditText = view.findViewById(R.id.notes_edit_text);
        confirmBookingButton = view.findViewById(R.id.confirm_booking_button);
        backButton = view.findViewById(R.id.booking_back_button);

        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    private void setupServiceDetailSpinner() {
        // Setup Service Detail Spinner với dữ liệu từ ServiceDetailAdapter
        Log.d("BookingFragment", "Setting up service detail spinner with " + serviceDetailNames.size() + " items");
        
        // Tạo danh sách hiển thị với tên và giá
        List<String> displayList = new ArrayList<>();
        for (int i = 0; i < serviceDetailNames.size(); i++) {
            String displayText = serviceDetailNames.get(i) + " - " + serviceDetailPrices.get(i) + " VNĐ";
            displayList.add(displayText);
        }

        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                displayList
        );
        serviceDetailSpinner.setAdapter(serviceAdapter);
        
        // Thêm listener để lưu thông tin service được chọn
        serviceDetailSpinner.setOnItemClickListener((parent, view, position, id) -> {
            selectedServiceId = serviceDetailIds.get(position);
            selectedServiceName = serviceDetailNames.get(position);
            Log.d("BookingFragment", "Selected service: " + selectedServiceName + " (ID: " + selectedServiceId + ")");
        });
    }

    private void loadPets() {
        String token = "Bearer " + SharedPrefManager.get(requireContext()).getToken();
        Log.d("BookingFragment", "Loading pets with token: " + token);
        Log.d("BookingFragment", "Token length: " + token.length());
        Log.d("BookingFragment", "Token starts with Bearer: " + token.startsWith("Bearer"));
        Log.d("BookingFragment", "Raw token from SharedPref: " + SharedPrefManager.get(requireContext()).getToken());
        
        viewModel.getPets(token).observe(getViewLifecycleOwner(), pets -> {
            Log.d("BookingFragment", "Received pets from ViewModel: " + (pets != null ? pets.size() : "null"));
            if (pets != null && !pets.isEmpty()) {
                petsList = pets;
                Log.d("BookingFragment", "Setting up pet spinner with " + pets.size() + " pets");
                setupPetSpinner();
            } else {
                Log.d("BookingFragment", "No pets received, using default pets");
                // Fallback to default pets if API fails
                setupDefaultPetSpinner();
            }
        });
        
        // Also observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("BookingFragment", "Error loading pets: " + error);
            }
        });
    }

    private void setupPetSpinner() {
        Log.d("BookingFragment", "Setting up pet spinner with " + petsList.size() + " pets");
        for (PetModel pet : petsList) {
            Log.d("BookingFragment", "Pet: " + pet.getName() + " (" + pet.getPetTypeString() + ")");
        }
        PetSpinnerAdapter petAdapter = new PetSpinnerAdapter(requireContext(), petsList);
        petSpinner.setAdapter(petAdapter);
        Log.d("BookingFragment", "Pet spinner adapter set successfully");

        petSpinner.setOnItemClickListener((parent, view, position, id) -> {
            selectedPetModel = petsList.get(position);
            Log.d("BookingFragment", "Selected pet: " + selectedPetModel.getName() + " - id: " + selectedPetModel.getId());
        });
    }

    private void setupDefaultPetSpinner() {
        Log.d("BookingFragment", "Setting up default pet spinner");
        List<String> pets = new ArrayList<>();
        pets.add("Chó");
        pets.add("Mèo");
        pets.add("Thỏ");
        pets.add("Hamster");
        pets.add("Chim");

        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                pets
        );
        petSpinner.setAdapter(petAdapter);
        Log.d("BookingFragment", "Default pet spinner adapter set successfully");
    }

    private void setupDatePicker() {
        dateEditText.setOnClickListener(v -> {
            // Tạo Locale tiếng Việt
            Locale vietnameseLocale = new Locale("vi", "VN");
            
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(year, month, dayOfMonth);
                        dateEditText.setText(dateFormat.format(selectedDate.getTime()));
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
            );

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            
            // Set Vietnamese text for buttons
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePickerDialog);
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Hủy", datePickerDialog);
            
            // Áp dụng locale tiếng Việt cho DatePicker
            try {
                DatePicker datePicker = datePickerDialog.getDatePicker();
                if (datePicker != null) {
                    // Sử dụng reflection để set locale cho DatePicker
                    java.lang.reflect.Field[] pickerFields = DatePicker.class.getDeclaredFields();
                    for (java.lang.reflect.Field field : pickerFields) {
                        if (field.getName().equals("mDelegate")) {
                            field.setAccessible(true);
                            Object delegate = field.get(datePicker);
                            if (delegate != null) {
                                java.lang.reflect.Field[] delegateFields = delegate.getClass().getDeclaredFields();
                                for (java.lang.reflect.Field delegateField : delegateFields) {
                                    if (delegateField.getName().equals("mCurrentLocale")) {
                                        delegateField.setAccessible(true);
                                        delegateField.set(delegate, vietnameseLocale);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.w("BookingFragment", "Could not set Vietnamese locale for DatePicker: " + e.getMessage());
            }
            
            datePickerDialog.show();
        });
    }

    private void setupTimePicker() {
        timeEditText.setOnClickListener(v -> {
            // Tạo Locale tiếng Việt
            Locale vietnameseLocale = new Locale("vi", "VN");
            
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, hourOfDay, minute) -> {
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        timeEditText.setText(timeFormat.format(selectedTime.getTime()));
                    },
                    selectedTime.get(Calendar.HOUR_OF_DAY),
                    selectedTime.get(Calendar.MINUTE),
                    true
            );
            
            // Set Vietnamese text for buttons
            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "OK", timePickerDialog);
            timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Hủy", timePickerDialog);
            
            // Áp dụng locale tiếng Việt cho TimePicker
            try {
                // Sử dụng reflection để truy cập TimePicker từ TimePickerDialog
                java.lang.reflect.Field[] dialogFields = TimePickerDialog.class.getDeclaredFields();
                for (java.lang.reflect.Field field : dialogFields) {
                    if (field.getName().equals("mTimePicker")) {
                        field.setAccessible(true);
                        TimePicker timePicker = (TimePicker) field.get(timePickerDialog);
                        if (timePicker != null) {
                            // Sử dụng reflection để set locale cho TimePicker
                            java.lang.reflect.Field[] pickerFields = TimePicker.class.getDeclaredFields();
                            for (java.lang.reflect.Field pickerField : pickerFields) {
                                if (pickerField.getName().equals("mDelegate")) {
                                    pickerField.setAccessible(true);
                                    Object delegate = pickerField.get(timePicker);
                                    if (delegate != null) {
                                        java.lang.reflect.Field[] delegateFields = delegate.getClass().getDeclaredFields();
                                        for (java.lang.reflect.Field delegateField : delegateFields) {
                                            if (delegateField.getName().equals("mCurrentLocale")) {
                                                delegateField.setAccessible(true);
                                                delegateField.set(delegate, vietnameseLocale);
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                Log.w("BookingFragment", "Could not set Vietnamese locale for TimePicker: " + e.getMessage());
            }
            
            timePickerDialog.show();
        });
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        confirmBookingButton.setOnClickListener(v -> {
            if (!hasNotificationPermission()) {
                showNotificationPermissionDialog();
                return;
            }
            if (validateInputs()) {
                confirmBooking();
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private boolean validateInputs() {
        if (serviceDetailSpinner.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn loại dịch vụ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (petSpinner.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn thú cưng", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (timeEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn giờ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    
    private void confirmBooking() {
        if (selectedServiceId != null && selectedServiceName != null) {
            String notes = notesEditText.getText().toString().trim();
            // Lấy token đúng từ SharedPrefManager
            String rawToken = SharedPrefManager.get(requireContext()).getToken();
            String token = rawToken != null && !rawToken.startsWith("Bearer ") ? "Bearer " + rawToken : rawToken;
            Log.d("BookingFragment", "Booking API token: " + token);
            String bookingTime = getBookingTimeISO8601();
            String petId = getSelectedPetId();

            // Build request body
            com.prm392.assignment.productsale.model.services.BookingRequestModel request = new com.prm392.assignment.productsale.model.services.BookingRequestModel();
            request.setBookingTime(bookingTime);
            request.setDescription(notes);
            request.setPromotionId(null); // Nếu có promotion thì truyền vào

            com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail.Pet pet =
                new com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail.Pet(petId);
            com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail.Service service =
                new com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail.Service(selectedServiceId);
            com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail detail =
                new com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail();
            detail.setPet(pet);
            java.util.List<com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail.Service> services = new java.util.ArrayList<>();
            services.add(service);
            detail.setServices(services);
            java.util.List<com.prm392.assignment.productsale.model.services.BookingRequestModel.BookingDetail> details = new java.util.ArrayList<>();
            details.add(detail);
            request.setBookingDetails(details);

            Log.d("BookingFragment", "BookingRequest: " + new com.google.gson.Gson().toJson(request));

            viewModel.createBooking(token, request).observe(getViewLifecycleOwner(), bookingId -> {
                if (bookingId != null && !bookingId.isEmpty()) {
                    sendBookingNotification(requireContext());
                    new android.os.Handler().postDelayed(() -> {
                        Navigation.findNavController(requireView()).navigateUp();
                    }, 1000);
                } else {
                    Toast.makeText(requireContext(), "Đặt lịch thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Vui lòng chọn loại dịch vụ", Toast.LENGTH_SHORT).show();
        }
    }

    private String getBookingTimeISO8601() {
        Calendar bookingDateTime = (Calendar) selectedDate.clone();
        bookingDateTime.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
        bookingDateTime.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
        bookingDateTime.set(Calendar.SECOND, 0);
        bookingDateTime.set(Calendar.MILLISECOND, 0);
        java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault());
        isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        return isoFormat.format(bookingDateTime.getTime());
    }

    private String getSelectedPetId() {
        if (selectedPetModel != null) {
            Log.d("BookingFragment", "Selected petId: " + selectedPetModel.getId());
            return selectedPetModel.getId();
        }
        Log.w("BookingFragment", "Chưa chọn thú cưng!");
        return null;
    }

    private void sendBookingNotification(Context context) {
        String channelId = "booking_notifications";
        String channelName = "Booking Notifications";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_cart) // Đổi icon nếu có icon booking riêng
                .setContentTitle("Đặt lịch thành công")
                .setContentText("Vui lòng vào phần My Booking để xem lại đơn.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2001, builder.build());
    }

    private boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    == android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void showNotificationPermissionDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Bật thông báo")
                .setMessage("Bạn cần bật quyền thông báo để nhận thông báo khi đặt lịch thành công. Bạn có muốn bật không?")
                .setPositiveButton("Bật", (dialog, which) -> {
                    requestNotificationPermission();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void requestNotificationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
    }

    // Nếu muốn xử lý kết quả trả về, override onRequestPermissionsResult trong Fragment
} 