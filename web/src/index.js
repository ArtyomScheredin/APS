import React, {useState} from 'react';
import ReactDOM from 'react-dom/client';
import Main from "./components/Main";
import './scss/style.scss'
import Table from "./components/Table";
import SnapshotPage from "./components/pages/SnapshotPage";
import StartRoundPage from "./components/pages/StartRoundPage";
import Info from "./components/pages/Info";
import Stub from "./components/Stub";
import {MAIN_URL} from "./components/Constants";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Link
} from "react-router-dom";

/*const Mai1n = () => {
    const [tab, setTab] = useState('Info')


    return <>
        <Main setTab={setTab}/>
        {{
            'StartRoundPage': <StartRoundPage setTab={setTab()}/>,
            'SnapshotPage': <SnapshotPage/>,
            'BuyersPage': {},
            'CouriersPage': <Table/>,
            'Info': <Info/>,
            'Stub': <Stub/>
        }[tab]}
    </>
}*/

// ========================================

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Main/>);

