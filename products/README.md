# Products
CRUD REST Java API that serves all the product data in euros and gets all that data from a MongoDB database.  
It connects to two different MongoDB databases for educational purposes only, using products DB as the primary one.  
It implemments validation over the request's JSON and over the database entities too, has basic http login security with admin user credentials, offers actuators endpoints and uses cloud config for properties management.  
The API responses are ResponseDTO objects with two alternative data inside: the CRUD data or some error messages in case of any error.