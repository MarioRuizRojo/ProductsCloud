import { unstable_HistoryRouter as HistoryRouter, Route, Routes } from "react-router-dom";

import './App.css';
import customHistory from './pages/customHistory';
import { NotFound } from './pages/error/notFound';
import { ProductDashboard } from './pages/main/productDashboard';


function App() {
  return (
    <div className='container mt-5'>
        <HistoryRouter history={customHistory}>
            <Routes>
                <Route path="/" element={<ProductDashboard></ProductDashboard>}>    
                </Route>
                <Route path="/notfound" element={<NotFound></NotFound>}>
                </Route>
            </Routes>
        </HistoryRouter>
    </div>    
  );
}

export default App;
