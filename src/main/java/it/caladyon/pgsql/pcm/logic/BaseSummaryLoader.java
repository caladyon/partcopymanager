/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

/**
 * @author Luciano Boschi
 * @since 30 lug 2015
 *
 */
public abstract class BaseSummaryLoader implements SummaryLoader {

	protected SummaryQuerier summaryQuerier;

	/**
	 * @return the summaryQuerier
	 */
	@Override
	public final SummaryQuerier getSummaryQuerier() {
		return summaryQuerier;
	}

	/**
	 * @param summaryQuerier the summaryQuerier to set
	 */
	@Override
	public void setSummaryQuerier(SummaryQuerier summaryQuerier) {
		this.summaryQuerier = summaryQuerier;
	}

}
