/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

/**
 * The definition for the logic with which retrieve the partition summary,
 * using a {@link SummaryQuerier}, that holds the query implementation.
 *
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public interface SummaryLoader {

	/**
	 * Somehow returns a list of partitions.
	 *
	 * @return
	 *
	 * @since 30 lug 2015
	 */
	public List<PartData> getPartitions();

	public void setSummaryQuerier(SummaryQuerier summaryQuerier);

	public SummaryQuerier getSummaryQuerier();

}
