/**
 *
 */
package it.caladyon.pgsql.pcm.util;

import it.caladyon.pgsql.pcm.DataFormat;
import it.caladyon.pgsql.pcm.DataLogic;
import it.caladyon.pgsql.pcm.PartCopyManager;
import it.caladyon.pgsql.pcm.format.TextDataFormat;
import it.caladyon.pgsql.pcm.logic.AlwaysSummaryLoader;
import it.caladyon.pgsql.pcm.logic.BaseSummaryLoader;
import it.caladyon.pgsql.pcm.logic.SimpleDeterminationAlgorithm;
import it.caladyon.pgsql.pcm.logic.SummarizedDataLogic;
import it.caladyon.pgsql.pcm.logic.SummaryLoader;
import it.caladyon.pgsql.pcm.logic.SummaryQuerier;

/**
 * @author Luciano Boschi 87001893
 * @since 13/mag/2015
 *
 */
public class PartCopyUtil {

	public static <R> PartCopyManager<R> complete(PartCopyManager<R> pcm, DataLogic<R> logic) {
		return complete(pcm, logic, new TextDataFormat());
	}

	public static <R> PartCopyManager<R> complete(PartCopyManager<R> pcm, DataLogic<R> logic, DataFormat format) {
		pcm.setLogic(logic);
		pcm.setFormat(format);
		return pcm;
	}

	public static <R> PartCopyManager<R> complete(PartCopyManager<R> pcm, SummarizedDataLogic<R> logic, SummaryLoader sl) {
		logic.setSummaryLoader(sl);
		logic.setDeterminationAlgorithm(new SimpleDeterminationAlgorithm());
		return complete(pcm, logic);
	}

	public static SummaryLoader wrapWithLoader(SummaryQuerier sq) {
		BaseSummaryLoader sl = new AlwaysSummaryLoader();
		sl.setSummaryQuerier(sq);
		return sl;
	}

}
