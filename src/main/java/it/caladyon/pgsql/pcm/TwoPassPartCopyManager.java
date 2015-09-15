/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This PartCopyManager avoids that two or more CopyManager could run concurrently,
 * when saving records in multiple partitions.
 * This is done with a simple two-pass strategy:
 * <ol>
 * <li>the given records are sorted in different lists, one for every partition;
 * <li>for every list,
 * {@link BasePartCopyManager#copyInSinglePartition(Connection, Collection, String) copyInSinglePartition}
 * is invoked.
 * </ol>
 *
 * @author Luciano Boschi
 * @param <R>
 * @since 15/mag/2015
 *
 */
public class TwoPassPartCopyManager<R> extends BasePartCopyManager<R> {

	@Override
	public long copyIn(Connection conn, Collection<R> records) throws SQLException, IOException {
		long rv = 0;
		if (records.size() > 0) {
			/* Sorting of records: partition name - list of record. */
			final Map<String, List<R>> sorting = new HashMap<String, List<R>>();

			// sorting
			for (R record : records) {
				String partname = logic.determinePartition(record);
				List<R> list = sorting.get(partname);
				if (list == null) {
					list = new ArrayList<R>();
					sorting.put(partname, list);
				}
				list.add(record);
			}
			// copying
			for (Entry<String, List<R>> erecs : sorting.entrySet()) {
				long tmp = copyInSinglePartition(conn, erecs.getValue(), erecs.getKey());
				if (tmp > 0) {
					rv += tmp;
				}
			}
		} else {
			log.debug("No records, no copy");
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.PartCopyManager#getMapper(java.sql.Connection)
	 */
	@Override
	public PartCopyMapper<R> getMapper(Connection conn) {
		// TODO
		throw new UnsupportedOperationException("Method not implemented!!");
	}

}
