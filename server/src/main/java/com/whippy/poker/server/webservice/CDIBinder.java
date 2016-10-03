//J-
package com.whippy.poker.server.webservice;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.whippy.poker.server.orchestrators.GlobalOrchestrator;


public class CDIBinder extends AbstractBinder {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Override
        protected void configure() {
                bind(GlobalOrchestrator.class).to(GlobalOrchestrator.class).in(Singleton.class);
        }

}
//J+
