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


@Path("state")
public class StateResource {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        @Inject
        public StateResource() {
        }


        @GET
        @Consumes({ MediaType.APPLICATION_JSON })
        @Produces({ MediaType.APPLICATION_JSON })
        @Path("/currentState")
        public Response getState(@QueryParam("id") String quoteId) {
                return Response.ok().build();
        }

}
//J+
