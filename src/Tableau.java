
public class Tableau {
	private final boolean flag1;
	private final boolean flag2;

	public Tableau(boolean flag1, boolean flag2) {
		this.flag1 = flag1;
		this.flag2 = flag2;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Tableau)) {
			return false;
		}

		Tableau otherKey = (Tableau) object;
		return this.flag1 == otherKey.flag1 && this.flag2 == otherKey.flag2;
	}

	@Override
	public int hashCode() {
		int result = 17; // any prime number
		result = 31 * result + Boolean.valueOf(this.flag1).hashCode();
		result = 31 * result + Boolean.valueOf(this.flag2).hashCode();
		return result;
	}
}
