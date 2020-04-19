package com.sap.controllers;


import com.sap.Bootstrap;
import com.sap.response.Response;
import com.sap.services.Service;
import com.sap.services.models.SalesDetails;
import com.sap.utils.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public class Controller {


    @Autowired
    Service service;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @RequestMapping(path = "/ingest/salesDetails", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> salesDetailsIngestion(@RequestBody List<SalesDetails> salesDetails)
    {
        Response response = new Response();
        Map<String,Object> outMap = service.saveSalesDetails(salesDetails);
        response.setCode(40001);
        if (outMap.get(AppConstants.STATUS).toString().equals(AppConstants.SUCCESS)){
            response.setData(outMap.get(AppConstants.DATA));
        }
        response.setMessage(outMap.get(AppConstants.MESSAGE).toString());
        response.setStatus(outMap.get(AppConstants.STATUS).toString());
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/getSalesByAmount", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> getSalesDetailsByDealAmount(@RequestParam(required = false,defaultValue = "AREA") String type)
    {
        Response response = new Response();
        Map<String, Object> outMap = service.getSalesDetailsByDealAmount(type);
        if (outMap.get(AppConstants.STATUS).toString().equals(AppConstants.SUCCESS)){
            response.setData(outMap.get(AppConstants.DATA));
        }
        response.setMessage(outMap.get(AppConstants.MESSAGE).toString());
        response.setStatus(outMap.get(AppConstants.STATUS).toString());
        response.setCode(40002);
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }

    @RequestMapping(path ="/getSalesDetailsByType", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> getSalesByType(@RequestParam(required = false,defaultValue = "AREA_PRODUCT") String type)
    {
        Response response = new Response();
        Map<String, Object> outMap = service.getSalesByProduct(type);
        if (outMap.get(AppConstants.STATUS).toString().equals(AppConstants.SUCCESS)){
            response.setData(outMap.get(AppConstants.DATA));
        }
        response.setMessage(outMap.get(AppConstants.MESSAGE).toString());
        response.setStatus(outMap.get(AppConstants.STATUS).toString());
        response.setCode(40003);
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }

    @RequestMapping(path ="/getStatsOnSales", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> getStatsonSales(@RequestParam(required = false,defaultValue = "AMOUNT") String type)
    {
        Response response = new Response();
        Map<String, Object> outMap = service.getTopAndLeastByType(type);
        if (outMap.get(AppConstants.STATUS).toString().equals(AppConstants.SUCCESS)){
            response.setData(outMap.get(AppConstants.DATA));
        }
        response.setMessage(outMap.get(AppConstants.MESSAGE).toString());
        response.setStatus(outMap.get(AppConstants.STATUS).toString());
        response.setCode(40004);
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }



}
