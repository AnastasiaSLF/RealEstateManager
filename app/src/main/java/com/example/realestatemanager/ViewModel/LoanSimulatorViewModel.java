package com.example.realestatemanager.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.realestatemanager.ui.LoanSimulatorDataBinding;

public class LoanSimulatorViewModel extends ViewModel {

    private LoanSimulatorDataBinding dataBinder = new LoanSimulatorDataBinding();


    public LoanSimulatorDataBinding getDataBinder() {
        return dataBinder;
    }
}