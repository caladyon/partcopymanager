/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

import it.caladyon.pgsql.pcm.DataLogic;

/**
 * Strategy for {@link DataLogic#determinePartition(Object)}.
 *
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public interface DeterminationAlgorithm {

	/**
	 *
	 * @param timestamp
	 * @param partitions		This is passed to avoid race conditions.
	 * @return					The "daynum" or -1 when not found.
	 *
	 * @since 29 lug 2015
	 */
	public abstract int determineDayNum(long timestamp, List<PartData> partitions);

}
