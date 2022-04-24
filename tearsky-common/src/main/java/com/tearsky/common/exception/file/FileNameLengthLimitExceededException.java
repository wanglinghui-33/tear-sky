package com.tearsky.common.exception.file;


/**
 * 文件名称超出最大长度异常类
 * @author tearsky
 *
 */
public class FileNameLengthLimitExceededException extends FileException{

	private static final long serialVersionUID = 1L;

	public FileNameLengthLimitExceededException(int defaultFileNameLength) {
		super("upload.filename.exceed.length", new Object[] { defaultFileNameLength });
	}
}
