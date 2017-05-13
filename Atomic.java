
public class Atomic implements Val {

	float v;

	public Atomic(float v) {
		this.v = v;
	}

	@Override
	public float eval() {
		return v;
	}

	@Override
	public String toString() {
		return "" + v;
	}
	
}
