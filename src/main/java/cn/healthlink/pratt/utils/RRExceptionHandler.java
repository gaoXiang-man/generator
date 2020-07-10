
package cn.healthlink.pratt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常处理器
 *
 */
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	public R handleRRException(RRException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}
}
