package com.tearsky.system.service;

import java.util.List;

import com.tearsky.system.domain.SysDictData;

/**
 * 字典业务层
 * 
 * @author tearsky
 *
 */
public interface ISysDictDataService {

	/**
	 * 根据字典类型查询字典数据
	 * 
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	public List<SysDictData> selectDictDataByType(String dictType);

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 * 
	 * @param dictType  字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	public String selectDictLabel(String dictType, String dictValue);
	
	/**
	 * 根据条件分页查询字典数据
	 * @param dictData 字典数据信息
	 * @return 字典数据集合信息
	 */
	public List<SysDictData> selectDictDataList(SysDictData dictData);
	
	/**
	 * 新增保存字典数据信息
	 * @param dictData 字典数据信息
	 * @return 结果
	 */
	public int insertDictData(SysDictData dictData);
	
	/**
	 * 根据数据字典ID查询信息
	 * @param dictCode 数据字典ID
	 * @return 结果
	 */
	public SysDictData selectDictDataById(Long dictCode);
	
	/**
	 * 修改保存字典数据信息
	 * @param dictData 字典数据信息
	 * @return 结果
	 */
	public int updateDictData(SysDictData dictData);
	
	/**
	 * 批量删除字典数据
	 * @param ids 需要删除的数据
	 * @return 结果
	 */
	public int deleteDictDataByIds(String ids);

}
