package com.ass2.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.Helper.CartDBHelper;
import com.ass2.Models.MainModel;
import com.ass2.Adapters.MainAdapter;
import com.ass2.project_smd.DashboardFragment;
import com.ass2.project_smd.R;
import com.ass2.project_smd.cart;

import java.util.ArrayList;
import java.util.Locale;

public class StartersFragment extends Fragment implements MainAdapter.CartUpdateListener {

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
        View rootView = inflater.inflate(R.layout.fragment_starters, container, false);
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

        recyclerView = rootView.findViewById(R.id.recyclerViewStarters);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayList<MainModel> startersList = new ArrayList<>();
        // Populate startersList with your starters data
        startersList.add(new MainModel(
                R.drawable.pizza_image,
                "Create Your Own Pizza",
                "$ 10",
                "Choose From Our Options Of Designa And Make Your Own Pizza.",
                "pizza",
                1,
                0));

        startersList.add(new MainModel(
                R.drawable.pizza2,
                "Fresh Farm House",
                "£14.25",
                "crisp capsicum, succulent mushrooms and fresh tomatoes",
                "pizza",
                2,
                1));
        startersList.add(new MainModel(
                R.drawable.pizza3,
                "Peppy Paneer",
                "£16.75",
                "Chunky paneer with crisp capsicum and spicy red pepperr",
                "pizza",
                3,
                1));
        startersList.add(new MainModel(
                R.drawable.pizza4,
                "Mexican Green Wave",
                "£20.00",
                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
                "pizza",
                4,
                1));
        startersList.add(new MainModel(
                R.drawable.pizza5,
                "Peppy Paneer",
                "£16.75",
                "Chunky paneer with crisp capsicum and spicy red pepper",
                "pizza",
                5,
                1));
        startersList.add(new MainModel(
                R.drawable.pizza6,
                "Mexican Green Wave",
                "£20.00",
                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
                "pizza",
                6,
                1));
        startersList.add(new MainModel(
                R.drawable.veg_extravaganz,
                "Veg Extravaganza",
                "£10.00",
                "Black olives, capsicum, onion, grilled mushroom, corn, tomato, jalapeno & extra cheese",
                "pizza",
                7,
                1));
        startersList.add(new MainModel(
                R.drawable.margherita,
                "Margherita",
                "£16.00",
                "A hugely popular margherita, with a deliciously tangy single cheese topping",
                "pizza",
                8,
                1));
        startersList.add(new MainModel(
                R.drawable.veggie_paradise,
                "Veggie Paradise",
                "£14.75",
                "oldern Corn, Black Olives, Capsicum & Red Paprika",
                "pizza",
                9,
                1));
//        MainAdapter adapter = new MainAdapter(list, requireActivity(), this);
//        recyclerViewCards.setAdapter(adapter);
//
//        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
//        recyclerViewCards.setLayoutManager(layoutManager);

        adapter = new MainAdapter(startersList, requireActivity(), this);
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
