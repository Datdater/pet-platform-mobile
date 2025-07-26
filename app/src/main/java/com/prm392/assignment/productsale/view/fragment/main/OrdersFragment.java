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
import com.prm392.assignment.productsale.adapters.OrdersAdapter;
import com.prm392.assignment.productsale.databinding.FragmentOrdersBinding;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.orders.OrderModel;
import com.prm392.assignment.productsale.model.orders.OrdersResponseModel;
import com.prm392.assignment.productsale.util.DialogsProvider;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.viewmodel.fragment.main.OrdersViewModel;
import com.prm392.assignment.productsale.viewmodel.fragment.main.home.OnSaleViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private FragmentOrdersBinding vb;
    private OrdersViewModel viewModel;
    private OrdersAdapter adapter;
    private List<OrderModel> allOrders = new ArrayList<>();

    public OrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vb = FragmentOrdersBinding.inflate(inflater, container, false);
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
        ((MainActivity) getActivity()).setTitle("Đơn hàng");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(OrdersViewModel.initializer)).get(OrdersViewModel.class);

        setupRecyclerView();
        setupLoadMoreButton();
        setupObservers();
        loadOrders();
    }

    private void setupRecyclerView() {
        adapter = new OrdersAdapter(getContext());
        vb.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.ordersRecyclerView.setAdapter(adapter);
    }
    
    private void setupLoadMoreButton() {
        vb.loadMoreButton.setOnClickListener(v -> {
            viewModel.loadMoreOrders();
        });
    }

    private void setupObservers() {
        // Observer for initial load and load more
        viewModel.getOrders().observe(getViewLifecycleOwner(), response -> {
            vb.ordersLoadingPage.setVisibility(View.GONE);
            viewModel.setLoadingMore(false);
            
            if (response == null) {
                Toast.makeText(getContext(), "Response null", Toast.LENGTH_SHORT).show();
                return;
            }

            if (response.isSuccessful()) {
                OrdersResponseModel ordersResponse = response.body();
                if (ordersResponse != null && ordersResponse.getOrders() != null) {
                    List<OrderModel> newOrders = ordersResponse.getOrders();
                    
                    if (viewModel.getCurrentPage() == 1) {
                        // First page - replace all orders
                        allOrders.clear();
                        allOrders.addAll(newOrders);
                        adapter.setOrders(allOrders);
                    } else {
                        // Load more - append to existing orders
                        allOrders.addAll(newOrders);
                        adapter.setOrders(allOrders);
                    }
                    
                    // Check if there are more orders to load
                    boolean hasMore = newOrders.size() >= 20; // If we got full page, there might be more
                    viewModel.setHasMoreData(hasMore);
                    
                    updateUI();
                } else {
                    if (viewModel.getCurrentPage() == 1) {
                        vb.emptyOrdersText.setVisibility(View.VISIBLE);
                        vb.ordersRecyclerView.setVisibility(View.GONE);
                    }
                    viewModel.setHasMoreData(false);
                }
            } else {
                if (viewModel.getCurrentPage() == 1) {
                    DialogsProvider.get(getActivity()).messageDialog(
                            getString(R.string.Loading_Failed),
                            getString(R.string.Please_Check_your_connection)
                    );
                } else {
                    Toast.makeText(getContext(), "Failed to load more orders", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // Observer for load more button visibility
        viewModel.getHasMoreData().observe(getViewLifecycleOwner(), hasMore -> {
            vb.loadMoreButton.setVisibility(hasMore ? View.VISIBLE : View.GONE);
        });
        
        // Observer for loading more state
        viewModel.getIsLoadingMore().observe(getViewLifecycleOwner(), isLoading -> {
            vb.loadMoreButton.setEnabled(!isLoading);
            vb.loadMoreButton.setText(isLoading ? "Loading..." : "Load More");
        });
    }
    
    private void updateUI() {
        if (allOrders.isEmpty()) {
            vb.emptyOrdersText.setVisibility(View.VISIBLE);
            vb.ordersRecyclerView.setVisibility(View.GONE);
            vb.loadMoreButton.setVisibility(View.GONE);
        } else {
            vb.emptyOrdersText.setVisibility(View.GONE);
            vb.ordersRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void loadOrders() {
        vb.ordersLoadingPage.setVisibility(View.VISIBLE);
        viewModel.getOrders(); // This will load the first page
    }
} 