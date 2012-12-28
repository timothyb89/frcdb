package net.frcdb.util;

/**
 * Simple immutable data pair
 * @author tim
 */
public class Pair<A, B> {

	private A a;
	private B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

}
