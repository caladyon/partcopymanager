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
public class TimedSummaryLoader extends BaseSummaryLoader {

	/** Default for {@link #partitionsDelay} (milliseconds). */
	public static final long DEFAULT_PARTITIONS_DELAY = 60 * 60 * 1000;

	/** Timestamp for {@link #loadPartitionTable()}. */
	private long partitionsTimestamp = -1;

	/** (milliseconds). */
	private long partitionsDelay = DEFAULT_PARTITIONS_DELAY;

	private List<PartData> partitions;

	/**
	 * @param partitionsDelay the partitionsDelay to set
	 */
	public final void setPartitionsDelay(long partitionsDelay) {
		this.partitionsDelay = partitionsDelay;
	}

	private synchronized void checkAndLoad() {
		final long currentTimeMillis = System.currentTimeMillis();
		if (partitionsTimestamp == -1 || partitionsTimestamp + partitionsDelay <= currentTimeMillis) {
			partitions = summaryQuerier.getPartitions();
			partitionsTimestamp = currentTimeMillis;
		}
	}

	@Override
	public List<PartData> getPartitions() {
		checkAndLoad();
		return partitions;
	}

}
