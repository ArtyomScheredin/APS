package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.dto.BuyerStats;
import ru.scheredin.SMO.dto.CourierStats;

import java.util.List;

public class AutoModeController {
    @Inject
    private AutoModeStatsService autoModeStatsService;

    @Path("/result/couriers")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGetCourier() throws JsonProcessingException {
        if (!autoModeStatsService.isReady()) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        List<CourierStats> courierResults = autoModeStatsService.getCourierResults();
        String result = new ObjectMapper().writeValueAsString(courierResults);
        return Response.json(result);
    }

    @Path("/result/buyers")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGetBuyer() throws JsonProcessingException {
        if (!autoModeStatsService.isReady()) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        List<BuyerStats> buyersResults = autoModeStatsService.getBuyersResults();
        String result = new ObjectMapper().writeValueAsString(buyersResults);
        Response response = new Response(Response.OK, result.getBytes());
        response.addHeader("Content-Type: application/json; charset=utf-8");
        return response;
    }
}
