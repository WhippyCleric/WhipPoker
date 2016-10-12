//J-
package com.whippy.poker.ai.utils;

import java.util.Map;

import org.junit.Test;

import com.whippy.poker.ai.utils.beans.FullWinPercentages;
import com.whippy.poker.common.beans.Hand;

public class WinningCalculatorTest {


	@Test
	public void testWinningCalculator(){
		WinningCalculator calc = new WinningCalculator();
		Map<Hand, FullWinPercentages> result = calc.getAllHandsFullWin();
		result.keySet();
	}

}
