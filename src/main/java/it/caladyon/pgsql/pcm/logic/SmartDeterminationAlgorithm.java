/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

/**
 * <p>
 * Implements a hopefully fast algorithm to determine the day number for the partition name.
 * <p>
 * This implementation is <b>not thread-safe</b>, due to the presence of {@link #lastIndex}.
 * <p>
 * This strategy does the job of the ancient "GenericDataLogic".
 *
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public class SmartDeterminationAlgorithm extends SimpleDeterminationAlgorithm {

	private int lastIndex = -1;

	/**
	 * Beware: multiple return points.
	 *
	 * @see it.caladyon.pgsql.pcm.logic.SimpleDeterminationAlgorithm#determineDayNum(long, java.util.List)
	 */
	@Override
	public int determineDayNum(long timestamp, List<PartData> partitions) {
		if (lastIndex >= 0) {
			PartData p = partitions.get(lastIndex);
			if (check(timestamp, p)) {
				return p.daynum;
			} else {
				for (int i = lastIndex - 1; i >= 0; --i) {
					p = partitions.get(i);
					if (check(timestamp, p)) {
						lastIndex = i;
						return p.daynum;
					}
				}
				for (int i = lastIndex + 1; i < partitions.size(); ++i) {
					p = partitions.get(i);
					if (check(timestamp, p)) {
						lastIndex = i;
						return p.daynum;
					}
				}
				return -1;
			}
		} else {
			int rv = super.determineDayNum(timestamp, partitions);
			if (rv >= 0) {
				lastIndex = rv;
			}
			return rv;
		}
	}

}
