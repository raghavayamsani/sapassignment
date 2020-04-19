package com.sap.services;


import com.sap.services.models.SalesDetails;
import com.sap.services.models.Stats;
import com.sap.utils.AppConstants;
import com.sap.utils.ServiceTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service{

    private static final Logger LOG = LogManager.getLogger(ServiceImpl.class);

    public static List<SalesDetails> salesDetailsList = new ArrayList<SalesDetails>();

    public Map<String,Object> saveSalesDetails(List<SalesDetails> salesDetails){
        LOG.debug("Enterted into saveSalesDetails Method");
        Map<String, Object> outMap = new HashMap<>();
        try {
            salesDetailsList = salesDetails;
            outMap.put(AppConstants.DATA, AppConstants.INGESTION_DATA_VALUE);
            outMap.put(AppConstants.MESSAGE, AppConstants.INGESTION_DATA_VALUE);
            outMap.put(AppConstants.STATUS, AppConstants.SUCCESS);

        } catch (Exception e) {
            outMap.put(AppConstants.MESSAGE, e.getMessage());
            outMap.put(AppConstants.STATUS,AppConstants.FAILURE);
            LOG.error(e.getMessage());
        }
        return outMap;
    }


    public Map<String,Object> getSalesDetailsByDealAmount(String type)
    {
        LOG.debug("Enterted into getSalesDetailsByDealAmount Method");
        Map<String,Object> outMap = new HashMap<>();
        try {
            List<SalesDetails> salesList = salesDetailsList;
            Map<Integer,Double> finalMap = calculateSalesByType(salesList,type);
            outMap.put(AppConstants.DATA, finalMap);
            outMap.put(AppConstants.MESSAGE, AppConstants.MESSAGE_VALUE.replace(AppConstants.TYPE, type));
            outMap.put(AppConstants.STATUS, AppConstants.SUCCESS);
        } catch (Exception e) {
            outMap.put(AppConstants.MESSAGE, e.getMessage());
            outMap.put(AppConstants.STATUS,AppConstants.FAILURE);
            LOG.error(e.getMessage());
        }
        return outMap;
    }

    public Map<Integer,Double> calculateSalesByType(List<SalesDetails> salesList,String type)
    {
        LOG.debug("Enterted into calculateSalesByType Method");
        Map<Integer, Double> finalMap = new HashMap<>();
        Map<Integer, DoubleSummaryStatistics> salesInfo = new HashMap<>();
        ServiceTypes serviceType = ServiceTypes.valueOf(type);
        switch (serviceType){
            case AREA:
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getAreaCode, Collectors.summarizingDouble(SalesDetails::getDealAmount)));
                break;
            case PRODUCT:
                salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getProductId, Collectors.summarizingDouble(SalesDetails::getDealAmount)));
                break;
            case SALESREP:
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

    public Map<String,Object> getSalesByProduct(String type){
        LOG.debug("Enterted into getSalesByProduct Method");
        Map<String,Object> outMap = new HashMap<>();
        try {
            Map<Integer,Map<Integer,Double>> finalMap = new HashMap<>();
            List<SalesDetails> salesList = salesDetailsList;
            Map<Integer,  Map<Integer,DoubleSummaryStatistics>> salesInfo = new HashMap<>();
            ServiceTypes serviceType = ServiceTypes.valueOf(type);
            switch (serviceType)
            {
                case AREA_PRODUCT:
                    salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getAreaCode, Collectors.groupingBy(SalesDetails::getProductId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                    break;
                case SALESREP_PRODUCT:
                    salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getSalesRepId, Collectors.groupingBy(SalesDetails::getProductId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                    break;
                case PRODUCT_AREA:
                    salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getProductId, Collectors.groupingBy(SalesDetails::getAreaCode,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                    break;
                case SALESREP_AREA:
                    salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getSalesRepId, Collectors.groupingBy(SalesDetails::getAreaCode,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                    break;
                case PRODUCT_SALESREP:
                    salesInfo = salesList.stream().collect(Collectors.groupingBy(SalesDetails::getProductId, Collectors.groupingBy(SalesDetails::getSalesRepId,Collectors.summarizingDouble(SalesDetails::getDealAmount))));
                    break;
                case AREA_SALESREP:
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
            outMap.put(AppConstants.DATA, finalMap);
            outMap.put(AppConstants.MESSAGE, AppConstants.MESSAGE_VALUE.replace(AppConstants.TYPE, type));
            outMap.put(AppConstants.STATUS, AppConstants.SUCCESS);
        } catch (Exception e) {
            outMap.put(AppConstants.MESSAGE, e.getMessage());
            outMap.put(AppConstants.STATUS,AppConstants.FAILURE);
            LOG.error(e.getMessage());
        }
        return outMap;
    }

    public Map<String,Object> getTopAndLeastByType(String type){
        LOG.debug("Enterted into getTopAndLeastByType Method");
        Map<String,Object> outMap = new HashMap<>();
        try{
            List<Stats> topandLowList = new ArrayList<>();
            List<SalesDetails> salesList = salesDetailsList;
            Map<Integer,Double> finalMap = calculateSalesByType(salesList,type);
            if (!finalMap.isEmpty())
            {
                Stats top = new Stats();
                Stats low = new Stats();
                top.setType(AppConstants.TOP);
                top.setId(Collections.max(finalMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey());
                top.setDealAmount(Collections.max(finalMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue());
                topandLowList.add(top);
                low.setType(AppConstants.LOW);
                low.setId(Collections.min(finalMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey());
                low.setDealAmount(Collections.min(finalMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue());
                topandLowList.add(low);
            }
            outMap.put(AppConstants.DATA, topandLowList);
            outMap.put(AppConstants.MESSAGE, AppConstants.MESSAGE_VALUE.replace(AppConstants.TYPE,type));
            outMap.put(AppConstants.STATUS, AppConstants.SUCCESS);
        }catch (Exception e){
            outMap.put(AppConstants.MESSAGE, e.getMessage());
            outMap.put(AppConstants.STATUS,AppConstants.FAILURE);
            LOG.error(e.getMessage());
        }
        return outMap;
    }

}

