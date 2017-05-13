
public class Main {

	public static void main(String[] args) {
		String str = 
				//"4 + 6 -1*5";
				"8 / 9 / 3 + 8 * 2";

		MathEval e = new MathEval(str);

		System.out.println("Parsed expression: " + e.getParsed());
		System.out.println("Result: " + e.eval());

	}

}
