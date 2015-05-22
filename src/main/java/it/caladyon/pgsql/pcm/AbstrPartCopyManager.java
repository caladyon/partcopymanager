/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

/**
 * @author Luciano Boschi 87001893
 * @param <R>
 * @since 30/apr/2015
 *
 */
public abstract class AbstrPartCopyManager<R> implements PartCopyManager<R> {

	/** Logger. */
	protected Log log = LogFactory.getLog(getClass());

	/** Logica. */
	protected DataLogic<R> logic = null;

	/** Format of the data (csv, text, binary). Optional; default: text. */
	protected DataFormat format = null;

	@Override
	public final void setLogic(DataLogic<R> logic) {
		this.logic = logic;
	}

	@Override
	public final DataLogic<R> getLogic() {
		return logic;
	}

	@Override
	public final DataFormat getFormat() {
		return format;
	}

	@Override
	public final void setFormat(DataFormat format) {
		this.format = format;
	}

	/**
	 * Utility method.
	 *
	 * @param conn
	 * @return
	 * @throws SQLException
	 *
	 * @since 30/apr/2015
	 */
	public static CopyManager getCopyManager(Connection conn) throws SQLException {
		Connection pgconn = (Connection) conn.unwrap(PGConnection.class);
		CopyManager cm = ((PGConnection) pgconn).getCopyAPI();
		return cm;
	}

	/**
	 * Utility method.
	 *
	 * @param partname
	 * @return
	 *
	 * @since 04/mag/2015
	 */
	public static String composeCopySql(String partname) {
		return new StringBuilder().append("COPY ").append(partname).append(" FROM STDIN").toString();
	}


}
