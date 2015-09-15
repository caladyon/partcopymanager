/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Getting started:
 * <ul>
 * <li>create and initialize a PartCopyManager;
 * <li>get a PartCopyMapper from PartCopyManager#getMapper;
 * <li>call {@link #insert(Object)} for each record to be inserted;
 * <li>{@link #close()} the PartCopyMapper;
 * <li>optionally, {@link #getCount()} of the inserted records.
 * </ul>
 *
 * A PartCopyMapper must be disposed after use.
 * You could store a PartCopyManager, using it as a factory.
 *
 * @author Luciano Boschi 87001893
 * @param <R>
 * @since 14 set 2015
 *
 */
public interface PartCopyMapper<R> extends Closeable {

	public void insert(R record) throws IOException, SQLException;

	public long getCount();

}
