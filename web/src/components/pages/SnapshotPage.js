import React, {useEffect, useState} from "react";
import Main from "../Main";
import {useParams} from "react-router";
import {MAIN_URL, SNAPSHOT_URL, SNAPSHOT_URL_FULL} from "../Constants";
import {useNavigate, useSearchParams} from "react-router-dom";
import Stub from "../Stub";

const RequestArray = ({requests, title, insertIndex, takeIndex}) => <>
    <div className="array">
        <h4>{title}</h4>
        {requests.map((req, index) => <div className={`request
            ${(req == null) ? ` request_empty` : ``}
            ${(index === insertIndex) ? ` request_insert` : ``}
            ${(index === takeIndex) ? ` request_take` : ``}`}>
            {(req == null) ? `` : req.serial}
        </div>)}
    </div>
</>

const SnapshotPage = () => {
    const [searchParams, setSearchParams] = useSearchParams()
    const [snapshot, setSnapshot] = useState(null)
    const [play, setPlay] = useState(false)
    const [speed, setSpeed] = useState(1000)
    const navigate = useNavigate();


    useEffect(() => {
        fetch(MAIN_URL + SNAPSHOT_URL + "?" + new URLSearchParams({id: searchParams.get("id")}))
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw response
            }).then(snapshot => {
            setSnapshot(snapshot)
        })
            .catch(r => {
                setSnapshot(null)
            })
    }, [searchParams, navigate])

    useEffect(() => {
        let interval
        if (play) {
            interval = setInterval(() => {
                setSearchParams(new URLSearchParams({id: parseInt(searchParams.get("id")) + 1}));
            }, speed);
        } else if (interval != null) {
            clearInterval(interval)
        }
        return () => clearInterval(interval)
    }, [play, searchParams, setSearchParams, speed])


    function timeout(delay) {
        return new Promise(res => setTimeout(res, delay));
    }


    return (<>
        <div className="snapshot">
            <div className="control-panel">
                <button
                    onClick={(e) => {
                        if (isNaN(parseInt(searchParams.get("id")))) {
                            alert("only numbers are allowed")
                            return
                        }
                        setSearchParams(new URLSearchParams({id: parseInt(searchParams.get("id")) - 1}))
                    }
                    }>←
                </button>
                <textarea onChange={(e) => {
                    if (isNaN(+e.target.value)) {
                        alert("only numbers are allowed: " + e.target.value)
                        e.preventDefault()
                        return
                    }
                    setSearchParams(new URLSearchParams({id: e.target.value}))
                }} value={searchParams.get("id")}></textarea>
                <button
                    onClick={(e) => {
                        if (isNaN(parseInt(searchParams.get("id")))) {
                            alert("only numbers are allowed")
                            return
                        }
                        setSearchParams(new URLSearchParams({id: parseInt(searchParams.get("id")) + 1}))
                    }}>→
                </button>
                <button
                    onClick={(e) => {
                        setPlay(!play)
                    }}>{play ? 'stop' : 'play'}
                </button>
                <h1>Speed</h1>
                <textarea id="speed" onChange={(e) => {
                    if (isNaN(+e.target.value)) {
                        alert("only numbers are allowed: " + e.target.value)
                        e.preventDefault()
                        return
                    }
                    setSpeed(e.target.value)
                }} value={speed}></textarea>
            </div>
            {snapshot == null ? <Stub message={"No corresponding snapshots"}/> : <>
                <div className="snapshot__header">
                    <div>
                        <h2>{snapshot.message}</h2>
                        <h3>{"time: " + snapshot.time}</h3>
                    </div>
                    <p className="info">dark blue border -> next place to take
                        <br/>red border -> next place to take</p>
                </div>
                <div className="smo">
                    <RequestArray requests={snapshot.buyers} title="buyers"></RequestArray>
                    <RequestArray requests={snapshot.buffer} title="buffer"
                                  insertIndex={snapshot.nextInsertIndex}
                                  takeIndex={snapshot.nextTakeIndex}
                    ></RequestArray>
                    <RequestArray requests={snapshot.couriers} title="couriers"></RequestArray>
                </div>
            </>}
        </div>
    </>)
        ;
}


export default SnapshotPage;
