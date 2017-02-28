package ideas.cluster;

public interface DistanceFunc<T> {
	public long distance(T img1, T img2);
	
	default public long square(long value) {
		return value * value;
	}
}
