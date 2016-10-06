//J-
package com.whippy.poker.server.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;




@Provider
@PreMatching
public class CORSResponseFilter implements ContainerResponseFilter {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
                requestContext.getHeaders().add("Access-Control-Allow-Origin", "*");
                responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
                responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                requestContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                responseContext.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
                requestContext.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
        }

}
//J+
