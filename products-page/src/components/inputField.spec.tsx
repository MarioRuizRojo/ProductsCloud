import { RenderResult, fireEvent, render } from "@testing-library/react";
import { InputField } from "./inputField";
import { Subject } from "rxjs";

let renderMocked : RenderResult;
let mockOnChange : jest.Mock;
let $changeListener: Subject<any>;

beforeEach(function():void{    
    $changeListener = new Subject<any>();
    mockOnChange = jest.fn((event:any)=>{
        $changeListener.next({name:event.target.name, value:event.target.value});
    });    
});


it('price inputfield ok', function(done):void{
    renderMocked = render(
        <InputField inputId="Price" inputLabel="Price" 
        inputType="number" inputValue={1.2} onChange={mockOnChange}/>
    );
    $changeListener.subscribe((data)=>{
        expect(data.name).toBe('namePrice');
        expect(data.value).toBe('2.3');
        done();
    });
    const priceInput : HTMLElement =  renderMocked.getByTestId('idPrice');
    fireEvent.change(priceInput, {target: {value: 2.3}});    
    expect(renderMocked).toMatchSnapshot();
});

it('name inputfield ok', function(done):void{
    renderMocked = render(
        <InputField inputId="Name" inputLabel="Name" 
        inputType="text" inputValue={'product name'} onChange={mockOnChange}/>
    );
    const newName : string = 'new product name';
    $changeListener.subscribe((data)=>{
        expect(data.name).toBe('nameName');
        expect(data.value).toBe(newName);
        done();
    });
    const nameInput : HTMLElement =  renderMocked.getByTestId('idName');
    fireEvent.change(nameInput, {target: {value: newName}});
    expect(renderMocked).toMatchSnapshot();
});