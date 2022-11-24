import {COURIERS_URL, MAIN_URL, SNAPSHOT_URL} from "../Constants";
import Table from "../Table";
import Stub from "../Stub";
import {useEffect, useState} from "react";

const BuyersPage = () => {
    const [result, setResult] = useState(null);

    useEffect(() => {
        fetch(MAIN_URL + COURIERS_URL)
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw response
            }).then(json => {
            setResult(json)
        })
    }, [])


    return <div>{result == null ? <Stub message='Couriers results are unavailable'/>
            : <Table name="Couriers" data={result}/>}</div>
}

export default BuyersPage