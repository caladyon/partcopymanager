package it.caladyon.pgsql.pcm.util;

import it.caladyon.pgsql.pcm.EveryPartCopyManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

/**
 * This class manages streams and threads bounded to the used partitions.
 *
 * @see EveryPartCopyManager
 * @author Luciano Boschi
 * @since 13/mag/2015
 *
 */
public class CopiesHolder {

	/**
	 * Copy process DTO.
	 * @author Luciano Boschi 87001893
	 * @since 13/mag/2015
	 */
	private class Copy {
		protected final CopyInThread t;
		protected final OutputStream s;
		protected Copy(CopyInThread t, OutputStream s) {
			super();
			this.t = t;
			this.s = s;
		}
	}

	/** Contains started copy processes. */
	private final Map<String, Copy> processes = new HashMap<String, Copy>();

	/**
	 *
	 * @param conn
	 * @param partname
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 *
	 * @since 13/mag/2015
	 */
	public OutputStream getStreamForPartition(Connection conn, String partname) throws SQLException, IOException {
		Copy rv = processes.get(partname);
		if (rv == null) {
			PipedInputStream is = new PipedInputStream();
			CopyInThread t = new CopyInThread(conn, partname, is);
			PipedOutputStream s = new PipedOutputStream(is);

			rv = new Copy(t, s);
			processes.put(partname, rv);

			t.start();
		}
		return rv.s;
	}

	/**
	 * For every thread, invokes {@link Thread#join()} and {@link CopyInThread#getCount()}.
	 * @return
	 *
	 * @since 13/mag/2015
	 */
	public Map<String, Long> joinAndCountAllThreads() {
		Map<String, Long> rv = new HashMap<String, Long>();
		for (Entry<String, Copy> entry : processes.entrySet()) {
			try {
				entry.getValue().t.join();
				rv.put(entry.getKey(), entry.getValue().t.getCount());
			} catch (InterruptedException e) {
				//log.warn("Wainting copy-in thread (" + entry.getKey() + ")", e);
				rv.put(entry.getKey(), -1L);
			}
		}
		return rv;
	}

	/**
	 * Invokes {@link OutputStream#close()} on all streams.
	 *
	 * @throws IOException
	 *
	 * @since 13/mag/2015
	 */
	public void closeAllStreams() throws IOException {
		for (Entry<String, Copy> entry : processes.entrySet()) {
			entry.getValue().s.close();
		}
	}

	/**
	 * Transforms the {@link #joinAndCountAllThreads()}'s output in a debug row.
	 *
	 * @param log
	 * @param map
	 *
	 * @since 13/mag/2015
	 */
	public static void logResults(Log log, Map<String, Long> map) {
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("Multiple copy manager results:");
			for (Entry<String, Long> entry : map.entrySet()) {
				sb.append('\n').append(entry.getKey()).append(": ").append(entry.getValue());
			}
			log.debug(sb.toString());
		}
	}

}