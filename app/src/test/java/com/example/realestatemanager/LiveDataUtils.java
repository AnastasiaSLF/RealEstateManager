package com.example.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.realestatemanager.dao.EstateDao;
import com.example.realestatemanager.modele.Property;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LiveDataUtils {
    public static synchronized <T> T getLiveDataValue(LiveData<T> liveData)
            throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Object[] result = new Object[1];
        liveData.observeForever(
                new Observer<T>() {
                    @Override
                    public void onChanged(T value) {
                        result[0] = value;
                        synchronized (countDownLatch) {
                            countDownLatch.countDown();
                        }
                        liveData.removeObserver(this);
                    }
                });
        if (countDownLatch.getCount() > 0) {
            synchronized (countDownLatch) {
                countDownLatch.wait(100);
            }
        }
        return ( T ) result[0];
    }
}
