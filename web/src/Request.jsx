import React from 'react';

const Request = ({request}) => {
    let className
    let contents
    if (request == null) {
        className = "null-request"
        contents = <p></p>
    } else {
        className = "request"
        contents = <p>{request.serial}</p>
    }
    return (
        <div className={className}>
            {contents}
        </div>);
}

export default Request;