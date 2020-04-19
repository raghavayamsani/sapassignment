package com.sap.services;

import com.sap.services.models.SalesDetails;

import java.util.List;
import java.util.Map;


public interface Service {



    public Map<String,Object> saveSalesDetails(List<SalesDetails> salesDetails);

    public Map<String,Object> getSalesDetailsByDealAmount(String type);

    public Map<String,Object> getSalesByProduct(String type);

    public Map<Integer,Double> calculateSalesByType(List<SalesDetails> salesDetailsList,String type);

    public Map<String,Object> getTopAndLeastByType(String type);
}
