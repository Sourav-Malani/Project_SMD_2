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

public class BurgerPizzaFragment extends Fragment implements MainAdapter.CartUpdateListener {

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
        View rootView = inflater.inflate(R.layout.fragment_burger_pizza, container, false);
        rectangle_floating_pizza = rootView.findViewById(R.id.rectangle_floating_pizza);
        floating_count_text = rootView.findViewById(R.id.floating_count_text);
        floating_subtotal_text = rootView.findViewById(R.id.floating_subtotal_text);
        relativeLayoutFloating = rootView.findViewById(R.id.rectangle_floating_pizza_burger_pizza);

        CartDBHelper dbHelper = new CartDBHelper(getContext());
        int itemCount = dbHelper.getItemCount(); // You need to implement getItemCount method in CartDBHelper
        if(itemCount == 0) {
            relativeLayoutFloating.setVisibility(View.GONE);
        } else {
            relativeLayoutFloating.setVisibility(View.VISIBLE);
        }

        recyclerView = rootView.findViewById(R.id.recyclerViewBurgerPizza);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayList<MainModel> asianList = new ArrayList<>();
        asianList.clear();
        // Populate startersList with your starters data

        asianList.add(new MainModel(
                R.drawable.burger_pizza_classic_veg,
                "Burger Pizza Classic Veg",
                "£5.00",
                "Bite into delight! Witness the epic combination of pizza and burger with our classic Burger Pizza",
                "burger_pizza",
                1,
                1));
        asianList.add(new MainModel(
                R.drawable.burger_pizza_classic_non_veg,
                "Burger Pizza Classic Non Veg",
                "£6.00",
                "For all the meat cravers, try the classic non-veg Burger Pizza loaded with the goodness",
                "burger_pizza",
                2,
                1));
        asianList.add(new MainModel(
                R.drawable.burger_pizza_premium_veg,
                "Burger Pizza Premium Veg",
                "£7.5",
                "The good just got better! Treat yourself to the premium taste of the Burger Pizza",
                "burger_pizza",
                3,
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