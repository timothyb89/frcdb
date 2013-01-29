package net.frcdb.api.team;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author tim
 */
@Entity
public class TeamStatistics {
	
	private int year;
	
	private double oprMin;
	private double oprMax;
	private double oprMean;
	private double oprSum;
	private double oprVariance;
	private double oprStandardDeviation;
	
	private double oprZMin;
	private double oprZMax;
	private double oprZMean;
	private double oprZSum;
	private double oprZVariance;
	private double oprZStandardDeviation;
	
	// TODO: more here
	@Id
	private Long id;

	public TeamStatistics() {
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getOprMax() {
		return oprMax;
	}

	public void setOprMax(double oprMax) {
		this.oprMax = oprMax;
	}

	public double getOprMean() {
		return oprMean;
	}

	public void setOprMean(double oprMean) {
		this.oprMean = oprMean;
	}

	public double getOprMin() {
		return oprMin;
	}

	public void setOprMin(double oprMin) {
		this.oprMin = oprMin;
	}

	public double getOprSum() {
		return oprSum;
	}

	public void setOprSum(double oprSum) {
		this.oprSum = oprSum;
	}

	public double getOprVariance() {
		return oprVariance;
	}

	public void setOprVariance(double oprVariance) {
		this.oprVariance = oprVariance;
	}

	public double getOprStandardDeviation() {
		return oprStandardDeviation;
	}

	public void setOprStandardDeviation(double oprStandardDeviation) {
		this.oprStandardDeviation = oprStandardDeviation;
	}
	
	public double getOprZMax() {
		return oprZMax;
	}

	public void setOprZMax(double oprZMax) {
		this.oprZMax = oprZMax;
	}

	public double getOprZMean() {
		return oprZMean;
	}

	public void setOprZMean(double oprZMean) {
		this.oprZMean = oprZMean;
	}

	public double getOprZMin() {
		return oprZMin;
	}

	public void setOprZMin(double oprZMin) {
		this.oprZMin = oprZMin;
	}

	public double getOprZSum() {
		return oprZSum;
	}

	public void setOprZSum(double oprZSum) {
		this.oprZSum = oprZSum;
	}

	public double getOprZVariance() {
		return oprZVariance;
	}

	public void setOprZVariance(double oprZVariance) {
		this.oprZVariance = oprZVariance;
	}

	public double getOprZStandardDeviation() {
		return oprZStandardDeviation;
	}

	public void setOprZStandardDeviation(double oprZStandardDeviation) {
		this.oprZStandardDeviation = oprZStandardDeviation;
	}
	
}
