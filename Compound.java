
public class Compound implements Val {

	Operator operator;
	Val left; // left operand
	Val right; // right operand

	public Compound(Operator operator, Val left, Val right) {
		super();
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	@Override
	public float eval() {
		// Recursive evaluation
		float lv = left.eval();
		float rv = right.eval();

		return operator.eval(lv, rv);
	}

	@Override
	public String toString() {
		return "(" + left.toString() + " " + operator.toString() + " " + right.toString() + ")";
	}

}
