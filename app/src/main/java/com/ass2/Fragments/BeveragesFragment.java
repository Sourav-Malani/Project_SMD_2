package com.ass2.Fragments;

import static android.os.Build.VERSION_CODES.R;

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

public class BeveragesFragment extends Fragment implements MainAdapter.CartUpdateListener {

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
        View rootView = inflater.inflate(R.layout.fragment_beverages, container, false);
        rectangle_floating_pizza = rootView.findViewById(R.id.rectangle_floating_pizza);
        floating_count_text = rootView.findViewById(R.id.floating_count_text);
        floating_subtotal_text = rootView.findViewById(R.id.floating_subtotal_text);
        relativeLayoutFloating = rootView.findViewById(R.id.rectangle_floating_pizza_beverages);

        CartDBHelper dbHelper = new CartDBHelper(getContext());
        int itemCount = dbHelper.getItemCount(); // You need to implement getItemCount method in CartDBHelper
        if(itemCount == 0) {
            relativeLayoutFloating.setVisibility(View.GONE);
        } else {
            relativeLayoutFloating.setVisibility(View.VISIBLE);
        }

        recyclerView = rootView.findViewById(R.id.recyclerViewBeverages);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayList<MainModel> beveragesList = new ArrayList<>();
        beveragesList.clear();
        // Populate startersList with your starters data

        beveragesList.add(new MainModel(
                R.drawable.pepsi,
                "Pepsi",
                "£1.5",
                "(475ML)",
                "beverages",
                1,
                1));
        beveragesList.add(new MainModel(
                R.drawable.seven_up,
                "7 Up",
                "£1.5",
                "(475ML)",
                "beverages",
                2,
                1));
        beveragesList.add(new MainModel(
                R.drawable.lipton,
                "Lipton Ice Tea",
                "£2.5",
                "(250ML)",
                "beverages",
                3,
                1));
        beveragesList.add(new MainModel(
                R.drawable.pepsi_black,
                "Pepsi Black",
                "£1.4",
                "CAN ",
                "beverages",
                4,
                1));
        beveragesList.add(new MainModel(
                R.drawable.mirinda,
                "Mirinda",
                "£1.5",
                "(475ML)",
                "beverages",
                5,
                1));


        adapter = new MainAdapter(beveragesList, requireActivity(), this);
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