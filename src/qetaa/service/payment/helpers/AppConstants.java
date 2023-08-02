package qetaa.service.payment.helpers;

public final class AppConstants {
	private final static String CUSTOMER_SERVICE = "http://localhost:8080/service-qetaa-customer/rest/";
	private final static String USER_SERVICE = "http://localhost:8080/service-qetaa-user/rest/";
	private final static String VENDOR_SERVICE = "http://localhost:8080/service-qetaa-vendor/rest/";
	private final static String CART_SERVICE = "http://localhost:8080/service-qetaa-cart/rest/";
	
	public final static String CUSTOMER_MATCH_TOKEN = CUSTOMER_SERVICE + "match-token";
	public final static String USER_MATCH_TOKEN = USER_SERVICE + "match-token";
	public final static String VENDOR_MATCH_TOKEN = VENDOR_SERVICE + "match-token";
	
	public final static String PAYFORT_ACCESS_CODE = "BVjF7nObEGjRIIS4kttQ";
	public final static String PAYFORT_MERCHANT_ID = "ZrSdMhsE";
	public final static String PAYFORT_CURRENCY = "SAR";
	public final static String PAYFORT_SHA_PHRASE_REQUEST = "Partspaymentrequest";
	public final static String PAYFORT_SHA_PHRASE_RESPONSE = "partspaymentresponse";
	public final static String PAYFORT_TOKEN_TEST_ENVIRONMENT_URL = "https://sbcheckout.PayFort.com/FortAPI/paymentPage";
	public final static String PAYFORT_PURCHASE_TEST_ENVIRONMENT_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";																	

	
	public final static String MOYASAR_TEST_SECRET_KEY = "sk_test_tYp9wshFiVYYQEgKfqDx6oCGoGib5cRRQPU8gdKC";
	public final static String MOYASAR_TEST_PUBLISHABLE_KEY = "pk_test_SugzkCgR72VgXxQHiYg7wVYSE7pCzYr5REYwVqQr";
	public final static String MOYASAR_LIVE_PUBLISHABLE_KEY = "pk_live_hGjxBboyUBKPghFg8E5q9UPYraJ1ueH7y8QyWPVG";
	public final static String MOYASAR_API_URL = "https://api.moyasar.com/v1/payments";
	
	public final static String POST_LOYALTY_POINTS = CUSTOMER_SERVICE + "loyalty-points/cart";
	public final static String PUT_CONFIRM_WIRE_TRANSFER = CART_SERVICE + "wire-transfer/confirm";
	public final static String PUT_REFUND_CART = CART_SERVICE + "refund-cart";
	
	public static final String SEND_SMS_TO_CUSTOMER = CUSTOMER_SERVICE + "send-sms-to-customer";
	
	public final static String getCompletedCarts(long cartId) {
		return CART_SERVICE + "completed-cart/cart/" + cartId;
	}
	
	public final static String getCourier(int id) {
		return VENDOR_SERVICE + "courier/" +id;
	}

}
