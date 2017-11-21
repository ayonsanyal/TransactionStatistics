package com.ayon.api.stats.domain;

public class TransactionStatistics {

	private double sum;
	private double min;
	private double max;
	private double avg;
	@Override
	public String toString() {
		return "TransactionStatistics [sum=" + sum + ", min=" + min + ", max=" + max + ", avg=" + avg + ", count="
				+ count + "]";
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(sum);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(min);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(max);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(avg);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (int) (count ^ (count >>> 32));
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
		TransactionStatistics other = (TransactionStatistics) obj;
		if (Double.doubleToLongBits(avg) != Double.doubleToLongBits(other.avg))
			return false;
		if (count != other.count)
			return false;
		if (Double.doubleToLongBits(max) != Double.doubleToLongBits(other.max))
			return false;
		if (Double.doubleToLongBits(min) != Double.doubleToLongBits(other.min))
			return false;
		if (Double.doubleToLongBits(sum) != Double.doubleToLongBits(other.sum))
			return false;
		return true;
	}
	public TransactionStatistics() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TransactionStatistics(double sum, double min, double max, double avg, long count) {
		super();
		this.sum = sum;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.count = count;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	private long count;
	
	
	public void updateStatistics(TransactionStatistics updatableTransaction){
		this.sum = updatableTransaction.sum;
		this.avg = updatableTransaction.avg;
		this.count = updatableTransaction.count;
		this.max = updatableTransaction.max;
		this.min = updatableTransaction.min;
}
	
}
