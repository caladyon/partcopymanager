/**
 *
 */
package it.caladyon.pgsql.pcm.util;

import it.caladyon.pgsql.pcm.DataFormat;
import it.caladyon.pgsql.pcm.PartCopyManager;
import it.caladyon.pgsql.pcm.ReloaderDataLogic;
import it.caladyon.pgsql.pcm.TextDataFormat;

/**
 * @author Luciano Boschi 87001893
 * @since 13/mag/2015
 *
 */
public class PartCopyUtil {

	public static <R> void setAndStart(PartCopyManager<R> pcm, ReloaderDataLogic<R> logic, DataFormat format) {
		pcm.setLogic(logic);
		pcm.setFormat(format);
		logic.postConstruct();
	}

	public static <R> void setAndStart(PartCopyManager<R> pcm, ReloaderDataLogic<R> logic) {
		setAndStart(pcm, logic, new TextDataFormat());
	}

}
