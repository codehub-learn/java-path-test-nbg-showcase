package gr.codelearn.rentbnb.domain;

public enum PaymentMethod {
	CASH(0f), ONLINE(0.10f);
	private final float discount;

	PaymentMethod(float discount) {
		this.discount = discount;
	}

	public float getDiscount() {
		return this.discount;
	}
}
