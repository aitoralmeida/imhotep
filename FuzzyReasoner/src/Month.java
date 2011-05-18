import java.io.Serializable;


public class Month implements Serializable{
	
	private static final long serialVersionUID = -2404227893084489586L;
	
	private final int year;
	private final int month;
	
	public Month(int year, int month) {
		this.year = year;
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + month;
		result = prime * result + year;
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
		Month other = (Month) obj;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "(" + year + "," + month + ")";
	}
}