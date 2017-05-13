import java.lang.reflect.Array;
import java.util.Map;
import java.util.TreeMap;

public class MathEval {

	String str;

	// Map operator symbol to precedence value.
	// Higher precedence value =
	// stronger grouping effect around that operator.
	Map<Character, Integer> mapPrecedence = new TreeMap<>();
	Map<Character, Operator> mapOps = new TreeMap<>();

	String[] pieces;
	// Parsed operator sequence
	Character[] ops;
	// Parsed value sequence
	Val[] vals;

	public MathEval(String _str) {

		init();

		// First step: put whitespace around all operators to allow
		// for easier splitting.
		String str = _str;
		for (char c : mapOps.keySet()) {
			str = str.replace("" + c, " " + c + " ");
		}

		pieces = str.split("\\s+"); // split on whitespace
		// Check validity
		boolean ok = true;
		for (int i = 1; ok && i < pieces.length; i += 2) {
			ok = isOperator(pieces[i]);
		}
		for (int i = 0; ok && i < pieces.length; i += 2) {
			ok = isNumber(pieces[i]);
		}
		if (!ok) {
			throw new IllegalArgumentException("Invalid format");
		}

		run();

	}

	public float eval() {
		return vals[0].eval();
	}

	public String getParsed() {
		return vals[0].toString();
	}
	
	private boolean isOperator(String s) {
		if (s.length() > 1) {
			return false;
		} else {
			char c = s.charAt(0);
			return mapOps.containsKey(c);
		}
	}

	private boolean isNumber(String s) {
		try {
			float f = Float.parseFloat(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private void init() {
		// Extend here with more operators if desired

		mapOps.put('+', new OpPlus());
		mapOps.put('-', new OpMinus());
		mapOps.put('*', new OpMult());
		mapOps.put('/', new OpDiv());

		mapPrecedence.put('+', 1);
		mapPrecedence.put('-', 1);
		mapPrecedence.put('*', 2);
		mapPrecedence.put('/', 2);
	}

	private void run() {
		// Initial parsing

		// Get operators
		int j = 0;
		ops = new Character[pieces.length / 2];
		for (int i = 1; i < pieces.length; i += 2) {
			ops[j] = pieces[i].charAt(0);
			j += 1;
		}
		j = 0;
		vals = new Val[ops.length + 1];
		for (int i = 0; i < pieces.length; i += 2) {
			vals[j] = new Atomic(Float.parseFloat(pieces[i]));
			j += 1;
		}

		while (vals.length >= 2) {
			parse();
		}

	}

	private void parse() {
		// Find operator with highest precedence
		int j = 0;
		int prec = -1;
		for (int i = 0; i < ops.length; ++i) {
			int p = mapPrecedence.get(ops[i]);
			if (p >= prec) { // the "=" enforces right-most grouping effect.
								// E.g. 8 / 9 / 3 ==> 8 / (9 / 3)
				j = i;
				prec = p;
			}
		}

		Val left = vals[j];
		Val right = vals[j + 1];
		Compound c = new Compound(mapOps.get(ops[j]), left, right);
		// Shrink arrays
		ops = shrink(Character.class, ops, j);
		vals = shrink(Val.class, vals, j);
		vals[j] = c;
	}

	private static <E> E[] shrink(Class<E> c, E[] array, int j) {
		E[] array2 = (E[]) Array.newInstance(c, array.length - 1);
		for (int i = 0; i < j; ++i) {
			array2[i] = array[i];
		}
		for (int i = j + 1; i < array.length; ++i) {
			array2[i - 1] = array[i];
		}
		return array2;
	}

}
