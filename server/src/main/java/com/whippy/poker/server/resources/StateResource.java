//J-
package com.whippy.poker.server.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whippy.poker.common.beans.ClientState;
import com.whippy.poker.server.orchestrators.GlobalOrchestrator;


@Path("state")
public class StateResource {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        @Inject
        GlobalOrchestrator orchestrator;

        ObjectMapper mapper;

        public StateResource() {
                mapper = new ObjectMapper();
        }

        @GET
        @Consumes({ MediaType.APPLICATION_JSON })
        @Produces({ MediaType.APPLICATION_JSON })
        @Path("/currentState")
        public Response getState(@QueryParam("id") String playerAlias) throws JsonProcessingException {
                ClientState state = orchestrator.getState(playerAlias);
                return Response.ok().entity(mapper.writeValueAsString(state)).build();
        }

}
//J+
