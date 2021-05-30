package com.example.servicesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


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
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (int i = 0; i < documentSnapshots.size(); ++i) {
                    User user = documentSnapshots.get(i).toObject(User.class);
                    if (user.getUserType().equals("Babysitter"))
                        users.add(user);
                }
                showProducts();
            }
        });
        return view;
    }

    private void showProducts() {
        EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), users);
        recyclerView.setAdapter(adapter);
    }

}