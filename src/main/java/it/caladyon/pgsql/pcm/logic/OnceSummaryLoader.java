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
public class OnceSummaryLoader extends BaseSummaryLoader {

	private List<PartData> partitions;

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.logic.BaseSummaryLoader#setSummaryQuerier(it.caladyon.pgsql.pcm.logic.SummaryQuerier)
	 */
	@Override
	public void setSummaryQuerier(SummaryQuerier summaryQuerier) {
		super.setSummaryQuerier(summaryQuerier);
		partitions = summaryQuerier.getPartitions();
	}

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.logic.SummaryLoader#getPartitions()
	 */
	@Override
	public List<PartData> getPartitions() {
		return partitions;
	}

}
