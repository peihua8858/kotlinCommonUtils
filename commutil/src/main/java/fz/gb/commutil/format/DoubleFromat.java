package fz.gb.commutil.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * float 格式化方法
 * @author tp
 *
 */
public class DoubleFromat {
	
	
	public static String fromat(double d,int digit,RoundingMode mode){
		  
	     BigDecimal b = new BigDecimal(d);
	     b.add(new BigDecimal("0.0001"));
	     
	     String pattern ="0.";
	     for (int i = 0; i < digit; i++) {
			pattern +="0";
		}
	     DecimalFormat dd =new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.US));
	     dd.setRoundingMode(mode);
		
		return dd.format(b);
	}
	
	/**
	 * 四舍5入 
	 * @param d
	 * @param digit 位数
	 * @return
	 */
	public static String fromat(double d,int digit){
	     return fromat(d, digit,RoundingMode.FLOOR);
	}

	/**
	 * 四舍5入
	 * @param d
	 * @param digit 位数
	 * @return
	 */
	public static String fromat(float d,int digit){
		return fromat(d, digit,RoundingMode.FLOOR);
	}
	
	

}
