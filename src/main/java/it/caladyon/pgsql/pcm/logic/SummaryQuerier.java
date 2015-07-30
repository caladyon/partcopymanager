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
	 *
	 * @return
	 *
	 * @since 30 lug 2015
	 */
	public List<PartData> getPartitions();

}
