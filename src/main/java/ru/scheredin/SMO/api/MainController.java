package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import ru.scheredin.SMO.Orchestrator;
import ru.scheredin.SMO.Round;

public class MainController {
    @Path("/start-round")
    @RequestMethod(Request.METHOD_POST)
    public Response handleGetBuyer(
            @Param(value = "buyersNumber", required = true) String rawBuyersNumber,
            @Param(value = "courierNumber", required = true) String rawCourierNumber,
            @Param(value = "processingTime", required = true) String rawProcessingTime,
            @Param(value = "processingTime", required = true) String rawBufferCapacity,
            @Param(value = "lambda", required = true) String rawLambda,
            @Param(value = "duration", required = true) String rawDuration
    ) throws JsonProcessingException {
        int buyersNumber;
        int courierNumber;
        double processingTime;
        int bufferCapacity;
        double lambda;
        double duration;
        try {
            buyersNumber = Integer.parseInt(rawBuyersNumber);
            courierNumber = Integer.parseInt(rawCourierNumber);
            processingTime = Double.parseDouble(rawProcessingTime);
            bufferCapacity = Integer.parseInt(rawBufferCapacity);
            lambda = Double.parseDouble(rawLambda);
            duration = Double.parseDouble(rawDuration);
        } catch (NumberFormatException e) {
            return new Response(Response.BAD_REQUEST, Response.EMPTY);
        }
        Round round = new Round(buyersNumber, courierNumber, processingTime, bufferCapacity, lambda, duration);
        Orchestrator.INSTANCE().runRound(round);
        return Response.ok(Response.EMPTY);
    }
}


