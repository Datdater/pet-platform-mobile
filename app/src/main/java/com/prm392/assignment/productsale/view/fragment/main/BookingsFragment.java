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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.adapters.BookingsAdapter;
import com.prm392.assignment.productsale.databinding.FragmentBookingsBinding;
import com.prm392.assignment.productsale.model.services.BookingResponseModel;
import com.prm392.assignment.productsale.util.SharedPrefManager;
import com.prm392.assignment.productsale.viewmodel.fragment.main.BookingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {
    private FragmentBookingsBinding vb;
    private BookingsViewModel viewModel;
    private BookingsAdapter adapter;
    private List<BookingResponseModel.BookingItem> allBookings = new ArrayList<>();

    public BookingsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vb = FragmentBookingsBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vb = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("My Bookings");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BookingsViewModel.class);
        setupRecyclerView();
        setupLoadMoreButton();
        setupObservers();
        loadBookings();
    }

    private void setupRecyclerView() {
        adapter = new BookingsAdapter(getContext());
        vb.bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.bookingsRecyclerView.setAdapter(adapter);
    }

    private void setupLoadMoreButton() {
        vb.loadMoreButton.setOnClickListener(v -> {
            String token = "Bearer " + SharedPrefManager.get(requireContext()).getToken();
            viewModel.loadMoreBookings(token);
        });
    }

    private void setupObservers() {
        viewModel.getBookingsLiveData().observe(getViewLifecycleOwner(), response -> {
            vb.bookingsLoadingPage.setVisibility(View.GONE);
            if (response == null) {
                Toast.makeText(getContext(), "Response null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.isSuccessful()) {
                BookingResponseModel bookingsResponse = response.body();
                if (bookingsResponse != null && bookingsResponse.getItems() != null) {
                    List<BookingResponseModel.BookingItem> newBookings = bookingsResponse.getItems();
                    if (viewModel.getCurrentPage() == 1) {
                        allBookings.clear();
                        allBookings.addAll(newBookings);
                        adapter.setBookings(allBookings);
                    } else {
                        allBookings.addAll(newBookings);
                        adapter.setBookings(allBookings);
                    }
                    boolean hasMore = newBookings.size() >= 10;
                    vb.loadMoreButton.setVisibility(hasMore ? View.VISIBLE : View.GONE);
                    updateUI();
                } else {
                    if (viewModel.getCurrentPage() == 1) {
                        vb.emptyBookingsText.setVisibility(View.VISIBLE);
                        vb.bookingsRecyclerView.setVisibility(View.GONE);
                    }
                    vb.loadMoreButton.setVisibility(View.GONE);
                }
            } else {
                if (viewModel.getCurrentPage() == 1) {
                    Toast.makeText(getContext(), "Loading failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to load more bookings", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            vb.bookingsLoadingPage.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void updateUI() {
        if (allBookings.isEmpty()) {
            vb.emptyBookingsText.setVisibility(View.VISIBLE);
            vb.bookingsRecyclerView.setVisibility(View.GONE);
            vb.loadMoreButton.setVisibility(View.GONE);
        } else {
            vb.emptyBookingsText.setVisibility(View.GONE);
            vb.bookingsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void loadBookings() {
        vb.bookingsLoadingPage.setVisibility(View.VISIBLE);
        String token = "Bearer " + SharedPrefManager.get(requireContext()).getToken();
        viewModel.loadBookings(token, true);
    }
} 