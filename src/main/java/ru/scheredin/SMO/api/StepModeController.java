package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import ru.scheredin.SMO.stats.Snapshot;
import ru.scheredin.SMO.stats.StepModeStatsService;


public class StepModeController {

    @Path("/snapshot")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGet(@Param(value = "id=", required = true) int id) throws JsonProcessingException {
        if (!StepModeStatsService.INSTANCE().isReady()
                || (id < 0)
                || (id > StepModeStatsService.INSTANCE().getSnapshots().size())) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        Snapshot snapshot = StepModeStatsService.INSTANCE().getSnapshots().get(id);
        String result = new ObjectMapper().writeValueAsString(snapshot); //маппим через джексон,
        // потому one-nio не завезли нормальную работу с json
        return Response.json(result);
    }
}
