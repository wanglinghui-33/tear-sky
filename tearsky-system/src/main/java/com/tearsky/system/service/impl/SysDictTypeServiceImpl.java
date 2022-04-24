package com.tearsky.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tearsky.common.constant.UserConstants;
import com.tearsky.common.core.text.Convert;
import com.tearsky.common.exception.BusinessException;
import com.tearsky.common.utils.StringUtils;
import com.tearsky.system.domain.SysDictType;
import com.tearsky.system.mapper.SysDictDataMapper;
import com.tearsky.system.mapper.SysDictTypeMapper;
import com.tearsky.system.service.ISysDictTypeService;

/**
 * 字典业务层处理
 * @author tearsky
 *
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService{
	
	@Autowired
	private SysDictTypeMapper dictTypeMapper;
	
	@Autowired
	private SysDictDataMapper dictDataMapper;

	/**
	 * 根据条件分页查询字典类型
	 * @param dictType 字典类型信息
	 * @return 字典类型集合信息
	 */
	@Override
	public List<SysDictType> selectDictTypeList(SysDictType dictType) {
		return dictTypeMapper.selectDictTypeList(dictType);
	}

	/**
	 * 校验字典类型名称是否唯一
	 * @param dictType 字典类型
	 * @return 结果
	 */
	@Override
	public String checkDictTypeUnique(SysDictType dictType) {
		Long dictId = StringUtils.isNull(dictType.getDictId()) ? -1L : dictType.getDictId();
		SysDictType sysDictType = dictTypeMapper.checkDictTypeUnique(dictType.getDictType());
		if(StringUtils.isNotNull(sysDictType) && sysDictType.getDictId().longValue() != dictId.longValue()) {
			return UserConstants.DICT_TYPE_NOT_UNIQUE;
		}
		return UserConstants.DICT_TYPE_UNIQUE;
	}

	/**
	 * 新增保存字典类型信息
	 * @param dictType 字典类型信息
	 * @return 结果
	 */
	@Override
	public int insertDictType(SysDictType dictType) {
		return dictTypeMapper.insertDictType(dictType);
	}

	/**
	 * 根据字典类型ID查询信息
	 * @param dictId 字典类型ID
	 * @return 字典类型
	 */
	@Override
	public SysDictType selectDictTypeById(Long dictId) {
		return dictTypeMapper.selectDictTypeById(dictId);
	}

	/**
     * 根据所有字典类型
     * @return 字典类型集合信息
     */
	@Override
	public List<SysDictType> selectDictTypeAll() {
		return dictTypeMapper.selectDictTypeAll();
	}

	/**
     * 修改保存字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
	@Override
	@Transactional
	public int updateDictType(SysDictType dictType) {
		SysDictType oldDict = dictTypeMapper.selectDictTypeById(dictType.getDictId());
		dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
		return dictTypeMapper.updateDictType(dictType);
	}

	/**
     * 	批量删除字典类型
     * @param ids 需要删除的数据
     * @return
     * @throws BusinessException
     */
	@Override
	public int deleteDictTypeByIds(String ids) throws BusinessException {
		Long[] dictIds = Convert.toLongArray(ids);
		for(Long dictId : dictIds) {
			SysDictType dictType = selectDictTypeById(dictId);
			if(dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
				throw new BusinessException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
			}
		}
		return dictTypeMapper.deleteDictTypeByIds(dictIds);
	}

}
