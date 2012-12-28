package net.frcdb.api.game.event.element;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author tim
 */
@Entity
public class OPRStatistics {
	
	private double min;
	private double max;
	private double mean;
	private double variance;
	private double standardDeviation;
	private double sum;

	@Id
	private Long id;
	
	public OPRStatistics() {
	}
	
	public OPRStatistics(SummaryStatistics stats) {
		this.min = stats.getMin();
		this.max = stats.getMax();
		this.mean = stats.getMean();
		this.variance = stats.getVariance();
		this.standardDeviation = stats.getStandardDeviation();
		this.sum = stats.getSum();
	}
	
	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}
	
}
