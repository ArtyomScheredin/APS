import React, {useState} from 'react';

const Header = () => {
    const [isReady, setReady] = useState(true)
    return (<div className="header">
        <h1>Header</h1>
        <a href="https://www.google.com/" className="main-menu-link">new simulation</a>
        {
            isReady ? (<>
                <a href="https://www.google.com/" className="main-menu-link">automode results</a>
                <a href="https://www.google.com/" className="main-menu-link">stepmode results</a>
            </>) : (<></>)
        }
    </div>);
}
export default Header;