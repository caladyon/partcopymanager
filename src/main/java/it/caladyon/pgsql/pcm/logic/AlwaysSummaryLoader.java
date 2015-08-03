/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

/**
 * Queries the summary at every invocation.
 *
 * @author Luciano Boschi
 * @since 30 lug 2015
 *
 */
public class AlwaysSummaryLoader extends BaseSummaryLoader {

	/**
	 * Calls {@link SummaryQuerier#getPartitions()}
	 * and returns its output without modifications.
	 *
	 * @see it.caladyon.pgsql.pcm.logic.SummaryLoader#getPartitions()
	 */
	@Override
	public List<PartData> getPartitions() {
		return summaryQuerier.getPartitions();
	}

}
