import {useState} from "react"


export const useInput = initialValue => {
    const [value, setValue] = useState(initialValue)

    return {
        value,
        setValue,
        reset: () => setValue(""),
        onChange: event => {
            const newValue = event.target.value;
            if (isNaN(newValue) || newValue < 0) {
                alert("Input parameters should be positive number")
            } else {
                setValue(newValue)
            }
        }

    }
}