package com.xlf.restroom.api;

import com.xlf.restroom.pojo.Toilet;

import java.util.List;


public interface IRestroomService {

    public List<Toilet> getAvailableToilet();

    public Toilet occpy(Long id);

    public Toilet release(Long id);

    public boolean checkAvailablility(Long id) throws InterruptedException;

    String demo();

    Toilet test(String input);

    void hystrixDemo(Long count) throws InterruptedException;

}
