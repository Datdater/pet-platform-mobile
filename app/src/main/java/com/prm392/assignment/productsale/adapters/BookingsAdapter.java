package com.prm392.assignment.productsale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.services.BookingResponseModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {
    private final List<BookingResponseModel.BookingItem> bookings = new ArrayList<>();
    private final Context context;

    public BookingsAdapter(Context context) {
        this.context = context;
    }

    public void setBookings(List<BookingResponseModel.BookingItem> newBookings) {
        bookings.clear();
        if (newBookings != null) bookings.addAll(newBookings);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item_layout, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingResponseModel.BookingItem booking = bookings.get(position);
        holder.bookingIdText.setText("Booking #" + shortId(booking.getBookingId()));
        holder.bookingStatusText.setText(getStatusText(booking.getStatus()));
        holder.bookingStatusText.setBackgroundResource(getStatusBg(booking.getStatus()));
        holder.bookingDateText.setText(formatDate(booking.getBookingTime()));
        holder.bookingTotalText.setText(formatPrice(booking.getTotalPrice()));
        // Render pet & services
        holder.petWithServicesContainer.removeAllViews();
        if (booking.getPetWithServices() != null) {
            for (BookingResponseModel.BookingItem.PetWithServices pws : booking.getPetWithServices()) {
                View petView = LayoutInflater.from(context).inflate(R.layout.item_booking_pet, holder.petWithServicesContainer, false);
                TextView petInfo = petView.findViewById(R.id.petInfoText);
                String petStr = pws.getPet().getName() + " (" + (pws.getPet().isPetType() ? "Chó" : "Mèo") + ", " + pws.getPet().getColor() + ", DOB: " + (pws.getPet().getDob() != null && pws.getPet().getDob().contains("T") ? pws.getPet().getDob().split("T")[0] : pws.getPet().getDob()) + ")";
                petInfo.setText(petStr);
                LinearLayout servicesContainer = petView.findViewById(R.id.servicesContainer);
                if (pws.getServices() != null) {
                    for (BookingResponseModel.BookingItem.PetWithServices.Service service : pws.getServices()) {
                        View serviceView = LayoutInflater.from(context).inflate(R.layout.item_booking_service, servicesContainer, false);
                        TextView serviceInfo = serviceView.findViewById(R.id.serviceInfoText);
                        serviceInfo.setText(service.getServiceName() + " - " + service.getServiceDetailName() + " (" + formatPrice(service.getPrice()) + ")");
                        servicesContainer.addView(serviceView);
                    }
                }
                holder.petWithServicesContainer.addView(petView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingIdText, bookingStatusText, bookingDateText, bookingTotalText;
        LinearLayout petWithServicesContainer;
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingIdText = itemView.findViewById(R.id.bookingIdText);
            bookingStatusText = itemView.findViewById(R.id.bookingStatusText);
            bookingDateText = itemView.findViewById(R.id.bookingDateText);
            bookingTotalText = itemView.findViewById(R.id.bookingTotalText);
            petWithServicesContainer = itemView.findViewById(R.id.petWithServicesContainer);
        }
    }

    private String shortId(String id) {
        if (id == null || id.length() < 6) return id != null ? id.toUpperCase() : "";
        return id.substring(id.length() - 6).toUpperCase();
    }

    private String getStatusText(int status) {
        switch (status) {
            case 1: return "Đã xác nhận";
            case 2: return "Đang xử lý";
            case 3: return "Hoàn thành";
            case 4: return "Đã hủy";
            default: return "Không rõ";
        }
    }
    private int getStatusBg(int status) {
        switch (status) {
            case 1: return R.drawable.status_confirmed_bg;
            case 2: return R.drawable.status_processing_bg;
            case 3: return R.drawable.status_completed_bg;
            case 4: return R.drawable.status_cancelled_bg;
            default: return R.drawable.status_unknown_bg;
        }
    }
    private String formatDate(String isoDate) {
        try {
            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            Date date = iso.parse(isoDate);
            SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return out.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }
    private String formatPrice(int price) {
        // Format with thousands separator and append the currency symbol
        return String.format(Locale.US, "%,d₫", price);
    }
} 