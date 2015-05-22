/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.util.List;

import org.postgresql.copy.CopyManager;

/**
 * Logic of data type management.
 *
 * @param <R>	Class modeling a record.
 *
 * @author Luciano Boschi
 * @since 30/apr/2015
 *
 */
public interface DataLogic<R> {

	/**
	 * Determines the partition to be used for the given record.
	 * The returned name should be fully qualified (i.e. schema.table).
	 *
	 * @param record
	 * @return
	 *
	 * @since 30/apr/2015
	 */
	public String determinePartition(R record);

	/**
	 * Restituisce l'elenco dei campi che devono essere inseriti come campi di un record.
	 * Gli oggetti sono convertiti in stringa, oppure sono null:
	 * le opportune trasformazioni per il {@link CopyManager}
	 * sono effettuate dal {@link DataFormat}
	 * (ad esempio: null in \N, escaping, carattere di separazione campi, eccetera).
	 *
	 * @param record
	 * @return		List of record's fields, or <code>null</code> when then record has not to be saved.
	 *
	 * @since 30/apr/2015
	 */
	public List<String> extractFields(R record);

}
