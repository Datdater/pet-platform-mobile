package com.prm392.assignment.productsale.viewmodel.fragment.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import com.prm392.assignment.productsale.data.repository.OrderRepository;
import com.prm392.assignment.productsale.model.orders.OrdersResponseModel;
import com.prm392.assignment.productsale.util.UserAccountManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Response;

public class OrdersViewModel extends ViewModel {
    private final OrderRepository orderRepository;
    private final String token;
    private LiveData<Response<OrdersResponseModel>> ordersLiveData;
    private final Application app;
    
    private final MutableLiveData<Boolean> isLoadingMore = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> hasMoreData = new MutableLiveData<>(true);
    private int currentPage = 1;
    private static final int PAGE_SIZE = 20;

    public OrdersViewModel(@NotNull Application application) {
        super();
        app = application;
        orderRepository = new OrderRepository();
        token = UserAccountManager.getToken(application, UserAccountManager.TOKEN_TYPE_BEARER);
    }

    public static final ViewModelInitializer<OrdersViewModel> initializer = new ViewModelInitializer<>(
            OrdersViewModel.class,
            creationExtras -> {
                Application app = creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new OrdersViewModel(app);
            }
    );

    public LiveData<Response<OrdersResponseModel>> getOrders() {
        currentPage = 1;
        return orderRepository.getOrders(token, currentPage, PAGE_SIZE);
    }

    public void loadOrders(Integer pageNumber, Integer pageSize) {
        ordersLiveData = orderRepository.getOrders(token, pageNumber, pageSize);
    }
    
    public void loadMoreOrders() {
        if (isLoadingMore.getValue() != null && isLoadingMore.getValue()) {
            return; // Already loading
        }
        
        isLoadingMore.setValue(true);
        currentPage++;
        ordersLiveData = orderRepository.getOrders(token, currentPage, PAGE_SIZE);
    }
    
    public LiveData<Boolean> getIsLoadingMore() {
        return isLoadingMore;
    }
    
    public LiveData<Boolean> getHasMoreData() {
        return hasMoreData;
    }
    
    public void setHasMoreData(boolean hasMore) {
        hasMoreData.setValue(hasMore);
    }
    
    public void setLoadingMore(boolean loading) {
        isLoadingMore.setValue(loading);
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
} 