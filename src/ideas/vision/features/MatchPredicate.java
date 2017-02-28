package ideas.vision.features;

public interface MatchPredicate<A,B> {
	public boolean test(A a, B b1, B b2);
}
