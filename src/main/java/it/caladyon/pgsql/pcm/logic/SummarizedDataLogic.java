/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import it.caladyon.pgsql.pcm.DataLogic;

/**
 * DataLogic based on a partition summary (a table whose records describe the partitions).
 *
 * @see SummaryQuerier
 * @see DeterminationAlgorithm
 *
 * @author Luciano Boschi 87001893
 * @param <R>
 * @since 29 lug 2015
 *
 */
abstract public class SummarizedDataLogic<R> implements DataLogic<R> {

	protected DeterminationAlgorithm determinationAlgorithm;

	protected SummaryLoader summaryLoader;

	/**
	 * Sets the algorithm used by {@link #determinePartition(Object)}.
	 *
	 * @param determinationAlgorithm		the determinationAlgorithm to set
	 *
	 * @since 29 lug 2015
	 */
	public final void setDeterminationAlgorithm(DeterminationAlgorithm determinationAlgorithm) {
		this.determinationAlgorithm = determinationAlgorithm;
	}

	/**
	 * @param summaryLoader the summaryLoader to set
	 */
	public final void setSummaryLoader(SummaryLoader summaryLoader) {
		this.summaryLoader = summaryLoader;
	}


}
