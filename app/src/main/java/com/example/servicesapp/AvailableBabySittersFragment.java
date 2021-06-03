package com.example.servicesapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class AvailableBabySittersFragment extends Fragment {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> searchList = new ArrayList<>();
    private RecyclerView recyclerView;

    public AvailableBabySittersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_employees, container, false);
        recyclerView = view.findViewById(R.id.rv);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("users");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                users.clear();
                for (int i = 0; i < documentSnapshots.size(); ++i) {
                    User user = documentSnapshots.get(i).toObject(User.class);
                    if (user.getUserType().equals("Babysitter") && user.getHourlyRate() != null)
                        users.add(user);
                }
                showProducts();
            }
        });

        EditText searchET = view.findViewById(R.id.et_search);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchList.clear();
                for (User user : users) {
                    if (user.getLocation().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchList.add(user);
                    }
                }
                EmployeeAdapter adapter =
                        new EmployeeAdapter(getActivity(), searchList);
               recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    private void showProducts() {
        EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), users);
        recyclerView.setAdapter(adapter);
    }

}