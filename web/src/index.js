import React from 'react';
import ReactDOM from 'react-dom/client';
import Request from "./Request";
import Header from "./components/header";
import Form from "./components/main";
import res from "./components/buyers-stats-data.json";
import './scss/style.scss'
import BuyerStatsPage from "./components/buyers-stats";


const MainPage = () => <>
    <Header/>
    <Form/>
</>;

// ========================================

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<BuyerStatsPage/>);
