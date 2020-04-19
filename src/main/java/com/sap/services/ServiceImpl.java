package com.sap.services;


import com.sap.services.models.SalesDetails;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service{

    public static List<SalesDetails> salesDetailsList = new ArrayList<SalesDetails>();

    public String saveSalesDetails(List<SalesDetails> salesDetails){
        HashMap<String, Object> outMap = new HashMap<String, Object>();
        salesDetailsList = salesDetails;
        return "Sales Detatils successfully";
    }


    public Map<Integer,Double> getSalesDetailsByDealAmount(String type)
    {
        List<SalesDetails> salesList = salesDetailsList;
        Map<Integer, Double> finalMap = new HashMap<>();
        Map<Integer, DoubleSummaryStatistics> salesInfo = new HashMap<>();
        switch (type){
            case "AREA":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getAreaCode, Collectors.summarizingDouble(SalesDetails::getDealAmount)));
                break;
            case "PRODUCT":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getProductId, Collectors.summarizingDouble(SalesDetails::getDealAmount)));
                break;
            case "SALESREP":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getSalesRepId, Collectors.summarizingDouble(SalesDetails::getDealAmount)));
                break;
        }
        if (!salesInfo.isEmpty())
        {
            Set<Integer> keys = salesInfo.keySet();
            for (Integer key: keys){
                finalMap.put(key, salesInfo.get(key).getSum());
            }
        }
        return finalMap;
    }

    public Map<Integer,Map<Integer,Double>> getSalesByProduct(String type){
        Map<Integer,Map<Integer,Double>> finalMap = new HashMap<>();
        List<SalesDetails> salesList = salesDetailsList;
        Map<Integer,  Map<Integer,DoubleSummaryStatistics>> salesInfo = new HashMap<>();

        switch (type)
        {
            case "AREA-PRODUCT":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getAreaCode, Collectors.groupingBy(SalesDetails::getProductId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                break;
            case "SALESREP-PRODUCT":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getSalesRepId, Collectors.groupingBy(SalesDetails::getProductId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                break;
            case "PRODUCT-AREA":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getProductId, Collectors.groupingBy(SalesDetails::getAreaCode,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                break;
            case "SALESREP-AREA":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getSalesRepId, Collectors.groupingBy(SalesDetails::getAreaCode,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                break;
            case "PRODUCT-SALESREP":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getProductId, Collectors.groupingBy(SalesDetails::getSalesRepId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                break;
            case "AREA-SALESREP":
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getAreaCode, Collectors.groupingBy(SalesDetails::getSalesRepId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                break;
        }

        if (!salesInfo.isEmpty()){
            Set<Integer> keys = salesInfo.keySet();
            for (Integer key: keys){
                HashMap<Integer, Double> productMap = new HashMap<>();
                Set<Integer> productKeys = salesInfo.get(key).keySet();
                for (Integer productKey: productKeys){
                    productMap.put(productKey, salesInfo.get(key).get(productKey).getSum());
                }
                finalMap.put(key,productMap);
            }
        }
        return finalMap;
    }

}

