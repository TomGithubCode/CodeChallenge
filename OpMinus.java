
public class OpMinus implements Operator {

	@Override
	public float eval(float left, float right) {
		return left - right;
	}

	@Override
	public String toString() {
		return "-";
	}

}
