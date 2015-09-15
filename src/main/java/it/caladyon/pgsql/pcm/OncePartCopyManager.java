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

import org.apache.commons.logging.Log;

import it.caladyon.pgsql.pcm.util.CopyInThread;

/**
 * This strategy determines the partition just once, for the first record,
 * thinking that all the records will fit in the same partition.
 *
 * @author Luciano Boschi 87001893
 * @param <R>	Data record.
 * @since 30/apr/2015
 *
 */
public class OncePartCopyManager<R> extends BasePartCopyManager<R> {

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

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.PartCopyManager#getMapper(java.sql.Connection)
	 */
	@Override
	public PartCopyMapper<R> getMapper(Connection conn) {
		return new OncePartCopyMapper<R>(conn, logic, format, log);
	}

}

/**
 *
 * @author Luciano Boschi 87001893
 * @since 15 set 2015
 *
 * @param <R>
 */
class OncePartCopyMapper<R> implements PartCopyMapper<R> {

	private final Connection conn;
	private final DataLogic<R> logic;
	private final DataFormat format;
	private final Log log;

	private long count;

	//final PipedInputStream is;
	private CopyInThread t = null;
	private OutputStream os = null;

	/**
	 * Puo' essere istanziato solo dal proprio PartCopyManager.
	 *
	 * @param conn
	 * @param logic
	 * @param format
	 *
	 */
	protected OncePartCopyMapper(Connection conn, DataLogic<R> logic, DataFormat format, Log log) {
		this.conn = conn;
		this.logic = logic;
		this.format = format;
		this.log = log;

		this.count = 0L;
	}

//	/**
//	 * Costruttore pubblico.
//	 *
//	 * @param conn
//	 * @param logic
//	 * @param format
//	 */
//	public OncePartCopyMapper(Connection conn, DataLogic<R> logic, DataFormat format) {
//		this(conn, logic, format, LogFactory.getLog(OncePartCopyMapper.class));
//	}

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.PartCopyMapper#insert(java.lang.Object)
	 */
	@Override
	public void insert(R record) throws IOException, SQLException {
		if (count == 0) {
			String partname = logic.determinePartition(record);;
			PipedInputStream is = new PipedInputStream();
			t = new CopyInThread(conn, partname, is);
			os = new PipedOutputStream(is);
			t.start();
		}

		++count;
		List<String> f = logic.extractFields(record);
		if (f != null) {
			ByteBuffer bb = format.serialize(f);
			os.write(bb.array(), bb.position(), bb.limit());
		}

	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
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
			count = t.getCount();
		} catch (Exception e) {
			log.warn("Error joining CopyInThread", e);
		}

		if (tmpex instanceof IOException) {
			throw ((IOException)tmpex);
		}
	}

	/**
	 * @return the count
	 */
	@Override
	public final long getCount() {
		return count;
	}

}