/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * TODO: copyIn(Object, records), in previsione di decoratori (per mybatis) ?
 *
 *
 * @author Luciano Boschi 87001893
 * @param <R>		Record type.
 * @since 30/apr/2015
 *
 */
public interface PartCopyManager<R> {

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(DataLogic<R> logic);

	/**
	 * @return the logic
	 */
	public DataLogic<R> getLogic();

	/**
	 * @param format the format to set
	 */
	public void setFormat(DataFormat format);

	/**
	 * @return the format
	 */
	public DataFormat getFormat();

	public PartCopyMapper<R> getMapper(Connection conn);

	/**
	 * Esegue "copy" dei records forniti usando la connessione data.
	 *
	 * @param conn
	 * @param records
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 *
	 * @since 30/apr/2015
	 */
	public long copyIn(Connection conn, Collection<R> records) throws SQLException, IOException;

}
