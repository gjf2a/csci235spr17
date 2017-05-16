package edu.hendrix.modeselection.cluster;

public interface Measurable<T> {
	public long distanceTo(T other);
}
