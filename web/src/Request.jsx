import React from 'react';

const Request = ({request}) => {
    return (
        <div className="request">
        <p>{request.serial}</p>
        </div>);
}

export default Request;