# Products with currency exchange calculation
CRUD REST Java API that serves all the product data in dollars and gets all that data from products service and converts its prices using coin-gecko as exchange rate provider.  
This API fetch data from two services: products and coin-gecko. Its serves the data received from products after applying to its prices the currency exchange received from coin-gecko.  

It implemments validation over the request's JSON, has basic http login security with admin user credentials, offers actuators endpoints and uses cloud config for properties management.  
The API responses are ResponseDTO objects with two alternative data inside: the CRUD data or some error messages in case of any error.