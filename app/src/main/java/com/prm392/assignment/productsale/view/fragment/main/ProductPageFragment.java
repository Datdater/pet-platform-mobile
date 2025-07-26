package com.prm392.assignment.productsale.view.fragment.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.databinding.FragmentProductPageBinding;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.products.ProductSaleModel;
import com.prm392.assignment.productsale.model.products.ProductSalePageResponseModel;
import com.prm392.assignment.productsale.model.products.StoreLocation;
import com.prm392.assignment.productsale.util.AppSettingsManager;
import com.prm392.assignment.productsale.util.DialogsProvider;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.viewmodel.fragment.main.ProductPageViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import lecho.lib.hellocharts.view.LineChartView;


public class ProductPageFragment extends Fragment {
    private FragmentProductPageBinding vb;
    private ProductPageViewModel viewModel;
    private NavController navController;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private String productVariantId;

    private GoogleMap googleMap;


    public ProductPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestNotificationPermission();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vb = FragmentProductPageBinding.inflate(inflater, container, false);
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
        ((MainActivity) getActivity()).setTitle(getString(R.string.Product));
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(ProductPageViewModel.initializer))
                .get(ProductPageViewModel.class);
        if (getArguments() != null) viewModel.setProductId(getArguments().getString("productId"));

        new Handler().post(() -> {
            navController = ((MainActivity) getActivity()).getAppNavController();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.product_page_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(map -> {
                this.googleMap = map;

                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            });
        }

        vb.increaseBtn.setOnClickListener(button -> {
            viewModel.setProductQuantity(viewModel.getProductQuantity() + 1);
            vb.txtQuantity.setText(viewModel.getProductQuantity() + "");
        });

        vb.decreaseBtn.setOnClickListener(button -> {
            if (viewModel.getProductQuantity() > 1) {
                viewModel.setProductQuantity(viewModel.getProductQuantity() - 1);
                vb.txtQuantity.setText(viewModel.getProductQuantity() + "");
            }
        });

        vb.txtQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int quantity = Integer.parseInt(vb.txtQuantity.getText().toString());
                    if (quantity < 1) {
                        viewModel.setProductQuantity(1);
                        vb.txtQuantity.setText("1");
                    } else viewModel.setProductQuantity(quantity);
                } catch (Exception e) {
                    viewModel.setProductQuantity(1);
                    vb.txtQuantity.setText("1");
                }
            }
        });

        vb.productPageBack.setOnClickListener(button -> {
            getActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        //Add to cart
        vb.addToCartBtn.setOnClickListener(button -> {
            vb.productPageLoadingPage.setVisibility(View.VISIBLE);
            viewModel.addProductToCart(this.productVariantId).observe(getViewLifecycleOwner(), response -> {
                vb.productPageLoadingPage.setVisibility(View.GONE);
                viewModel.setProductQuantity(1);
                vb.txtQuantity.setText("1");
                DialogsProvider.get(getActivity()).messageDialog("Success","Add to Cart Successful");
                sendCartNotification(requireContext());
                vb.productPageLoadingPage.setVisibility(View.GONE);
            });
        });

        vb.productPageNavigateButton.setOnClickListener(button -> {
//            String address = viewModel.getStoreLocation().getFullAddress(); // Ví dụ: "123 Lý Thường Kiệt, Quận 10, TP.HCM"
            String address = "123 Lý Thường Kiệt, Quận 10, TP.HCM";
            Uri uri = Uri.parse("google.navigation:q=" + Uri.encode(address));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        });

        loadProductSale();
    }
    // Hàm gửi thông báo
    private void sendCartNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        String channelId = "cart_notifications";
        String channelName = "Cart Notifications";

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

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Giỏ hàng")
                .setContentText("Bạn vừa thêm một sản phẩm vào giỏ hàng!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1001, builder.build());
    }


    void loadProductSale() {
        vb.productPageLoadingPage.setVisibility(View.VISIBLE);

        viewModel.getProductSale().observe(getViewLifecycleOwner(), response -> {

            switch (response.code()) {
                case BaseResponseModel.SUCCESSFUL_OPERATION:
                    if (response.body() != null) {
                        viewModel.setProductSaleModel(response.body());
                        renderProductSaleData();
                        vb.productPageLoadingPage.setVisibility(View.GONE);
                        vb.getRoot().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.lay_on));
                    }
                    break;

                case BaseResponseModel.FAILED_NOT_FOUND:
                    DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Product_Not_Found), getString(R.string.Product_Not_Found_in_Server));
                    break;

                case BaseResponseModel.FAILED_REQUEST_FAILURE:
                    Toast.makeText(getContext(), "Loading Failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getContext(), "Server Error | Code: " + response.code(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void renderProductSaleData() {
        ProductSalePageResponseModel productSaleModel = viewModel.getProductSaleModel();
        Map<String, Set<String>> availableAttributeValues = new HashMap<>();
        Map<String, String> selectedAttributes = new HashMap<>(); // user selections

        for (ProductSalePageResponseModel.Variant variant : productSaleModel.getVariants()) {
            for (Map.Entry<String, String> entry : variant.getAttributes().entrySet()) {
                availableAttributeValues
                        .computeIfAbsent(entry.getKey(), k -> new HashSet<>())
                        .add(entry.getValue());
            }
        }

        // --- Set product info ---
        vb.productPageBrand.setText(productSaleModel.getCategoryName());
        vb.productPageTitle.setText(productSaleModel.getName());
        vb.productPageDescription.setText(productSaleModel.getDescription());
        vb.txtQuantity.setText(String.valueOf(viewModel.getProductQuantity()));
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        vb.productPagePrice.setText(format.format(productSaleModel.getBasePrice()));
        vb.textView18.setText("Store: " + productSaleModel.getStoreName());
        vb.productSaleTechSpecsText.setText(buildTechSpecs(productSaleModel));
        vb.productSaleFullDescription.setText(productSaleModel.getDescription());
        vb.productRating.setText(String.valueOf(productSaleModel.getStarAverage()));
        vb.reviewCount.setText(productSaleModel.getReviewCount() + " reviews");
        vb.soldCount.setText(productSaleModel.getSold() + " sold");

        // --- Load main image ---
        String mainImageUrl = getMainImageUrl(productSaleModel.getImages());
        if (mainImageUrl != null && !mainImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(mainImageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .into(vb.productPageImage);
        }

        // --- Render Variant Spinners ---
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        vb.productVariantSection.removeAllViews();

        for (Map.Entry<String, Set<String>> entry : availableAttributeValues.entrySet()) {
            String attrName = entry.getKey();
            List<String> values = new ArrayList<>(entry.getValue());

            View variantView = inflater.inflate(R.layout.item_variant_spinner, vb.productVariantSection, false);
            TextView label = variantView.findViewById(R.id.variant_label);
            Spinner spinner = variantView.findViewById(R.id.variant_spinner);
            label.setText(attrName);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, values);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedAttributes.put(attrName, values.get(position));
                    updatePriceBySelectedVariant(productSaleModel.getVariants(), selectedAttributes);
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });

            vb.productVariantSection.addView(variantView);
        }
    }
    private void updatePriceBySelectedVariant(List<ProductSalePageResponseModel.Variant> variants, Map<String, String> selectedAttrs) {
        for (ProductSalePageResponseModel.Variant variant : variants) {
            if (variant.getAttributes().entrySet().containsAll(selectedAttrs.entrySet())) {
                NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
                vb.productPagePrice.setText(format.format(variant.getPrice()) + " đ");
                // Nếu muốn cập nhật stock:
                // vb.productStock.setText("In stock: " + variant.getStock());
                this.productVariantId = variant.getId();
                return;
            }
        }

        // Nếu không có tổ hợp phù hợp:
        vb.productPagePrice.setText("Không có giá"); // hoặc ẩn, hoặc đặt lại basePrice
    }

    private String getMainImageUrl(List<ProductSalePageResponseModel.Image> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }

        // Find main image first
        for (ProductSalePageResponseModel.Image image : images) {
            if (image.isMain()) {
                return image.getImageUrl();
            }
        }

        // If no main image, return first image
        return images.get(0).getImageUrl();
    }

    private String buildTechSpecs(ProductSalePageResponseModel productSaleModel) {
        StringBuilder specs = new StringBuilder();

        specs.append("Dimensions: ")
                .append(productSaleModel.getLength()).append("mm × ")
                .append(productSaleModel.getWidth()).append("mm × ")
                .append(productSaleModel.getHeight()).append("mm\n");

        specs.append("Weight: ").append(productSaleModel.getWeight()).append("g\n");

        if (productSaleModel.getVariants() != null && !productSaleModel.getVariants().isEmpty()) {
            specs.append("Available Variants: ").append(productSaleModel.getVariants().size());
        }

        return specs.toString();
    }

    public String dateTimeConvert(String dateTime) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";

        try {
            SimpleDateFormat input = new SimpleDateFormat(inputPattern);
            input.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date parsed = input.parse(dateTime);

            SimpleDateFormat destFormat = new SimpleDateFormat(outputPattern);
            destFormat.setTimeZone(TimeZone.getDefault());

            return destFormat.format(parsed);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "-";
    }

}