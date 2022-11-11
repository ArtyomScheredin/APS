package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.dto.Round;

public class MainController {
    @Inject
    OrchestratorService orchestrator;

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
        if (isBadParameters(buyersNumber,
                            courierNumber,
                            processingTime,
                            bufferCapacity,
                            lambda,
                            duration)) {
            return new Response(Response.BAD_REQUEST, Response.EMPTY);
        }
        Round round = new Round(buyersNumber, courierNumber, processingTime, bufferCapacity, lambda, duration);
        orchestrator.runRound(round);
        return Response.ok(Response.EMPTY);
    }

    private static boolean isBadParameters(int buyersNumber,
                                           int courierNumber,
                                           double processingTime,
                                           int bufferCapacity,
                                           double lambda,
                                           double duration) {
        if ((buyersNumber < 0) | (courierNumber < 0) | (processingTime < 0)
                | (bufferCapacity < 0) | (lambda < 0) | (duration < 0)) {
            return true;
        }
        return false;
    }
}



