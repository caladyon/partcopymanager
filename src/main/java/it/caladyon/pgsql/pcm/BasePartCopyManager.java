/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import it.caladyon.pgsql.pcm.util.CopyInThread;

/**
 * @author Luciano Boschi 87001893
 * @since 14 set 2015
 *
 * @param <R>
 */
public abstract class BasePartCopyManager<R> extends AbstrPartCopyManager<R> {

	protected long copyInSinglePartition(Connection conn, Collection<R> records, String partname) throws SQLException, IOException {
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
			Exception tmpex = null;
			try {
				os.close();
			} catch (Exception e) {
				tmpex = e;
				log.warn("Error closing OutputStream", e);
			}

			try {
				if (t.isAlive()) {
					t.join();
				}
				rv = t.getCount();
			} catch (Exception e) {
				log.warn("Error waiting copy-in thread (" + partname + ")", e);
				rv = -1;
			}

			if (tmpex instanceof IOException) {
				throw ((IOException)tmpex);
			}
		}
		return rv;
	}

}