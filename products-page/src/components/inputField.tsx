
export type InputFieldProps = {
    inputValue: any;
    inputType: string;
    inputId : string;
    inputLabel : string;
    onChange: (event:React.ChangeEvent<HTMLInputElement>)=>void;
};

export function InputField(inputFieldProps : InputFieldProps){
    const id : string = 'id'+inputFieldProps.inputId;
    const name : string = 'name'+inputFieldProps.inputId;

    let extras : any = {required:true};
    if (inputFieldProps.inputType==='number') {
        extras = {max:'200000', min:'0', step:'0.001'};
    }
    
    return (
        <div className='form-group'>
            <label htmlFor={name}>{inputFieldProps.inputLabel}</label>
            <input type={inputFieldProps.inputType} value={inputFieldProps.inputValue} 
                className='form-control' id={id} data-testid={id}
                onChange={inputFieldProps.onChange} name={name} {...extras}/>
        </div>
    );
}