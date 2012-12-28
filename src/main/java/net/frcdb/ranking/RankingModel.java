package net.frcdb.ranking;

import java.util.*;
import net.frcdb.ranking.team.OPRFactor;

/**
 *
 * @author tim
 */
public class RankingModel<T extends Object> {
	
	public static RankingModel TEAM_RANKING = new RankingModel(
			new OPRFactor());
	
	private List<RankingFactor<T>> factors;
	
	public RankingModel() {
		factors = new ArrayList<RankingFactor<T>>();
	}
	
	public RankingModel(RankingFactor<T>... factors) {
		this.factors = Arrays.asList(factors);
	}
	
	public void addFactor(RankingFactor<T> f) {
		factors.add(f);
	}
	
	public void removeFactor(RankingFactor<T> f) {
		factors.remove(f);
	}

	public List<RankingFactor<T>> getFactors() {
		return factors;
	}
	
	public double getScore(T object) {
		double ret = 0;
		
		for (RankingFactor f : factors) {
			ret += f.getScore(f);
		}
		
		return ret;
	}
	
	/**
	 * Sorts the given list based on the current ranking model. The returned
	 * list will be a copy of the original, sorted in descending order by the
	 * calculated score.
	 * @param objs
	 * @return 
	 */
	public List<T> rank(List<T> objs) {
		List<T> ret = new ArrayList<T>();
		ret.addAll(objs);
		
		Collections.sort(ret, scoreComparator);
		
		return ret;
	}
	
	private Comparator<T> scoreComparator = new Comparator<T>() {

		@Override
		public int compare(T a, T b) {
			double aScore = getScore(a);
			double bScore = getScore(b);
			
			if (aScore == bScore) {
				return 0;
			} else if (aScore < bScore) {
				return -1;
			} else { // aScore > bScore
				return 1;
			}
		}
		
	};
	
}
