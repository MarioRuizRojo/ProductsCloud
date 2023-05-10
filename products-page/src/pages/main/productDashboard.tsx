import { ErrorBoundary } from "../error/errorBoundary";
import { ProductEditorView } from "./productEditorView";
import { ProductListView } from "./productListView";


export function ProductDashboard(){
    return (
    <div>
        <div className='row'>
            <div className='col-lg-8 offset-lg-2'>
                <div className='card'>
                    <div className='card-body'>
                        <h1 className='title'>Products in Dollars App</h1>
                    </div>
                </div>
            </div>
        </div>
        <div className='row mt-4'>
            <ErrorBoundary fallback={<p style={{color:'white'}}>Something went wrong</p>}>
                <div className='col-lg-6'>
                    <ProductEditorView/>
                </div>
                <div className='col-lg-6'>
                    <ProductListView/>
                </div>
            </ErrorBoundary>
        </div>
    </div>
    );
}