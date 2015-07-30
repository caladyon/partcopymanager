/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

/**
 *
 * <p>
 * This implementation is thread-safe.
 *
 * @author Luciano Boschi 87001893
 * @since 29 lug 2015
 *
 */
public class SimpleDeterminationAlgorithm implements DeterminationAlgorithm {

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.DeterminationAlgorithm#determineDayNum(long, java.util.List)
	 */
	@Override
	public int determineDayNum(long timestamp, List<PartData> partitions) {
		for (int i = partitions.size() - 1; i >= 0 ; --i) {
			PartData p = partitions.get(i);
			if (check(timestamp, p)) {
				return p.daynum;
			}
		}
		return -1;
	}

	protected final boolean check(long timestamp, PartData p) {
		return timestamp >= p.mints && timestamp < p.maxts;
	}

}
