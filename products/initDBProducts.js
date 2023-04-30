//#run mongo in windows
//mongosh.exe
//show dbs
//use employees
//use products
//load('initDBProducts.js')
db = connect( 'mongodb://localhost/products' );

db.products.drop();
db.categories.drop();

db.createCollection('categories');
db.categories.insertMany( [
   {
      name: 'electronics',
   },
   {
      name: 'sports',
   },
   {
      name: 'furniture',
   },
   {
      name: 'computers',
   },
] );

var electronicsCat = db.categories.find({name: 'electronics'}).toArray()[0];
var sportsCat = db.categories.find({name: 'sports'}).toArray()[0];
var furnitureCat = db.categories.find({name: 'furniture'}).toArray()[0];
var computersCat = db.categories.find({name: 'computers'}).toArray()[0];
var today = new Date();

db.createCollection('products');
db.products.insertMany( [
   {
      name: 'TV Panasonic Screen LCD',
      price: 456.89,
      category: electronicsCat,
      created_at: today
   },
   {
      name: 'Sony Camera HD Digital',
      price: 177.89,
      category: electronicsCat,
      created_at: today
   },
   {
     name: 'Apple iPod',
     price: 46.89,
     category: electronicsCat,
     created_at: today
  },
  {
     name: 'Sony Notebook',
     price: 846.89,
     category: computersCat,
     created_at: today
  },
   {
      name: 'Hewlett Packard Multifuncional',
      price: 200.89,
      category: computersCat,
      created_at: today
   },
   {
      name: 'Bianchi bicycle',
      price: 70.89,
      category: sportsCat,
      created_at: today
   },
   {
     name: 'HP Notebook Omen 17',
     price: 2500.89,
     category: computersCat,
     created_at: today
  },
  {
     name: 'Rodulf Ikea Desk Table',
     price: 150.89,
     category: furnitureCat,
     created_at: today
  },
  {
     name: 'TV Sony Bravia OLED 4K Ultra HD',
     price: 2255.89,
     category: electronicsCat,
     created_at: today
  }
] );
//show collections
//db.categories.find()
//db.products.find()
//exit