/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.util.List;


/**
 * <p>
 * Using a generic "partition record type", implements a hopefully fast algorithm to determine the day number
 * for the partition name.
 * {@link #determinePartition(Object)} implementations should use {@link #determineDayNum(long)}.
 * <p>
 * This implementation is <b>not thread-safe</b>, due to the presence of {@link #lastIndex}.
 *
 * @author Luciano Boschi
 * @param <R>
 * @since 14/mag/2015
 *
 */
public abstract class GenericDataLogic<R> extends ReloaderDataLogic<R> {

	/**
	 * DTO.
	 * @author Luciano Boschi
	 * @since 14/mag/2015
	 *
	 */
	protected static class PartData {

		protected final long mints;

		protected final long maxts;

		protected final int daynum;

		public PartData(long mints, long maxts, int daynum) {
			super();
			this.mints = mints;
			this.maxts = maxts;
			this.daynum = daynum;
		}

	}

	protected List<PartData> partitions;

	private int lastIndex = -1;

	/**
	 * Beware: multiple return points.
	 *
	 * @param timestamp
	 * @param partitions		This is passed to avoid race conditions.
	 *
	 * @return		The "daynum" or -1 when not found.
	 *
	 * @since 14/mag/2015
	 */
	protected final int determineDayNum(long timestamp, List<PartData> partitions) {
		if (lastIndex >= 0) {
			PartData p = partitions.get(lastIndex);
			if (check(timestamp, p)) {
				return p.daynum;
			} else {
				for (int i = lastIndex - 1; i >= 0; --i) {
					p = partitions.get(i);
					if (check(timestamp, p)) {
						lastIndex = i;
						return p.daynum;
					}
				}
				for (int i = lastIndex + 1; i < partitions.size(); ++i) {
					p = partitions.get(i);
					if (check(timestamp, p)) {
						lastIndex = i;
						return p.daynum;
					}
				}
				return -1;
			}
		} else {
			for (int i = 0; i < partitions.size(); ++i) {
				PartData p = partitions.get(i);
				if (check(timestamp, p)) {
					lastIndex = i;
					return p.daynum;
				}
			}
			return -1;
		}
	}

	private final boolean check(long timestamp, PartData p) {
		return timestamp >= p.mints && timestamp < p.maxts;
	}

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.ReloaderDataLogic#loadPartitionTable0()
	 */
	@Override
	protected final void loadPartitionTable0() {
		partitions = loadPartitionTable1();
		lastIndex = -1;
	}

	/**
	 * Implementations should query the partition summary table
	 * and transform the records in {@link PartData} objects.
	 *
	 * @return
	 *
	 * @since 14/mag/2015
	 */
	protected abstract List<PartData> loadPartitionTable1();

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.ReloaderDataLogic#getPartitionsSize()
	 */
	@Override
	protected final int getPartitionsSize() {
		return partitions.size();
	}

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.ReloaderDataLogic#debugPartitions()
	 */
	@Override
	protected String debugPartitions() {
		String rv = super.debugPartitions();
		for (PartData p : partitions) {
			rv += ":" + p.daynum;
		}
		return rv;
	}

}
