import React from 'react';
import ReactDOM from 'react-dom/client';
import Request from "./Request";
import './index.css'
import {render} from "react-dom";

class Square extends React.Component {
    render() {
        return (
            <button className="square">
                {/* TODO */}
            </button>
        );
    }
}

class Board extends React.Component {
    renderSquare(i) {
        return <Square/>;
    }

    render() {
        const status = 'Next player: X';

        return (
            <div>
                <div className="status">{status}</div>
                <div className="board-row">
                    {this.renderSquare(0)}
                    {this.renderSquare(1)}
                    {this.renderSquare(2)}
                </div>
                <div className="board-row">
                    {this.renderSquare(3)}
                    {this.renderSquare(4)}
                    {this.renderSquare(5)}
                </div>
                <div className="board-row">
                    {this.renderSquare(6)}
                    {this.renderSquare(7)}
                    {this.renderSquare(8)}
                </div>
            </div>
        );
    }
}

const InputParameter = ({name}) => {
    return (<div className="input-parameter">
            <label for={name}>{name}</label>
            <input type="text" id={name}/>
        </div>
    );
}

const Form = () => {
    const fields = ["buyersNumber",
        "courierNumber",
        "processingTime",
        "bufferCapacity",
        "lambda",
        "duration"]
    let listFields = fields.map((field) => <InputParameter name={field}/>);
    return (
        <form>
            {listFields}
            <input type="submit"/>
        </form>
    )
}

const MainPage = () => {
    return (<div>
        <Form/>
        <a href=""/*TODO*/ className="auto-mode-link">auto mode</a>
        <br/>
        <a href=""/*TODO*/ className="step-mode-link">step mode</a>
    </div>);
}

const Header = () => {
    return (<div className="header">
        <a href=""/*TODO*/ className="main-menu-link">new simulation</a>
    </div>);
}

const SnapShot = {
    []
}

const Snapshot = ({snapshot}) => {
    return (
        <div>
            <p>"{snapshot.time} : {snapshot.message}"</p>
            <
        </div>
    )
}

const StepModePage = () => {
return (
    <div>
        <Header/>
        <Snapshot></Snapshot>
        <button>next</button>
        <button>prev</button>
    </div>
)
}

// ========================================

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Header/>);
