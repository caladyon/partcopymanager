/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * How to use a class derived from {@link ReloaderDataLogic}:
 * <ol>
 * <li>instantiate;
 * <li>configure (invoking setter methods);
 * <li>invoke {@link #postConstruct()}
 * (only if the context doesn't do it automatically via {@link PostConstruct} annotation);
 * <li>pass to a {@link PartCopyManager} instance (via setLogic method).
 * </ol>
 *
 * @author Luciano Boschi
 * @param <R>		Data record.
 *
 * @since 12/mag/2015
 *
 */
public abstract class ReloaderDataLogic<R> implements DataLogic<R> {

	/** Default for {@link #partitionsDelay} (milliseconds). */
	public static final long DEFAULT_PARTITIONS_DELAY = 60 * 60 * 1000;

	/** Default for {@link #partitionsRetryDelay} (milliseconds). */
	public static final long DEFAULT_PARTITIONS_RETRY_DELAY = 5 * 60 * 1000;

	/** Logger. */
	protected final Log log = LogFactory.getLog(getClass());

	/** Timestamp for {@link #loadPartitionTable()}. */
	private long partitionsTimestamp = -1;

	/** (milliseconds). */
	private long partitionsDelay = DEFAULT_PARTITIONS_DELAY;

	/** (milliseconds). */
	private long partitionsRetryDelay = DEFAULT_PARTITIONS_RETRY_DELAY;

	/** Reloader of the partition summary. */
	private Timer timer = null;

	/**
	 * Constructor.
	 *
	 * @param ssf
	 */
	public ReloaderDataLogic() {
		super();
	}

	/**
	 * @param partitionsDelay the partitionsDelay to set
	 */
	public final void setPartitionsDelay(long partitionsDelay) {
		this.partitionsDelay = partitionsDelay;
	}

	/**
	 * @param partitionsRetryDelay the partitionsRetryDelay to set
	 */
	public final void setPartitionsRetryDelay(long partitionsRetryDelay) {
		this.partitionsRetryDelay = partitionsRetryDelay;
	}

	@PostConstruct
	public void postConstruct() {
		log.debug("ReloaderDataLogic: post construct");
		loadPartitionTable();
		startTimer();
	}

	/**
	 *
	 * @since 12/mag/2015
	 */
	protected synchronized void loadPartitionTable() {
		if (partitionsTimestamp == -1 || partitionsTimestamp + partitionsDelay <= System.currentTimeMillis()) {
			log.debug("Reloading partition summary...");
			loadPartitionTable0();
			if (getPartitionsSize() == 0) {
				log.warn("Partition summary is empty!");
				partitionsTimestamp += partitionsRetryDelay;
			} else {
				partitionsTimestamp = System.currentTimeMillis();
				if (log.isInfoEnabled()) {
					log.info(debugPartitions());
				}
			}
		}
	}

	/**
	 *
	 *
	 * @since 20/mag/2015
	 */
	protected String debugPartitions() {
		return "Loaded " + getPartitionsSize() + " partitions";
	}

	protected abstract int getPartitionsSize();

	/**
	 * Queries the database to update the partition summary.
	 * Called by {@link #postConstruct()}.
	 * @since 12/mag/2015
	 */
	abstract protected void loadPartitionTable0();

	/**
	 * Starts the timer.
	 * Called by {@link #postConstruct()}.
	 */
	protected synchronized void startTimer() {
		if (timer == null) {
			timer = new Timer();
			PartitionReloader tr = new PartitionReloader();
			timer.scheduleAtFixedRate(tr, 0, partitionsDelay);
			log.debug("startTimer - Timer started");
		}
	}

	/**
	 * Calls {@link Timer#cancel()}.
	 */
	protected synchronized void cancelTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			log.debug("cancelTimer - Timer canceled");
		}
	}

	/**
	 *
	 * @author Luciano Boschi
	 * @since 12/mag/2015
	 *
	 */
	protected class PartitionReloader extends TimerTask {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			loadPartitionTable();
		}

	}

}
