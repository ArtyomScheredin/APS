export default () =>  <div className="info">
    <h1>Simulating a mass service system</h1>
    <hr/>
    <p>Proud to present my website for simulating mass service system. Provided with capabilities of this web-app
        you would be able to simulate a audiotechnika shop. <br/>
    </p>
    <p> System consists of 5 elements.
        <ul>
            <li>
                <u>producers</u>: buyers/customers of the shop who creates buyers’ requests with Puasson distribution
                time rule.
                You
                can calibrate it by passing lambda parameter
            </li>
            <li><u>Seller</u>: He accepts buyers requests and distributes them over buffer</li>
            <li><u>Buffer</u>: audiotechnika shop internal DB, whose stores all customers requests.
                If buffer is overflown the last inserted request is rejected. Insertion and take occurs with
                circled separated pointers
            </li>
            <li><u>Logistian</u>: An employee who takes requests from buffer and assign them to couriers</li>
            <li><u>Couriers</u>: Fetch demanded stuff to client in constant time which could be specified by processing
                time
                parameter
            </li>
        </ul>
    </p>
</div>