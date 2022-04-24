package com.tearsky.system.service;

import java.util.List;

import com.tearsky.system.domain.SysDictType;

/**
 * 字典业务层
 * 
 * @author tearsky
 *
 */
public interface ISysDictTypeService {

	/**
	 * 根据条件分页查询字典类型
	 * @param dictType 字典类型信息
	 * @return 字典类型集合信息
	 */
	public List<SysDictType> selectDictTypeList(SysDictType dictType);
	
	/**
	 * 校验字典类型名称是否唯一
	 * @param dictType 字典类型
	 * @return 结果
	 */
	public String checkDictTypeUnique(SysDictType dictType);
	
	/**
	 * 新增保存字典类型信息
	 * @param dictType 字典类型信息
	 * @return 结果
	 */
	public int insertDictType(SysDictType dictType);
	
	/**
	 * 根据字典类型ID查询信息
	 * @param dictId 字典类型ID
	 * @return 字典类型
	 */
	public SysDictType selectDictTypeById(Long dictId);
	
	/**
     * 根据所有字典类型
     * 
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll();
    
    /**
     * 修改保存字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int updateDictType(SysDictType dictType);
    
    /**
     * 	批量删除字典类型
     * @param ids 需要删除的数据
     * @return
     * @throws Exception
     */
    public int deleteDictTypeByIds(String ids) throws Exception;
}
