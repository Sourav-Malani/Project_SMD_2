package com.ass2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.Helper.CartDBHelper;
import com.ass2.Models.CartModel;
import com.ass2.Models.MainModel;
import com.ass2.project_smd.R;
import com.ass2.project_smd.cart;
import com.ass2.project_smd.create_your_own_pizza;

import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LAYOUT_1 = 0;
    private static final int VIEW_TYPE_LAYOUT_2 = 1;
    private static ArrayList<CartModel> list;
    private static Context context;

    private TextView subtotalTextView;


    public CartAdapter(ArrayList<CartModel> list, Context context, TextView subtotalTextView) {
        this.list = list;
        this.context = context;
        this.subtotalTextView = subtotalTextView;
    }

    public CartAdapter(ArrayList<CartModel> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void updateSubtotal() {
        CartDBHelper dbHelper = new CartDBHelper(context);
        double subtotalValue = dbHelper.calculateSubtotal();
        subtotalTextView.setText(String.format(Locale.getDefault(), "%.2f", subtotalValue));
    }

    @Override
    public int getItemViewType(int position) {
        final CartModel model = list.get(position);
        return model.getViewType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case VIEW_TYPE_LAYOUT_1:
                View layout2View = inflater.inflate(R.layout.create_your_own_pizza_layout, parent, false);
                return new CartAdapter.Layout2ViewHolder(layout2View, this);
            case VIEW_TYPE_LAYOUT_2:
            default:
                View layout1View = inflater.inflate(R.layout.cart_layout, parent, false);
                return new CartAdapter.Layout1ViewHolder(layout1View, this);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CartModel model = list.get(position);

        if (holder instanceof Layout1ViewHolder) {
            Layout1ViewHolder layout1ViewHolder = (Layout1ViewHolder) holder;
            layout1ViewHolder.bindLayout1Data(model);
            layout1ViewHolder.adapter = this; // Ensure the adapter reference is set
        } else if (holder instanceof Layout2ViewHolder) {
            Layout2ViewHolder layout2ViewHolder = (Layout2ViewHolder) holder;
            layout2ViewHolder.bindLayout2Data(model);
            layout2ViewHolder.adapter = this; // Ensure the adapter reference is set
        }
    }

    private static class Layout1ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName,itemPrice,itemDescription,itemCount;
        ImageButton minusButton,plusButton,crossButton;

        private CartAdapter adapter;

        Layout1ViewHolder(View itemView, CartAdapter adapter) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemCount = itemView.findViewById(R.id.itemCount);
            minusButton = itemView.findViewById(R.id.btn_minus_item);
            plusButton = itemView.findViewById(R.id.btn_plus_item);
            crossButton = itemView.findViewById(R.id.cross);

            this.adapter = adapter;
        }

        void bindLayout1Data(CartModel item) {
            // Bind data for layout 1
            itemImage.setImageResource(item.getItemImage());


            itemName.setText(item.getItemName());
            itemPrice.setText(item.getItemPrice());
            itemDescription.setText(item.getItemDescription());
            itemCount.setText(String.valueOf(item.getItemCount()));



            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(itemCount.getText().toString());
                    if (count > 1) {
                        count--;
                        double newPrice = calculateNewPrice(item, count);
                        itemPrice.setText(String.format("£%.2f", newPrice));
                        itemCount.setText(String.valueOf(count));
                        item.setItemPrice(String.valueOf(newPrice));
                        item.setItemCount(count);
                        updateItemInDB(item);
                    }
                    else {
                        int adapterPosition = getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            // Access list and notifyItemRemoved through the adapter instance
                            adapter.removeItem(adapterPosition);
                        }
                    }
                    adapter.updateSubtotal();
                }
            });

            crossButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        // Access list and notifyItemRemoved through the adapter instance
                        adapter.removeItem(adapterPosition);
                        adapter.updateSubtotal();
                    }

                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(itemCount.getText().toString());
                    if(count<4) { //Restrict to 3
                        count++;
                        double newPrice = calculateNewPrice(item, count);
                        itemPrice.setText(String.format("£%.2f", newPrice));
                        itemCount.setText(String.valueOf(count));
                        item.setItemPrice(String.valueOf(newPrice));
                        item.setItemCount(count);
                        updateItemInDB(item);
                    }
                    adapter.updateSubtotal();
                }
            });
        }
    }
    public void removeItem(int position) {
        CartModel removedItem = list.get(position);
        CartDBHelper dbHelper = new CartDBHelper(context);
        dbHelper.deleteCartItem(removedItem); // Method to delete the item from the database
        list.remove(position);
        notifyItemRemoved(position);
    }
    private static double calculateNewPrice(CartModel item, int newCount) {
        double unitPrice = -1.0f;
        if(item.getViewType() == VIEW_TYPE_LAYOUT_1){
            unitPrice = item.getUnitPrice(item.getItemSize(), item.getItemCrust());
        }
        else if(item.getViewType() == VIEW_TYPE_LAYOUT_2){
            String unitPriceString = item.getItemPrice();
            unitPrice = parseUnitPrice(unitPriceString);
        }
        return unitPrice * newCount;
    }

    private static void updateItemInDB(CartModel item) {
        CartDBHelper dbHelper = new CartDBHelper(context);
        boolean updateSuccess = dbHelper.updateCartItem(item);
        Log.d("CartAdapterDebug", "Update success: " + updateSuccess);
    }
    private static double parseUnitPrice(String unitPriceString) {
        // Remove the currency symbol and any non-numeric characters
        String cleanedPriceString = unitPriceString.replaceAll("[^0-9.]", "");

        // Parse the cleaned string as a double
        try {
            return Double.parseDouble(cleanedPriceString);
        } catch (NumberFormatException e) {
            // Handle any parsing errors here
            e.printStackTrace();
            return 0.0; // Return a default value or handle the error as needed
        }
    }

    private static void updateItemCountInDB(CartModel item, int newCount) {
        // Update item count in the database
        CartDBHelper dbHelper = new CartDBHelper(context);
        item.setItemCount(newCount);
        boolean updateSuccess = dbHelper.updateCartItem(item);
        Log.d("CartAdapterDebug", "Update success: " + updateSuccess);
    }
    // ViewHolder for layout 2
    private static class Layout2ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName,itemPrice,itemDescription,itemCountT;
        TextView sauceLeft, sauceRight;
        TextView toppingsLeft, toppingsRight;
        ImageButton minusButton,plusButton,crossButton;
        private CartAdapter adapter;



        Layout2ViewHolder(View itemView, CartAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);

            //itemDescription = itemView.findViewById(R.id.itemDescription);
            itemCountT = itemView.findViewById(R.id.itemCountYO);
            sauceLeft = itemView.findViewById(R.id.itemSauce_left);
            sauceRight = itemView.findViewById(R.id.itemSauce_right);
            toppingsLeft = itemView.findViewById(R.id.itemToppings_left);
            toppingsRight = itemView.findViewById(R.id.itemToppings_right);



            minusButton = itemView.findViewById(R.id.btn_minus_item);
            plusButton = itemView.findViewById(R.id.btn_plus_item);
            crossButton = itemView.findViewById(R.id.cross);
        }

        void bindLayout2Data(CartModel item) {
            // Bind data for layout 1
            itemImage.setImageResource(item.getItemImage());


            itemName.setText(item.getItemName());
            itemPrice.setText(item.getItemPrice());
            //itemDescription.setText(item.getItemDescription());
            itemCountT.setText(String.valueOf(item.getItemCount()));
            sauceLeft.setText(item.getSelectedSauceLeft());
            sauceRight.setText(item.getSelectedSauceRight());

            //toppingsLeft.setText(item.getSelectedToppingsLeft().toString());
            //toppingsRight.setText(item.getSelectedToppingsRight().toString());
            // Bind toppings data
            if (item.getSelectedToppingsLeft() != null) {
                StringBuilder leftToppingsBuilder = new StringBuilder();
                for (String topping : item.getSelectedToppingsLeft()) {
                    leftToppingsBuilder.append(topping).append(", ");
                }
                String leftToppings = leftToppingsBuilder.toString();
                // Remove the trailing comma and space
                if (leftToppings.length() > 2) {
                    leftToppings = leftToppings.substring(0, leftToppings.length() - 2);
                }
                toppingsLeft.setText(leftToppings);
            }

            if (item.getSelectedToppingsRight() != null) {
                StringBuilder rightToppingsBuilder = new StringBuilder();
                for (String topping : item.getSelectedToppingsRight()) {
                    rightToppingsBuilder.append(topping).append(", ");
                }
                String rightToppings = rightToppingsBuilder.toString();
                // Remove the trailing comma and space
                if (rightToppings.length() > 2) {
                    rightToppings = rightToppings.substring(0, rightToppings.length() - 2);
                }
                toppingsRight.setText(rightToppings);
            }



            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(itemCountT.getText().toString());
                    if (count > 1) {
                        count--;
                        double newPrice = calculateNewPrice(item, count);
                        itemPrice.setText(String.format("£%.2f", newPrice));
                        itemCountT.setText(String.valueOf(count));
                        item.setItemPrice(String.valueOf(newPrice));
                        item.setItemCount(count);
                        updateItemInDB(item);

                    } else {
                        int adapterPosition = getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            // Access list and notifyItemRemoved through the adapter instance
                            adapter.removeItem(adapterPosition);
                            //remove item from database


                        }
                    }
                    adapter.updateSubtotal();
                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(itemCountT.getText().toString());
                    if(count<4){
                        count++;
                        double newPrice = calculateNewPrice(item, count);
                        itemPrice.setText(String.format("£%.2f", newPrice));
                        itemCountT.setText(String.valueOf(count));
                        item.setItemPrice(String.valueOf(newPrice));
                        item.setItemCount(count);
                        updateItemInDB(item);
                    }
                    adapter.updateSubtotal();
                }

            });

            crossButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        // Access list and notifyItemRemoved through the adapter instance
                        adapter.removeItem(adapterPosition);
                        adapter.updateSubtotal();
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}