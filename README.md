
# Product management and currency conversion Website Demo
Microservices system that serves a website for managing products with prices in dollars taking the data from a database  
of products with prices in euros.  
It is written in React with RxJS(frontend) and Spring Boot WebFlux Java(backend).  
All microservices use reactive programming, this offers flexibility in big data load scenarios.  

## The frontend app (as microservice)
It is a React App to manage products, it fetches all the product data in dollars from the first backend microservice.

## The first microservice (products-currency)
It is a CRUD REST Java API that serves product data converted to dollars to the website and uses the second microservice to request that product data in euros from a MongoDB.  

## The second microservice (products)
It is a CRUD REST Java API that serves all the product data in euros to the first microservice and gets all that data from a MongoDB database.  

## Deployment
This Microservices system is intended to be deployed in Azure but for developing porpouses a local deployment is also available.  

## Local Deployment (for developers)
1. Change the server port number in each of these apps: config-server, products and products-currency.
So they use different ports and there isn't any conflict at localhost.  

2. Create a repository called config-repo in Azure Devops, save your PAT with full access and your user name.  

3. Set up all these enviromental variables with your chosen values: CLOUD_CONFIG_CONN_STR, CLOUD_CONFIG_PASSWORD, PERSONAL_ACCESS_TOKEN and ENCRYPT_KEY. Also update spring.cloud.config.server.git.username with your user name.  

4. Copy all jars into one folder then run the config-server microservice  
```Shell
java -jar  configserver-0.0.1-SNAPSHOT.jar  --spring.profiles.active=test
```

5. Change the url of spring.config.import to match your congif-server url, in products and products-currency. Also change the server port value of products app inside config-repo file for profile test  

6. Run the MongoDB scripts (initDBEmployees.js and initDBProducts.js) in your local mongodb. They are inside products folder.  

7. Run the products microservice  
```Shell
java -jar  products-0.0.1-SNAPSHOT.jar  --spring.profiles.active=test
```

8. Run the products-currency microservice  
```Shell
java -jar  products-0.0.1-SNAPSHOT.jar  --spring.profiles.active=test
```

9. Run the products-page frontend app  
Inside products-page folder  
```Shell
npm install
npm start
```

## Azure Deployment
1. Create a repository called config-repo in Azure Devops, save your PAT with full access and your user name.  

2. Create a _Keyvault_ and save these secrets with your personal values in them: cloud-config-conn1, cloud-config-pass1, encrypt-key1 and personal-access-token1. Also update spring.cloud.config.server.git.username with your user name.  

3. Create a _MongoDB_, one _ACR_ and four _Web App for Containers_ to host config-server, products, products-currency and products-page inside them.  

4. Run the MongoDB scripts (initDBEmployees.js and initDBProducts.js) in your mongodb server or cloud account. They are inside products folder.  

5. Upload all the repos in Azure Devops and run the Web Apps.