package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import ru.scheredin.SMO.stats.AutoModeStatsService;
import ru.scheredin.SMO.stats.BuyerStats;
import ru.scheredin.SMO.stats.CourierStats;
import ru.scheredin.SMO.stats.Snapshot;
import ru.scheredin.SMO.stats.StepModeStatsService;

import java.util.List;

public class AutoModeController {

    @Path("/result/courier")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGetCourier() throws JsonProcessingException {
        if (!AutoModeStatsService.INSTANCE().isReady()) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        List<CourierStats> courierResults = AutoModeStatsService.INSTANCE().getCourierResults();
        String result = new ObjectMapper().writeValueAsString(courierResults);
        return Response.json(result);
    }

    @Path("/result/buyer")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGetBuyer() throws JsonProcessingException {
        if (!AutoModeStatsService.INSTANCE().isReady()) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        List<BuyerStats> buyersResults = AutoModeStatsService.INSTANCE().getBuyersResults();
        String result = new ObjectMapper().writeValueAsString(buyersResults);
        return Response.json(result);
    }
}
