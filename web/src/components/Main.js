import React, {useState} from 'react';
import StartRoundPage from "./pages/StartRoundPage";
import SnapshotPage from "./pages/SnapshotPage";
import {Link, Route, BrowserRouter as Router, Routes, useNavigate} from "react-router-dom";
import Table from "./Table";
import Info from "./pages/Info";
import Stub from "./Stub";
import {BUYERS_URL, COURIERS_URL, INFO_URL, SNAPSHOT_URL, START_ROUND_URL} from "./Constants";
import BuyersPage from "./pages/BuyersPage";
import CouriersPage from "./pages/CouriersPage";

const Main = () => {
    const [tab, setTab] = useState("Info")


    return (
        <Router>
            <nav className="header">
                <h1>{tab}</h1>
                {[
                    [START_ROUND_URL, 'new simulation'],
                    [COURIERS_URL, 'couriers'],
                    [BUYERS_URL, 'buyers'],
                    [{pathname: SNAPSHOT_URL, search: '?id=0'}, 'snapshot'],
                    [INFO_URL, 'info'],
                ].map(e => <h4><Link
                    to={e[0]} onClick={() =>
                    setTab(e[1])}>{e[1]}</Link></h4>)
                }
            </nav>
            <Routes>
                <Route path="/start-round" element={<StartRoundPage/>}/>
                <Route path="/couriers" element={<CouriersPage/>}/>
                <Route path="/buyers" element={<BuyersPage/>}/>
                <Route path="/snapshot" element={<SnapshotPage/>}/>
                <Route path="/info" element={<Info/>}/>
                <Route path="*" element={<Stub message={"Sorry, this page is unavailable"}/>}/>
            </Routes>
        </Router>

    );
}
export default Main;