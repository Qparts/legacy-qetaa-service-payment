package qetaa.service.payment.helpers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Helper {
	
	
	public static int getRandomInteger(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	public static String getSecuredRandom() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	public static Date addMinutes(Date original, int minutes) {
		return new Date(original.getTime() + (1000L * 60 * minutes));
	}

	public String getDateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
		return sdf.format(date);
	}

	public static String getTokenizationSignature(String language, String orderId) {
		String phrase = AppConstants.PAYFORT_SHA_PHRASE_REQUEST 
				+ "access_code=" + AppConstants.PAYFORT_ACCESS_CODE
				+ "language=" + language 
				+ "merchant_identifier=" + AppConstants.PAYFORT_MERCHANT_ID
				+ "merchant_reference=" + orderId
//				+ "return_url="
				+ "service_command=TOKENIZATION"
				+ AppConstants.PAYFORT_SHA_PHRASE_REQUEST;
		return cypher(phrase);
	}
	
	
	public static boolean matchPayfortResponseSignature(Map<String,Object> map){
		String signature = map.get("signature").toString();
		String phrase = AppConstants.PAYFORT_SHA_PHRASE_RESPONSE
				+"access_code="+map.get("access_code").toString()
				+"card_bin="+map.get("card_bin").toString()
				+"card_holder_name="+map.get("card_holder_name").toString().replace("+", " ")
				+"card_number="+map.get("card_number").toString()
				+"expiry_date="+map.get("expiry_date").toString()
				+"language="+map.get("language").toString()
				+"merchant_identifier="+map.get("merchant_identifier").toString()
				+"merchant_reference="+map.get("merchant_reference").toString()
				+"remember_me="+map.get("remember_me").toString()
				+"response_code="+map.get("response_code").toString()
				+"response_message="+map.get("response_message").toString()
			//	+"return_url="+map.get("return_url").toString().replace("%3A", ":").replace("%2F", "/")
				+"service_command="+map.get("service_command").toString()
				+"status="+map.get("status").toString()
				+"token_name="+map.get("token_name").toString()
				+AppConstants.PAYFORT_SHA_PHRASE_RESPONSE;
		return cypher(phrase).equals(signature);
	}
	
	public static Date getFromDate(int month, int year) {
		Date from = new Date();
		if (month == 12) {
			Calendar cFrom = new GregorianCalendar();
			cFrom.set(year, 0, 1, 0, 0, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			from.setTime(cFrom.getTimeInMillis());
		} else {
			Calendar cFrom = new GregorianCalendar();
			cFrom.set(year, month, 1, 0, 0, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			from.setTime(cFrom.getTimeInMillis());
		}
		return from;
	}
	
	public static Date getToDate(int month, int year) {
		Date to = new Date();
		if (month == 12) {
			Calendar cTo = new GregorianCalendar();
			cTo.set(year, 11, 31, 0, 0, 0);
			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, cTo.getActualMaximum(Calendar.MILLISECOND));
			to.setTime(cTo.getTimeInMillis());
		} else {
			Calendar cTo = new GregorianCalendar();
			cTo.set(year, month, 1, 0, 0, 0);
			cTo.set(Calendar.MILLISECOND, 0);
			cTo.set(Calendar.DAY_OF_MONTH, cTo.getActualMaximum(Calendar.DAY_OF_MONTH));
			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, cTo.getActualMaximum(Calendar.MILLISECOND));
			to.setTime(cTo.getTimeInMillis());
		}
		return to;
	}
	
	public static Integer paymentIntegerFormat(double am){
		return Double.valueOf(am * 100).intValue();
	}
	
	public static Map<String, String> getQueryMap(String query)  
	{  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {  
	        String name = param.split("=")[0];  
	        String value = param.split("=")[1];  
	        map.put(name, value);  
	    }  
	    return map;  
	}

	public static String cypher(String text){
		try{
		String shaval = "";
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");

		byte[] defaultBytes = text.getBytes();

		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		shaval = hexString.toString();

		return shaval;
		}
		catch(NoSuchAlgorithmException ex){
			return text;
		}
	}
	
	

}