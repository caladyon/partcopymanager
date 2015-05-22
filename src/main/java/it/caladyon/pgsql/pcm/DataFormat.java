/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.List;

/**
 *
 * TODO: metodo per modificare le opzioni dell'SQL di copia.
 *
 * @author Luciano Boschi
 * @since 04/mag/2015
 *
 */
public interface DataFormat {

	/**
	 *
	 * @param f		List of field values.
	 * @return
	 * @throws CharacterCodingException
	 *
	 * @since 04/mag/2015
	 */
	ByteBuffer serialize(List<String> f) throws CharacterCodingException;

}
