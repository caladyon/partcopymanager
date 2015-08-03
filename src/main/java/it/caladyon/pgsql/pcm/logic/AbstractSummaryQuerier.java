/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Luciano Boschi 87001893
 * @since 30 lug 2015
 *
 */
public abstract class AbstractSummaryQuerier implements SummaryQuerier {

	/** Logger. */
	protected final Log log = LogFactory.getLog(getClass());

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.logic.SummaryQuerier#getPartitions()
	 */
	@Override
	public final List<PartData> getPartitions() {
		log.debug("Reloading partition summary...");
		List<PartData> rv = readPartitions();
		log.info(debugPartitions(rv));
		return rv;
	}

	/**
	 * This method may throw exception (IO, SQL, ...).
	 *
	 * @return		The same conventions of {@link #getPartitions()} apply.
	 *
	 */
	protected abstract List<PartData> readPartitions();

	protected String debugPartitions(List<PartData> partitions) {
		return "Loaded " + partitions.size() + " partitions";
	}

}
