//#run mongo in windows
// ./mongosh.exe
//show dbs
//use employees
//load('initDBEmployees.js')
db = connect( 'mongodb://localhost/employees' );

db.employees.drop();
db.companies.drop();

db.createCollection('companies');
db.companies.insertMany( [
   {
      name: 'ACME',
   },
] );
db.createCollection('employees');
var acmeCom = db.companies.find({name: 'ACME'}).toArray()[0];

db.employees.insertMany( [
   {
      name: 'John',
      last_name: 'Doe',
      company: acmeCom,
      created_at: new Date()
   }
] );
//db.companies.find()
//db.employees.find()
//exit