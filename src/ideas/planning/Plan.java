package ideas.planning;

public class Plan<C extends Enum<C>, M extends Enum<M>> {
	private M action;
	private C result;
	private Plan<C,M> next;
	
	public Plan(M action, C result) {
		this(action, result, null);
	}
	
	public Plan(M action, C result, Plan<C,M> next) {
		this.action = action;
		this.result = result;
		this.next = next;
	}
	
	public boolean contains(C state) {
		Plan<C, M> step = this;
		while (step != null) {
			if (step.result == state) {
				return true;
			} else {
				step = step.next;
			}
		}
		return false;
	}
	
	public M getAction() {return action;}
}
