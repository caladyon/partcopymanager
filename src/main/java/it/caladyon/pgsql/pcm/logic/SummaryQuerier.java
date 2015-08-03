/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

/**
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public interface SummaryQuerier {

	/**
	 * Actually queries the summary.
	 * This method may throw exception (IO, SQL, ...).
	 *
	 * @return		A list of information about the partition.
	 * 				NULL is synonym of error.
	 *
	 * @since 30 lug 2015
	 */
	public List<PartData> getPartitions();

}
