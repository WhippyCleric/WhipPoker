//J-
package com.whippy.poker.server.webservice;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/")
public class RestApplication extends ResourceConfig {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        public RestApplication() {
                property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
                register(new CDIBinder());
                register(new com.whippy.poker.server.filters.CORSResponseFilter());
                // This must change to specify the root package
                // where the JAX-RS resources and providers should be discovered
                packages("com.whippy.poker.server.resources");
        }

}
//J+
