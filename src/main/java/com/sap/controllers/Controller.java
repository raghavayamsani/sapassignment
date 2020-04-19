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

    private static final Logger LOG = LogManager.getLogger(Bootstrap.class);

    @RequestMapping(path = "/ingest/salesDetails", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> salesDetailsIngestion(@RequestBody List<SalesDetails> salesDetails)
    {
        Response response = new Response();
        String status = service.saveSalesDetails(salesDetails);
        response.setCode(40001);
        response.setMessage(status);
        response.setStatus(AppConstants.SUCCESS);
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/getSalesByAmount", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> getSalesDetailsByDealAmount(@RequestParam(required = false,defaultValue = "AREA") String type)
    {
        Response response = new Response();
        Map<Integer, Double> salesDetails = service.getSalesDetailsByDealAmount(type);
        response.setData(salesDetails);
        response.setStatus(AppConstants.SUCCESS);
        response.setCode(40002);
        response.setMessage("Fetch Data for " + type + " Successfully");
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }

    @RequestMapping(path ="/getSalesDetailsByType", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> getSalesByProduct(@RequestParam(required = false,defaultValue = "AREA-PRODUCT") String type)
    {
        Response response = new Response();
        Map<Integer, Map<Integer,Double>> salesDetails = service.getSalesByProduct(type);
        response.setData(salesDetails);
        response.setStatus(AppConstants.SUCCESS);
        response.setCode(40003);
        response.setMessage("Fetched Data for " + type + " Successfully");
        return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
    }

}
