package com.ass2.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.Models.MainModel;
import com.ass2.Adapters.MainAdapter;
import com.ass2.project_smd.R;

import java.util.ArrayList;

public class StartersFragment extends Fragment implements MainAdapter.CartUpdateListener {

    private RecyclerView recyclerView;
    private MainAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_starters, container, false);

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


        adapter = new MainAdapter(startersList, requireActivity(), this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
        recyclerView.setLayoutManager(layoutManager);


        return rootView;
    }

    @Override
    public void onCartUpdated() {
        // Implement functionality for cart updates if needed
    }
}
