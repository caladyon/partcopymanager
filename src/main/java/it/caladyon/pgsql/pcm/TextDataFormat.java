/**
 *
 */
package it.caladyon.pgsql.pcm;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;

/**
 * Instances of this class are not safe for use by multiple concurrent threads,
 * since internally it uses one single {@link CharsetEncoder}.
 * <p>
 * TODO: StringBuilder and CharBuffer as fields, for performance
 *
 * @author Luciano Boschi
 * @since 04/mag/2015
 *
 */
public class TextDataFormat implements DataFormat {

	public static final char[] DEFAULT_NULL = {'\\', 'N'};

	public static final char DEFAULT_DELIMITER = '\t';

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private char[] nullv = DEFAULT_NULL;

	private char delimiter = DEFAULT_DELIMITER;

//	private Charset charset = DEFAULT_CHARSET;
	private CharsetEncoder encoder = DEFAULT_CHARSET.newEncoder();

	/**
	 * @return the nullv
	 */
	public final char[] getNullv() {
		return nullv;
	}

	/**
	 * @param nullv the nullv to set
	 */
	public final void setNullv(char[] nullv) {
		this.nullv = nullv;
	}

	/**
	 * @return the delimiter
	 */
	public final char getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public final void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the charset
	 */
	public final Charset getCharset() {
//		return charset;
		return encoder.charset();
	}

	/**
	 * @param charset the charset to set
	 */
	public final void setCharset(Charset charset) {
//		this.charset = charset;
		encoder = charset.newEncoder();
	}

	@Override
	public ByteBuffer serialize(List<String> f) throws CharacterCodingException {
		//
		StringBuilder sb = new StringBuilder();
		ByteBuffer rv = null;
		if (f != null) {
			for (String i : f) {
				if (i == null) {
					sb.append(DEFAULT_NULL);
				} else {
					sb.append(i);
				}
				sb.append(DEFAULT_DELIMITER);
			}
			sb.setCharAt(sb.length() - 1, '\n');
			rv = encoder.encode(CharBuffer.wrap(sb));
		}
		return rv;
	}

}
