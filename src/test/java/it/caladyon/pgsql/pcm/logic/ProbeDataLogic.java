/**
 *
 */
package it.caladyon.pgsql.pcm.logic;

import java.util.ArrayList;
import java.util.List;

import it.caladyon.pgsql.pcm.exceptions.PartCopyManagerException;

/**
 * @author Luciano Boschi
 * @since 29 lug 2015
 *
 */
public class ProbeDataLogic extends SummarizedDataLogic<ProbeRecord> {

	@Override
	public String determinePartition(ProbeRecord record) {
		String rv = null;
		List<PartData> partitions = summaryLoader.getPartitions();
		int daynum = determinationAlgorithm.determineDayNum(record.getDate().getTime(), partitions);
		if (daynum >= 0) {
			rv = "table_prefix_" + daynum;
		} else {
			throw new PartCopyManagerException("Record not in known partition summary: " + record);
		}
		return rv;
	}

	@Override
	public List<String> extractFields(ProbeRecord record) {
		List<String> rv = new ArrayList<String>();
		rv.add(String.valueOf(record.getId()));
		rv.add(record.getDate().toString());
		rv.add(record.getValue());
		return rv ;
	}

}
