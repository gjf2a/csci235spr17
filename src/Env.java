
public class Env {
	public static void main(String[] args) {
		for (String arg: args) {
			String value = System.getenv(arg);
			System.out.println(arg + ": " + value);
		}
	}
}
