/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

/**
 * @author Luciano Boschi
 * @since 30 lug 2015
 *
 */
public class AlwaysSummaryLoader extends BaseSummaryLoader {

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.logic.SummaryLoader#getPartitions()
	 */
	@Override
	public List<PartData> getPartitions() {
		return summaryQuerier.getPartitions();
	}

}
