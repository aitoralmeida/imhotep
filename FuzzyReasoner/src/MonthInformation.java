import java.io.Serializable;


public class MonthInformation implements Serializable {
	private static final long serialVersionUID = -2393485714226150793L;
	private final double small;
	private final double normal;
	private final double big;
	
	public MonthInformation(double small, double normal, double big) {
		this.small = small;
		this.normal = normal;
		this.big = big;
	}
	
	public double getSmall() {
		return small;
	}
	public double getNormal() {
		return normal;
	}
	public double getBig() {
		return big;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(big);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(normal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(small);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthInformation other = (MonthInformation) obj;
		if (Double.doubleToLongBits(big) != Double.doubleToLongBits(other.big))
			return false;
		if (Double.doubleToLongBits(normal) != Double
				.doubleToLongBits(other.normal))
			return false;
		if (Double.doubleToLongBits(small) != Double
				.doubleToLongBits(other.small))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MonthInformation [small=" + small + ", normal=" + normal
				+ ", big=" + big + "]";
	}
}