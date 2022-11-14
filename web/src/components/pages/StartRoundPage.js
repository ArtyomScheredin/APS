import {useInput} from "../useInput";
import {MAIN_URL, START_ROUND_URL} from "../Constants";
import {useNavigate} from "react-router-dom";

const InputField = ({name, value, onChange}) => {
    return (<>
        <label htmlFor={name}>{name}</label>
        <input type="text" id={name} value={value} onChange={onChange}></input>
    </>);
}

const StartRoundPage = ({setTab}) => {
    const navigate = useNavigate()
    let customHooks = new Map()
    customHooks.set("buyersNumber", useInput(`5`))
    customHooks.set("courierNumber", useInput(`5`))
    customHooks.set("processingTime", useInput(`0.1`))
    customHooks.set("bufferCapacity", useInput(`10`))
    customHooks.set("lambda", useInput(`12`))
    customHooks.set("duration", useInput(`5`))

    const handleSubmit = (evt) => {
        evt.preventDefault()
        for (let customHook of customHooks) {
            if (customHook[1].value === '') {
                alert("You must fill all fields!")
                return
            }
        }
        let result = new Map()
        customHooks.forEach((v, k) => {
            result.set(k, v.value)
            v.reset()
        })
        console.log(JSON.stringify(Object.fromEntries(result)))
        fetch(MAIN_URL + START_ROUND_URL, {
            method: 'PUT',
            body: JSON.stringify(Object.fromEntries(result)),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(r => {
            navigate('/snapshot?id=0')
        })
            .catch(r => {
                alert(`error: ${r}`)
            })

    }
    const requiredRoundParams = []
    for (let key of customHooks.keys()) {
        requiredRoundParams.push(key)
    }

    return (<form autoComplete='off' onSubmit={handleSubmit}>
        {requiredRoundParams.map(el => {
            return <InputField name={el}
                               value={customHooks.get(el).value}
                               onChange={customHooks.get(el).onChange}/>
        })}
        <input type="submit" id="submit_btn"/>
    </form>);
}

export default StartRoundPage;