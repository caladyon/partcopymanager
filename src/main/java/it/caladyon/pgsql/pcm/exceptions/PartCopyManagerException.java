/**
 *
 */
package it.caladyon.pgsql.pcm.exceptions;

/**
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public class PartCopyManagerException extends IllegalStateException {

	private static final long serialVersionUID = 1L;

	public PartCopyManagerException() {
		super();
	}

	public PartCopyManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public PartCopyManagerException(String s) {
		super(s);
	}

	public PartCopyManagerException(Throwable cause) {
		super(cause);
	}

}
