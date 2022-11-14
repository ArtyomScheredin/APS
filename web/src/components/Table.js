import Main from "./Main";

const TableHeader = ({types, name}) => <>
    <caption>
        {name}
    </caption>
    <thead>
    <tr>{types.map(el => <th>{el}</th>)}</tr>
    </thead>
</>;

const TableContents = ({result}) => <>
    <tbody>
    {result.map(buyer => <tr>{
            Object.entries(buyer).map((el) => <td>{el[1]}</td>)}
        </tr>
    )}
    </tbody>
</>

const Table = ({data, name}) => {
    if (data.length == 0) {
        throw 'empty array passed to render a table'
    }
    let keys = Object.keys(data[0]);
    return (<>
        <table>
            <TableHeader types={keys} name={name}/>
            <TableContents result={data}/>
        </table>
    </>);
}

export default Table;
