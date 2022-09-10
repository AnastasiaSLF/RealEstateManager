package com.example.realestatemanager.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.R;
import com.example.realestatemanager.ViewModel.LoanSimulatorViewModel;
import com.example.realestatemanager.databinding.ActivityLoanSimulatorBinding;

public class LoanSimulatorActivity extends AppCompatActivity {

    LoanSimulatorViewModel viewModel;
    ActivityLoanSimulatorBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loan_simulator);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.loan_simulator);
        viewModel = new ViewModelProvider(this).get(LoanSimulatorViewModel.class);
        binding.setBinding(viewModel.getDataBinder());

    }
}
