import {COURIERS_URL, MAIN_URL} from "../Constants";
import Table from "../Table";
import {useEffect, useState} from "react";
import Stub from "../Stub";

const BuyersPage = () => {
    const [result, setResult] = useState(null);

    useEffect(fetch(MAIN_URL + COURIERS_URL)
        .then(r => {
            if (r.ok) {
                return r.json()
            }
            throw r
        }).then(json => {
            setResult(json)
        }), [])

    return <div>{result == null ? <Stub message='Couriers results are unavailable'/>
            : <Table name='Couriers' data={result}/>}</div>
}

export default BuyersPage