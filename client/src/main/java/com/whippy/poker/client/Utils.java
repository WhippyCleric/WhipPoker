//J-
package com.whippy.poker.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;


public class Utils {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        public static String convert(InputStream in) throws IOException {
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, "UTF-8");
                return writer.toString();
        }

}
//J+
