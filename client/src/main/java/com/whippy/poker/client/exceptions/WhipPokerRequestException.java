//J-
package com.whippy.poker.client.exceptions;

import java.io.IOException;

public class WhipPokerRequestException extends Exception {

	//~ ----------------------------------------------------------------------------------------------------------------
	//~ Static fields/initializers
	//~ ----------------------------------------------------------------------------------------------------------------

	public WhipPokerRequestException(String convert) {
		super(convert);
	}

	public WhipPokerRequestException(IOException e) {
		super(e);
	}

	/**  */
	private static final long serialVersionUID = 1L;

}
//J=