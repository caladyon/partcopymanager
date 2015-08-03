package it.caladyon.pgsql.pcm.util;

import it.caladyon.pgsql.pcm.AbstrPartCopyManager;
import it.caladyon.pgsql.pcm.OncePartCopyManager;
import it.caladyon.pgsql.pcm.exceptions.PartCopyManagerException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.copy.CopyManager;

/**
 * Thread executing {@link CopyManager#copyIn(String, InputStream)}.
 *
 * @author Luciano Boschi
 * @since 04/mag/2015
 *
 */
public class CopyInThread extends Thread {

	/** Logger. */
	private final Log log = LogFactory.getLog(getClass());

	private final CopyManager cm;
	private final String sql;
	private InputStream is;

	/** Copy-in return value. */
	private long count = -1L;

	/**
	 * Constructor.
	 *
	 * @param conn			SQL connection.
	 * @param partname		Partition name.
	 * @param is			Stream of the input data.
	 *
	 * @throws SQLException
	 */
	public CopyInThread(Connection conn, String partname, InputStream is) throws SQLException {
		if (partname == null) {
			throw new PartCopyManagerException("'partname' cannot be null!");
		} else if (is == null) {
			throw new PartCopyManagerException("'is' cannot be null!");
		} else {
			cm = OncePartCopyManager.getCopyManager(conn);
			sql = AbstrPartCopyManager.composeCopySql(partname);
			this.is = is;
			if (log.isDebugEnabled())
				log.debug("Constructor: " + sql + " | " + is);
		}
	}

	/**
	 * @return the count
	 */
	public final long getCount() {
		return count;
	}

	/**
	 * Executes {@link CopyManager#copyIn(String, InputStream)}
	 * reading data from the given {@link InputStream}.
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			count = cm.copyIn(sql, is);
			if (log.isDebugEnabled())
				log.debug("copy-in returned: " + count);
		} catch (SQLException e) {
			log.error("Error during copy-in", e);
		} catch (IOException e) {
			log.error("Error during copy-in", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.warn("Error after copy-in", e);
			}
		}
	}

}