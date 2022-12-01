package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import one.nio.http.HttpSession;
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

    @Inject
    private ObjectMapper objectMapper;

    @Path("/snapshot")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGet(@Param(value = "id=", required = true) int id, Request request,
                              HttpSession session) throws JsonProcessingException {
        if (!snapshotService.isReady()
                || (id < 0)
                || (id > snapshotService.getSnapshots().size())) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        Snapshot snapshot = snapshotService.getSnapshots().get(id);
        String result = objectMapper.writeValueAsString(snapshot); //маппим через джексон,
        // потому one-nio не завезли нормальную работу с json
        Response response = new Response(Response.OK, result.getBytes());
        response.addHeader("Content-Type: application/json; charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin: *");
        return response;
    }

    @Path("/snapshot/size")
    @RequestMethod(Request.METHOD_GET)
    public Response handleGet(Request request,
                              HttpSession session) throws JsonProcessingException {
        int finalSize = (snapshotService.getSnapshots() == null) ? 0 : snapshotService.getSnapshots().size();
        Response response = new Response(Response.OK, objectMapper.writeValueAsBytes(new Object() {
            public int size = finalSize;
        }));
        response.addHeader("Content-Type: application/json; charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin: *");
        return response;
    }
}
