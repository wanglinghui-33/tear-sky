package com.tearsky.common.exception.file;

/**
 * 文件大小限制异常类
 * @author Administrator
 *
 */
public class FileSizeLimitExceededException extends FileException {

	private static final long serialVersionUID = 1L;
	
	public FileSizeLimitExceededException(long defalutMaxSize) {
		super("upload.exceed.maxSize", new Object[] {defalutMaxSize});
	}

}
