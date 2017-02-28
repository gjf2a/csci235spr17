package modeselection.util;

public interface Clusterable<T> {
	public T weightedCentroidWith(T other, long thisCount, long otherCount);
}
