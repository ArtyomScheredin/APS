package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import ru.scheredin.SMO.dto.Snapshot;
import ru.scheredin.SMO.services.SnapshotService;


public class StepModeController {
    @Inject
    private SnapshotService snapshotService;

    @Path("/snapshot")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGet(@Param(value = "id=", required = true) int id) throws JsonProcessingException {
        if (!snapshotService.isReady()
                || (id < 0)
                || (id > snapshotService.getSnapshots().size())) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        Snapshot snapshot = snapshotService.getSnapshots().get(id);
        String result = new ObjectMapper().writeValueAsString(snapshot); //маппим через джексон,
        // потому one-nio не завезли нормальную работу с json
        return Response.json(result);
    }
}
