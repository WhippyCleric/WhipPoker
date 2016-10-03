//J-
package com.whippy.poker.server.resources;

import javax.inject.Inject;
import javax.ws.rs.Path;

import com.whippy.poker.server.orchestrators.GlobalOrchestrator;


@Path("event")
public class ActionResource {


        @Inject
        GlobalOrchestrator orchestrator;

}
//J+
