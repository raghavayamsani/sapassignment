# ASSGINMENT DEMO

#### How to Run and Deploy Application


1. Go to Docker File and build it using this command
 ```
 docker build -t {dockerhubusername}/sapassignment -f Dockerfile . 
 ```
2. Image will be created, If you want to run and validate it. 
```
 docker run -p 8080:8080 {dockerhubysername}/sapassignment
```
3. Once the Docker Image is created push this to Docker Hub.
```
docker push {dockerhubusername}/sapassignment:latest
```
4.Create the Kubernetes Deployment file and deploy it in K8 Cluster.
```
export KUBECONFIG = "Configuration File"
kubectl apply -f deployment.yaml
```
##### How to Create Deployment file
```
kubectl create deployment demo --image={dockerhubname}/sapassignment --dry-run -o=yaml > deployment.yaml

echo --- >> deployment.yaml

kubectl create service clusterip demo --tcp=8080:8080 --dry-run -o=yaml >> deployment.yaml

```

####Services
All the API's which are exposed are authenticated with the SpringBoot Authentication.

Below are multiple services in this application.

```
1. /ingest/salesDetails
2. /getSalesByAmount
3. /getSalesDetailsByType
4. /getStatsOnSales
```

######NOTE:- For all the API's Basic Authentication is Mandatory.

##### /ingest/salesDetails
This is the POST Method where we can ingest the Sales Transactions.

Each Transaction will consist of salesRepId,dealAmount,areaCode,productId.

1. salesRepId is the Integer.
2. dealAmount is the Double.
3. areaCode is the Integer.
4. productId is the Integer.

```
URL: /ingest/salesDetails
METHOD: POST
Pass the Username and Password as the Authorization.
Sample JSON Input PAYLOAD:

[{
	"dealAmount": 4200,
	"areaCode": 900146,
	"salesRepId": 600198,
	"productId": 5025
}, {
	"dealAmount": 8800,
	"areaCode": 900167,
	"salesRepId": 600198,
	"productId": 5011
}]

output:

{
    "data": "Ingested Data Successfully",
    "status": "success",
    "code": 40001,
    "message": "Ingested Data Successfully"
}

```

```
URL: /getSalesByAmount?type={RequestParam}
METHOD: GET
RequestParam: This will accept the multiple params:- AREA,PRODUCT,SALESREP. 
Request Parameter is not Mandatory, if the Request Parameter is not submitted by default it 
considers the AREA 
Output:
{
    "data": {
        "900109": 62200.0,
        "900192": 69800.0,
        "900195": 74800.0,
        "900194": 74200.0
    },
    "status": "success",
    "code": 40002,
    "message": "Data Successfully fetched for AREA"
}
```

```
URL: /getSalesDetailsByType?type={RequestParam}
METHOD: GET
RequestParam: This will accept the multiple params:- AREA_PRODUCT,SALESREP_PRODUCT,PRODUCT_AREA,
 SALESREP_AREA,PRODUCT_SALESREP,AREA_SALESREP
Request Parameter is not Mandatory, if the Request Parameter is not submitted by default it 
considers the AREA_PRODUCT
Output:
{
    "data": {
        "900109": {
            "5025": 6400.0,
            "5012": 12600.0,
            "5030": 7400.0,
            "5031": 9600.0,
            "5033": 4000.0,
            "5035": 8200.0,
            "5022": 4000.0,
            "5038": 10000.0
        },
        "900108": {
            "5024": 23400.0,
            "5026": 9600.0,
            "5027": 9200.0,
            "5011": 6400.0,
            "5013": 8400.0,
            "5030": 7800.0,
            "5014": 9800.0,
            "5033": 8400.0,
            "5018": 5000.0,
            "5035": 5200.0,
            "5020": 6600.0,
            "5039": 23600.0
        }
    },
    "status": "success",
    "code": 40003,
    "message": "Data Successfully fetched for AREA_PRODUCT"
}
```

```
URL: /getStatsOnSales?type={RequestParam}
METHOD: GET
RequestParam: This will accept the multiple params:- AREA,PRODUCT,SALESREP. 
Request Parameter is not Mandatory, if the Request Parameter is not submitted by default it 
considers the AREA 
Output:
{
    "data": [
        {
            "type": "TOP",
            "id": 5028,
            "dealAmount": 324600.0
        },
        {
            "type": "LOW",
            "id": 5039,
            "dealAmount": 176400.0
        }
    ],
    "status": "success",
    "code": 40004,
    "message": "Data Successfully fetched for PRODUCT"
}

```





##### Test Cases
AnalyticsTests is the class where it consists all the test cases.

All the test cases are the basic unit test cases which will check if the API is up and running and also
validates the Basic Authentication and it expects 200 Response code.

If we want we can also validate with the data in future.


#### Notebooks
Python Code is written in a notebook to generate a sample ingestion data.


