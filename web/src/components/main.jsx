const requiredRoundParams = ["buyersNumber",
    "courierNumber",
    "processingTime",
    "bufferCapacity",
    "lambda",
    "duration"]

const InputField = ({name}) => {
    return (<>
        <label htmlFor={name}>{name}</label>
        <input type="text" id={name}></input>
    </>);
}

const Form = () => {
    return (<form>
        {requiredRoundParams.map((el) => {
            return <InputField name={el}/>
        })}
        <input type="submit" id="submit_btn"/>
    </form>);
}

export default Form;