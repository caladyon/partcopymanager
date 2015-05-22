/**
 *
 */
package it.caladyon.pgsql.pcm;

import it.caladyon.pgsql.pcm.util.CopyInThread;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * This strategy determines the partition just once, for the first record,
 * thinking that all the records will fit in the same partition.
 *
 * @author Luciano Boschi 87001893
 * @param <R>	Data record.
 * @since 30/apr/2015
 *
 */
public class OncePartCopyManager<R> extends AbstrPartCopyManager<R> {

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.PartCopyManager#copyIn(java.sql.Connection, java.util.Collection)
	 */
	@Override
	public long copyIn(Connection conn, Collection<R> records) throws SQLException, IOException {
		long rv = 0;
		if (records.size() > 0) {
			String partname = logic.determinePartition(records.iterator().next());
			rv = copyInSinglePartition(conn, records, partname);
		} else {
			log.debug("No records, no copy");
		}
		return rv;
	}

	protected long copyInSinglePartition(Connection conn, Collection<R> records, String partname)
			throws SQLException, IOException {
		long rv = 0;
		PipedInputStream is = new PipedInputStream();
		CopyInThread t = new CopyInThread(conn, partname, is);
		OutputStream os = new PipedOutputStream(is);
		try {
			t.start();
			for (R record : records) {
				List<String> f = logic.extractFields(record);
				if (f != null) {
					ByteBuffer bb = format.serialize(f);
					os.write(bb.array(), bb.position(), bb.limit());
				}
			}
		} finally {
			os.close();
		}
		try {
			t.join();
			rv = t.getCount();
		} catch (InterruptedException e) {
			log.warn("Waiting copy-in thread (" + partname + ")", e);
			rv = -1;
		}
		return rv;
	}

}
