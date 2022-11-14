import {BUYERS_URL, MAIN_URL} from "../Constants";
import Table from "../Table";
import {useEffect, useState} from "react";
import Stub from "../Stub";

const BuyersPage = () => {
    const [result, setResult] = useState(null);

    useEffect(fetch(MAIN_URL + BUYERS_URL)
        .then(r => {

            if (r.ok) {
                return r.json()
            }
            throw r
        }).then(json => {
            setResult(json)
        }), [])

    return <div>{result == null ? <Stub message='Buyers results are unavailable'/>
            : <Table name='Buyers' data={result}/>}</div>
}

export default BuyersPage