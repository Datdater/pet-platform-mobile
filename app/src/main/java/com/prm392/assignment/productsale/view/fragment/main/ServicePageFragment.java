package com.prm392.assignment.productsale.view.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.adapters.ServiceDetailAdapter;
import com.prm392.assignment.productsale.adapters.ServiceStepAdapter;
import com.prm392.assignment.productsale.databinding.FragmentServicePageBinding;
import com.prm392.assignment.productsale.model.services.ServiceDetailResponseModel;
import com.prm392.assignment.productsale.viewmodel.fragment.main.ServicePageViewModel;
import android.util.Log;
import java.util.ArrayList;

public class ServicePageFragment extends Fragment {
    private FragmentServicePageBinding binding;
    private ServicePageViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentServicePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(ServicePageViewModel.initializer)).get(ServicePageViewModel.class);

        // Lấy token và serviceId từ arguments hoặc SharedPreferences tuỳ logic app
        String token = "Bearer ..."; // TODO: Lấy token thực tế
        String serviceId = getArguments() != null ? getArguments().getString("serviceId") : null;
        Log.d("ServicePageFragment", "serviceId nhận được: " + serviceId);
        if (serviceId == null) {
            Toast.makeText(getContext(), "Không tìm thấy dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.servicePageLoadingPage.setVisibility(View.VISIBLE);
        Log.d("ServicePageFragment", "Gọi fetchServiceDetail với serviceId: " + serviceId);
        viewModel.fetchServiceDetail(token, serviceId);
        viewModel.getServiceDetail().observe(getViewLifecycleOwner(), detail -> {
            Log.d("ServicePageFragment", "Kết quả trả về từ API: " + (detail == null ? "null" : "OK"));
            binding.servicePageLoadingPage.setVisibility(View.GONE);
            if (detail == null) {
                Toast.makeText(getContext(), "Không tải được dữ liệu dịch vụ", Toast.LENGTH_SHORT).show();
                return;
            }
            // Bind dữ liệu ra view
            binding.servicePageTitle.setText(detail.getName());
            binding.servicePageCategory.setText(detail.getServiceCategoryName());
            binding.servicePageBasePrice.setText("Giá cơ bản: " + detail.getBasePrice() + " VNĐ");
            binding.servicePageLocation.setText("Khu vực: " + detail.getStoreCity() + ", " + detail.getStoreDistrict());
            binding.servicePageEstimatedTime.setText("Thời gian ước tính: " + detail.getEstimatedTime());
            binding.servicePageDescription.setText(detail.getDescription());
            binding.serviceRating.setText(String.valueOf(detail.getRatingAverage()));
            binding.serviceReviewCount.setText("(" + detail.getTotalReviews() + " đánh giá)");
            binding.serviceUsedCount.setText(detail.getTotalUsed() + " lượt sử dụng");
            Glide.with(requireContext()).load(detail.getImage()).into(binding.servicePageImage);
            // Adapter cho danh sách loại phòng/dịch vụ
            ServiceDetailAdapter detailAdapter = new ServiceDetailAdapter(getContext(), detail.getPetServiceDetails());
            binding.serviceDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.serviceDetailRecyclerView.setAdapter(detailAdapter);
            // Adapter cho các bước dịch vụ
            ServiceStepAdapter stepAdapter = new ServiceStepAdapter(getContext(), detail.getPetServiceSteps());
            binding.serviceStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.serviceStepRecyclerView.setAdapter(stepAdapter);
        });
        binding.servicePageBack.setOnClickListener(v -> requireActivity().onBackPressed());
        
        // Setup booking button click listener
        binding.serviceBookAppointmentButton.setOnClickListener(v -> {
            // Lấy dữ liệu service detail hiện tại từ ViewModel
            ServiceDetailResponseModel currentDetail = viewModel.getServiceDetail().getValue();
            if (currentDetail != null && currentDetail.getPetServiceDetails() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("serviceId", serviceId);
                bundle.putString("serviceName", currentDetail.getName());
                bundle.putString("serviceCategory", currentDetail.getServiceCategoryName());
                bundle.putInt("basePrice", currentDetail.getBasePrice());
                bundle.putString("storeLocation", currentDetail.getStoreCity() + ", " + currentDetail.getStoreDistrict());
                bundle.putString("estimatedTime", currentDetail.getEstimatedTime());
                bundle.putString("description", currentDetail.getDescription());
                
                // Truyền danh sách service details
                ArrayList<String> serviceDetailNames = new ArrayList<>();
                ArrayList<String> serviceDetailIds = new ArrayList<>();
                ArrayList<Integer> serviceDetailPrices = new ArrayList<>();
                
                for (ServiceDetailResponseModel.PetServiceDetail detail : currentDetail.getPetServiceDetails()) {
                    serviceDetailNames.add(detail.getName());
                    serviceDetailIds.add(detail.getId());
                    serviceDetailPrices.add(detail.getAmount());
                }
                
                bundle.putStringArrayList("serviceDetailNames", serviceDetailNames);
                bundle.putStringArrayList("serviceDetailIds", serviceDetailIds);
                bundle.putIntegerArrayList("serviceDetailPrices", serviceDetailPrices);
                
                Navigation.findNavController(v).navigate(R.id.action_servicePageFragment_to_bookingFragment, bundle);
            } else {
                Toast.makeText(requireContext(), "Không thể tải thông tin dịch vụ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 