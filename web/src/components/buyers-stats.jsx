import res from "./buyers-stats-data.json";
import Header from "./header";

const tableTypes = ["buyer", "rej. probability", "avg. time", "avg. buffer time",
    "avg. processing time", "dispersion buffer time", "dispersion processing time"]

const TableHeader = () => <>
    <caption>
        Results for buyers
    </caption>
    <thead>
    <tr>{tableTypes.map(el => <th>{el}</th>)}</tr>
    </thead>
</>;

const TableContents = ({result}) => <>
    <tbody>
    {res.map(buyer =>
        <tr>
            {buyer.map(e =>
                <td>
                    {e.value()}
                </td>)}
        </tr>)}
    </tbody>
</>

const BuyerStatsPage = () => <>
    <Header/>
    <table>
        <TableHeader/>
        <TableContents result={res}/>
    </table>
</>

export default BuyerStatsPage;
