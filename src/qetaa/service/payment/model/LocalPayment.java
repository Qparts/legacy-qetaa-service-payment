package qetaa.service.payment.model;

public class LocalPayment {
	
	private char type;//c = credit card, s = sadad
	private long cartId;
	private int paymentIndex;//indicate whether its first payment or second payment
	private double amount;//amount
	private String callback;//callback url
	private String name;//sadad username, or credit card name
	private String number;//ccnumber
	private Integer cvc;//cc cvc
	private Integer month;//cc exp
	private Integer year;//cc year
	
	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public Integer getCvc() {
		return cvc;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getYear() {
		return year;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setCvc(Integer cvc) {
		this.cvc = cvc;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public int getPaymentIndex() {
		return paymentIndex;
	}

	public void setPaymentIndex(int paymentIndex) {
		this.paymentIndex = paymentIndex;
	}

	public String getCallback() {
		return callback;
	}

	public char getType() {
		return type;
	}

	public long getCartId() {
		return cartId;
	}
	
	public double getAmount(){
		return amount;
	}

	public void setType(char type) {
		this.type = type;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	@Override
	public String toString() {
		return "LocalPayment [type=" + type + ", cartId=" + cartId + ", paymentIndex=" + paymentIndex + ", amount="
				+ amount + ", callback=" + callback + ", name=" + name + ", number=" + number + ", cvc=" + cvc
				+ ", month=" + month + ", year=" + year + "]";
	}
	
	
}
