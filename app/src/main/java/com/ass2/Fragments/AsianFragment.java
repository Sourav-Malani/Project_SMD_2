package com.ass2.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ass2.Adapters.MainAdapter;
import com.ass2.Helper.CartDBHelper;
import com.ass2.Models.MainModel;
import com.ass2.project_smd.R;
import com.ass2.project_smd.cart;

import java.util.ArrayList;
import java.util.Locale;

public class AsianFragment extends Fragment implements MainAdapter.CartUpdateListener {

    private RecyclerView recyclerView;
    private MainAdapter adapter;
    TextView floating_count_text,floating_subtotal_text;
    View rectangle_floating_pizza;
    RelativeLayout relativeLayoutFloating;

    @Override
    public void onCartUpdated() {
        updateCartUI();
    }
    public void updateCartUI() {
        CartDBHelper dbHelper = new CartDBHelper(getContext());
        double subtotal = dbHelper.calculateSubtotal();
        int itemCount = dbHelper.getItemCount(); // You need to implement getItemCount method in CartDBHelper
        if(itemCount == 0) {
            relativeLayoutFloating.setVisibility(View.GONE);
        } else {
            relativeLayoutFloating.setVisibility(View.VISIBLE);
        }

        floating_subtotal_text.setText(String.format(Locale.getDefault(), "£%.2f", subtotal));
        floating_count_text.setText(String.valueOf(itemCount));
    }
    @Override
    public void onResume() {
        super.onResume();
        updateCartUI(); // Call your method to update UI
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_asian, container, false);
        rectangle_floating_pizza = rootView.findViewById(R.id.rectangle_floating_pizza);
        floating_count_text = rootView.findViewById(R.id.floating_count_text);
        floating_subtotal_text = rootView.findViewById(R.id.floating_subtotal_text);
        relativeLayoutFloating = rootView.findViewById(R.id.rectangle_floating_pizza_starters);

        CartDBHelper dbHelper = new CartDBHelper(getContext());
        int itemCount = dbHelper.getItemCount(); // You need to implement getItemCount method in CartDBHelper
        if(itemCount == 0) {
            relativeLayoutFloating.setVisibility(View.GONE);
        } else {
            relativeLayoutFloating.setVisibility(View.VISIBLE);
        }

        recyclerView = rootView.findViewById(R.id.recyclerViewAsian);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayList<MainModel> asianList = new ArrayList<>();
        asianList.clear();
        // Populate startersList with your starters data

        asianList.add(new MainModel(
                R.drawable.profile_image,
                "Asian",
                "£14.25",
                "crisp capsicum, succulent mushrooms and fresh tomatoes",
                "pizza",
                1,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "AsianTwo",
                "£16.75",
                "Chunky paneer with crisp capsicum and spicy red pepperr",
                "pizza",
                3,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "Mexican Green Wave",
                "£20.00",
                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
                "pizza",
                4,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "Peppy Paneer",
                "£16.75",
                "Chunky paneer with crisp capsicum and spicy red pepper",
                "pizza",
                5,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "Mexican Green Wave",
                "£20.00",
                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
                "pizza",
                6,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "Veg Extravaganza",
                "£10.00",
                "Black olives, capsicum, onion, grilled mushroom, corn, tomato, jalapeno & extra cheese",
                "pizza",
                7,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "Margherita",
                "£16.00",
                "A hugely popular margherita, with a deliciously tangy single cheese topping",
                "pizza",
                8,
                1));
        asianList.add(new MainModel(
                R.drawable.pizza_image,
                "Veggie Paradise",
                "£14.75",
                "oldern Corn, Black Olives, Capsicum & Red Paprika",
                "pizza",
                9,
                1));


        adapter = new MainAdapter(asianList, requireActivity(), this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
        recyclerView.setLayoutManager(layoutManager);
        rectangle_floating_pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), cart.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


}