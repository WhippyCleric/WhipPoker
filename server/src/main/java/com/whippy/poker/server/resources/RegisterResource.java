//J-
package com.whippy.poker.server.resources;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whippy.poker.common.beans.Player;
import com.whippy.poker.server.orchestrators.GlobalOrchestrator;


@Path("register")
public class RegisterResource {

        @Inject
        GlobalOrchestrator orchestrator;

        ObjectMapper mapper;

        public RegisterResource() {
                mapper = new ObjectMapper();
        }

        @POST
        @Consumes({ MediaType.APPLICATION_JSON })
        @Produces({ MediaType.APPLICATION_JSON })
        @Path("/registerPlayer")
        public Response registerPlayer(String jsonPlayer) throws JsonParseException, JsonMappingException, IOException {
                Player player = mapper.readValue(jsonPlayer, Player.class);
                orchestrator.registerPlayer(player);
                return Response.ok().entity(mapper.writeValueAsString(player)).build();
        }

        @GET
        @Consumes({ MediaType.APPLICATION_JSON })
        @Produces({ MediaType.APPLICATION_JSON })
        @Path("/start")
        public Response start(String jsonPlayer) {
                orchestrator.start();
                return Response.ok().build();
        }


}
//J+
