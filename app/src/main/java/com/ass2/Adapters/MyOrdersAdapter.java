package com.ass2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.Models.OrderModel;
import com.ass2.Models.CartModel;
import com.ass2.project_smd.R;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder> implements Filterable {

    //private final Context context;
    private final List<OrderModel> orderList;
    private List<OrderModel> orderListFull; // A full copy of the order list
//    public MyOrdersAdapter(Context context, ArrayList<OrderModel> orderList) {
//        this.context = context;
//        this.orderList = orderList;
//    }
    public MyOrdersAdapter(Context context, List<OrderModel> orderList) {
        this.orderList = orderList;
        orderListFull = new ArrayList<>(orderList); // Initialize with full order list
    }

    @Override
    public Filter getFilter() {
        return orderFilter;
    }

    private Filter orderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OrderModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Iterate through each order in the full list
                for (OrderModel order : orderListFull) {
                    // Iterate through each item in the order's items list
                    for (CartModel item : order.getOrderedItems()) {
                        // Check if the item name or any toppings contain the search pattern
                        if (item.getItemName().toLowerCase().contains(filterPattern) ||
                                item.getSelectedToppingsLeft().toString().toLowerCase().contains(filterPattern) ||
                                item.getSelectedToppingsRight().toString().toLowerCase().contains(filterPattern)) {
                            filteredList.add(order);
                            break; // Break to avoid adding the same order multiple times if multiple items match
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orderList.clear();
            orderList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Bind order data to the ViewHolder
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.customerNameTextView.setText("Customer: " + order.getCustomerName());
        holder.totalPriceTextView.setText("Total Price: " + order.getTotalPrice());
        holder.orderStatusTextView.setText("Status: " + order.getOrderStatus());
        holder.orderDateTextView.setText("Order Date: " + order.getOrderDate());
        holder.orderedItemsTextView.setText("Items: " + order.getOrderedItems());
        holder.customerAddressTextView.setText("Address: " + order.getCustomerAddress());
        holder.customerPhoneTextView.setText("Phone: " + order.getCustomerPhone());
        holder.customerEmailTextView.setText("Email: " + order.getCustomerEmail());
        // Clear old views
        holder.itemsContainer.removeAllViews();
        // Dynamically create TextViews for each item
        for (CartModel item : order.getOrderedItems()) {
            TextView itemView = new TextView(holder.itemView.getContext());
            itemView.setText(item.getItemName() + " - " + item.getItemPrice());
            // Set any desired styling on itemView here, e.g., padding, text size, color
            holder.itemsContainer.addView(itemView);
        }
        // Bind ordered items to a separate RecyclerView if needed
        // You can access the ordered items list using order.getOrderedItems()
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, customerNameTextView,
                totalPriceTextView, orderStatusTextView,
                orderDateTextView, orderedItemsTextView,
                customerAddressTextView, customerPhoneTextView,
                customerEmailTextView;

        LinearLayout itemsContainer;

        public MyOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderedItemsTextView = itemView.findViewById(R.id.orderedItemsTextView);
            customerAddressTextView = itemView.findViewById(R.id.customerAddressTextView);
            customerPhoneTextView = itemView.findViewById(R.id.customerPhoneTextView);
            customerEmailTextView = itemView.findViewById(R.id.customerEmailTextView);
            itemsContainer = itemView.findViewById(R.id.itemsContainer);


        }
    }
}
