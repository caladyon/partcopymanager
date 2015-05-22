/**
 *
 */
package it.caladyon.pgsql.pcm;

import it.caladyon.pgsql.pcm.util.CopiesHolder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This strategy determines the partition of every given record:
 * it doesn't work for a number of record above 50 (yes, fifty).
 * Replaced by "TwoPassPartCopyManager".
 *
 * @author Luciano Boschi 87001893
 * @param <R>
 * @since 13/mag/2015
 *
 * @deprecated
 */
@Deprecated
public class EveryPartCopyManager<R> extends AbstrPartCopyManager<R> {


	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.PartCopyManager#copyIn(java.sql.Connection, java.util.Collection)
	 */
	@Override
	public long copyIn(Connection conn, Collection<R> records) throws SQLException, IOException {
		long rv = 0;
		if (records.size() > 0) {

			CopiesHolder holder = new CopiesHolder();
			try {
				for (R record : records) {
					String partname = logic.determinePartition(record);

					List<String> f = logic.extractFields(record);
					ByteBuffer bb = format.serialize(f);
					holder.getStreamForPartition(conn, partname).write(bb.array(), bb.position(), bb.limit());
				}
			} finally {
				holder.closeAllStreams();
			}

			Map<String, Long> map = holder.joinAndCountAllThreads();
			for (Entry<String, Long> entry : map.entrySet()) {
				if (entry.getValue() < 0) {
					log.warn("Unknown");
				} else {
					rv += entry.getValue();
				}
			}
			CopiesHolder.logResults(log, map);
		} else {
			log.debug("No records, no copy");
		}
		return rv;
	}

}
