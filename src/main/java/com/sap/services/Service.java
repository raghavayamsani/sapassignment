package com.sap.services;

import com.sap.services.models.SalesDetails;

import java.util.List;
import java.util.Map;


public interface Service {



    public String saveSalesDetails(List<SalesDetails> salesDetails);

    public Map<Integer, Double> getSalesDetailsByDealAmount(String type);

    public Map<Integer,Map<Integer,Double>> getSalesByProduct(String type);
}
