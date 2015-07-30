/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

/**
 * @author Luciano Boschi
 * @param <R>
 * @since 30 lug 2015
 *
 */
public abstract class BaseDataLogic<R> extends SummarizedDataLogic<R> {

	/* (non-Javadoc)
	 * @see it.caladyon.pgsql.pcm.DataLogic#determinePartition(java.lang.Object)
	 */
	@Override
	public String determinePartition(R record) {
		String rv = null;
		final int daynum = determinationAlgorithm.determineDayNum(extractTimestamp(record), summaryLoader.getPartitions());
		if (daynum >= 0) {
			StringBuilder sb = new StringBuilder();
			appendPieces(sb, record, daynum);
			rv = sb.toString();
		}
		return rv;
	}

	//sb.append(getTablePrefix()).append(daynum).toString();
	abstract protected void appendPieces(StringBuilder sb, R record, int daynum);

	abstract protected long extractTimestamp(R record);

}
