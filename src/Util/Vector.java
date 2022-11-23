package Util;

public class Vector<A, B> {
	private A a;
	private B b;
	
	/**
	 * Constructor
	 * @param a
	 * @param b
	 */
	public Vector(A a, B b) {
		setA(a);
		setB(b);
	}
	
	public Vector(Vector<A, B> vector) {
		this(vector.getA(), vector.getB());
	}
	
	/**
	 * Setters and getters
	 */
	public void setA(A a) {
		this.a = a;
	}
	
	public void setB(B b) {
		this.b = b;
	}
	
	public A getA() {
		return a;
	}
	
	public B getB() {
		return b;
	}
}
