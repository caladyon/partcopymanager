package it.caladyon.pgsql.pcm.logic;

/**
 * DTO.
 * @author Luciano Boschi
 *
 */
public class PartData {

	/** Min timestamp. */
	protected final long mints;

	/** Max timestamp. */
	protected final long maxts;

	/** Partition number. */
	protected final int daynum;

	/**
	 * Contructor.
	 *
	 * @param mints
	 * @param maxts
	 * @param daynum
	 */
	public PartData(long mints, long maxts, int daynum) {
		super();
		this.mints = mints;
		this.maxts = maxts;
		this.daynum = daynum;
	}

}