//J-
package com.whippy.poker.server.orchestrators;

import com.whippy.poker.server.beans.Table;

public class GlobalOrchestrator {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private Table table;

        public GlobalOrchestrator(Table table) {
                this.table = table;
        }

        public Table getTable() {
                return table;
        }

}
//J+
