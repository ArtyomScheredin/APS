package ru.scheredin.SMO.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.inject.Inject;
import one.nio.http.HttpSession;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.RequestMethod;
import one.nio.http.Response;
import one.nio.net.Session;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.dto.Round;

import java.io.IOException;

public class MainController {
    @Inject
    private OrchestratorService orchestrator;
    @Inject
    private ObjectMapper objectMapper;

    @Path("/start-round")
    @RequestMethod(Request.METHOD_PUT)
    public Response handleStartRound(Request request) throws IOException {
        Round round;
        try {
            round = objectMapper.readValue(request.getBody(), Round.class);
        } catch (JsonParseException | MismatchedInputException e) {
            return new Response(Response.BAD_REQUEST, "incorrect body".getBytes());
        }
        orchestrator.runRound(round);
        Response response = Response.ok(Response.EMPTY);
        response.addHeader("Access-Control-Allow-Origin: *");
        response.addHeader("Access-Control-Allow-Methods: GET, OPTIONS, HEAD, PUT, POST");
        return response;
    }

}



