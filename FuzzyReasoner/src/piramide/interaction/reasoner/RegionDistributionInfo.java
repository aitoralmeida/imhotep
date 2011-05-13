package piramide.interaction.reasoner;

public class RegionDistributionInfo {
	private final String name;
	private final double proportion;
	
	public RegionDistributionInfo(String name, double proportion){
		this.name = name;
		this.proportion = proportion;
	}

	public String getName() {
		return name;
	}

	public double getProportion() {
		return proportion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(proportion);
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
		RegionDistributionInfo other = (RegionDistributionInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(proportion) != Double
				.doubleToLongBits(other.proportion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegionDistributionInfo [name=" + name + ", proportion="
				+ proportion + "]";
	}
}
