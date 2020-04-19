package com.sap;

import com.sap.services.models.SalesDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AnalyticsTests {

    @LocalServerPort
    int randomServerPort;

    @Test
    @Order(1)
    public void testSecurity() throws  Exception, URISyntaxException{
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/getSalesByAmount";
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result = restTemplate.withBasicAuth("raghavay","password").getForEntity(uri,String.class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode() );
    }

    @Test
    @Order(2)
    public void testIngestionSalesDetails() throws URISyntaxException{
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/ingest/salesDetails";
        URI uri = new URI(baseUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("raghavay", "password");
        SalesDetails salesDetails = new SalesDetails();
        List<SalesDetails> salesDetailsList = new ArrayList<>();
        salesDetails.setAreaCode(900103);
        salesDetails.setDealAmount(45679.0);
        salesDetails.setProductId(5010);
        salesDetails.setSalesRepId(600050);
        salesDetailsList.add(salesDetails);
        HttpEntity<List<SalesDetails>> request = new HttpEntity<>(salesDetailsList, httpHeaders);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @Order(3)
    public void testGetSalesByAmount() throws URISyntaxException{
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/getSalesByAmount";
        URI uri = new URI(baseUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        String type = "AREA-PRODUCT";
        ResponseEntity<String> result = restTemplate.withBasicAuth("raghavay", "password").getForEntity(uri,String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @Order(4)
    public void testGetSalesDetailInfo() throws URISyntaxException{
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/getSalesDetailsByType?type=AREA-PRODUCT";
        URI uri = new URI(baseUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<String> result = restTemplate.withBasicAuth("raghavay", "password").getForEntity(uri,String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

}
