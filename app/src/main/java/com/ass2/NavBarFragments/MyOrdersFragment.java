package com.ass2.NavBarFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ass2.Adapters.MyOrdersAdapter;
import com.ass2.Helper.OrderDBHelper;
import com.ass2.Models.OrderModel;
import com.ass2.project_smd.R;

import java.util.ArrayList;
import androidx.appcompat.widget.SearchView;
public class MyOrdersFragment extends Fragment {

    RecyclerView myOrdersRecyclerView;
    SearchView searchOrders;
    ImageButton backMyOrders;
    MyOrdersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.myOrdersRecyclerView);
        searchOrders = view.findViewById(R.id.searchOrders);
        backMyOrders = view.findViewById(R.id.backMyOrders);


        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userPrefs", MODE_PRIVATE);
        String userEmail =sharedPrefs.getString("email", "");


        //ArrayList<OrderModel> orderList = createSampleOrderList();

        //Get data from the OderDbHelper.
        OrderDBHelper orderDBHelper = new OrderDBHelper(getContext());
        //ArrayList<OrderModel> orderList = orderDBHelper.getAllOrders();
        ArrayList<OrderModel> orderList = orderDBHelper.getOrdersWithEmail(userEmail);

        adapter = new MyOrdersAdapter(getContext(), orderList);
        myOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myOrdersRecyclerView.setAdapter(adapter);

        setupSearchView();

        backMyOrders.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }

    private void setupSearchView() {
        searchOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Let the SearchView handle the default behavior
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText); // Filter the adapter
                return true;
            }
        });
    }
    // Create a sample list of orders (replace with your data retrieval logic)

}
