package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**A utility class having
 * common and reusable conversion and evaluation methods.
 * Created by AYON SANYAL on 19-11-2017
 */
public class TimeConversionUtils {

	public static long convertTimeStampToSeconds(long timestamp){
		return 1000 * (timestamp/ 1000);
}

	public static long findTimeElapsedInSeconds(long roundedTimestamp){
		return convertTimeStampToSeconds(System.currentTimeMillis()) - roundedTimestamp;
}
	
	public static double divide(double value1, double value2){
		if (value2 == 0) {
			return 0;
		}
		BigDecimal value1Bd = BigDecimal.valueOf(value1);
		BigDecimal value2Bd = BigDecimal.valueOf(value2);
		return value1Bd.divide(value2Bd,RoundingMode.HALF_DOWN).doubleValue();
}
	
	
}
