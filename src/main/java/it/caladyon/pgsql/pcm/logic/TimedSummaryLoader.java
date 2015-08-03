/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import it.caladyon.pgsql.pcm.exceptions.PartCopyManagerException;

/**
 * Queries the partition summary once every {@link #partitionsDelay} milliseconds.
 * A retry policy has been implemented.
 *
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public class TimedSummaryLoader extends BaseSummaryLoader {

	/** Logger. */
	private final Log log = LogFactory.getLog(getClass());

	/** Default for {@link #partitionsDelay} (milliseconds). */
	public static final long DEFAULT_PARTITIONS_DELAY = 60 * 60 * 1000;

	/** Default for {@link #retryDelay} (milliseconds). */
	public static final long DEFAULT_RETRY_DELAY = 10 * 60 * 1000;

	/** Timestamp for {@link #loadPartitionTable()}. */
	private long partitionsTimestamp = -1;

	/** (milliseconds). */
	private long partitionsDelay = DEFAULT_PARTITIONS_DELAY;

	/** (milliseconds). */
	private long retryDelay = DEFAULT_RETRY_DELAY;

	/** Number of failed {@link #checkAndLoad()}. */
	private int retry = 0;

	/** Cached partition list. */
	private List<PartData> partitions;

	/**
	 * @param partitionsDelay the partitionsDelay to set
	 */
	public final void setPartitionsDelay(long partitionsDelay) {
		this.partitionsDelay = partitionsDelay;
	}

	/**
	 * @param retryDelay the retryDelay to set
	 */
	public final void setRetryDelay(long retryDelay) {
		this.retryDelay = retryDelay;
	}

	/**
	 * This method catches every exception.
	 */
	private synchronized void checkAndLoad() {
		try {
			final long currentTimeMillis = System.currentTimeMillis();
			if (partitionsTimestamp == -1
					|| partitionsTimestamp + partitionsDelay + (retry * retryDelay)<= currentTimeMillis)
			{
				List<PartData> temp = summaryQuerier.getPartitions();
				if (temp != null) {
					partitions = temp;
					partitionsTimestamp = currentTimeMillis;
				} else {
					throw new PartCopyManagerException("SummaryQuerier returned NULL");
				}
			}
		} catch (Exception e) {
			++retry;
			log.warn("Partition summary not changed due to exception during the query (retry=" + retry
					+ "; timestamp=" + partitionsTimestamp + ")", e);
		}
	}

	/**
	 * Calls {@link #checkAndLoad()} before returning {@link #partitions}.
	 */
	@Override
	public List<PartData> getPartitions() {
		checkAndLoad();
		return partitions;
	}

}
