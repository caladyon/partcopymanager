/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.Date;

/**
 * @author Luciano Boschi 87001893
 * @since 29 lug 2015
 *
 */
public class ProbeRecord {

	private final int id;

	private final Date date;

	private final String value;

	public ProbeRecord(int id, Date date, String value) {
		super();
		this.id = id;
		this.date = date;
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return the date
	 */
	public final Date getDate() {
		return date;
	}

	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

}
