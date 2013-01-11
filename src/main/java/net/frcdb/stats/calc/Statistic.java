package net.frcdb.stats.calc;

/**
 *
 * @author tim
 */
public interface Statistic {

	/**
	 * Gets the possible names for this statistic. The formal name for this
	 * statistic should be at index zero.
	 * @return an arrays of strings containing possible names for this statistic
	 */
	public String[] getNames();
	
	/**
	 * Gets the name of the backend this statistic should be executed on. If
	 * null, it will be executed on the main application instance.
	 * @return the backend name, or null
	 */
	public String getBackendName();

}
