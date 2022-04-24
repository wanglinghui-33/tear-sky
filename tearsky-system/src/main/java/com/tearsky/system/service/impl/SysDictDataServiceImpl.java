package com.tearsky.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tearsky.common.core.text.Convert;
import com.tearsky.system.domain.SysDictData;
import com.tearsky.system.mapper.SysDictDataMapper;
import com.tearsky.system.service.ISysDictDataService;

/**
 * 字典业务层处理
 * 
 * @author tearsky
 *
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

	@Autowired
	private SysDictDataMapper dictDataMapper;

	/**
	 * 根据字典类型查询字典数据
	 * 
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	@Override
	public List<SysDictData> selectDictDataByType(String dictType) {
		return dictDataMapper.selectDictDataByType(dictType);
	}

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 * 
	 * @param dictType  字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	@Override
	public String selectDictLabel(String dictType, String dictValue) {
		return dictDataMapper.selectDictLabel(dictType, dictValue);
	}

	/**
	 * 根据条件分页查询字典数据
	 * @param dictData 字典数据信息
	 * @return 字典数据集合信息
	 */
	@Override
	public List<SysDictData> selectDictDataList(SysDictData dictData) {
		return dictDataMapper.selectDictDataList(dictData);
	}

	/**
	 * 新增保存字典数据信息
	 * @param dictData 字典数据信息
	 * @return 结果
	 */
	@Override
	public int insertDictData(SysDictData dictData) {
		return dictDataMapper.insertDictData(dictData);
	}

	/**
	 * 根据数据字典ID查询信息
	 * @param dictCode 数据字典ID
	 * @return 结果
	 */
	@Override
	public SysDictData selectDictDataById(Long dictCode) {
		return dictDataMapper.selectDictDataById(dictCode);
	}

	/**
	 * 修改保存字典数据信息
	 * @param dictData 字典数据信息
	 * @return 结果
	 */
	@Override
	public int updateDictData(SysDictData dictData) {
		return dictDataMapper.updateDictData(dictData);
	}

	@Override
	public int deleteDictDataByIds(String ids) {
		return dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
	}

}
