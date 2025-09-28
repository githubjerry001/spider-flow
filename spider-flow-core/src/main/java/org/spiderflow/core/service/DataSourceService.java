package org.spiderflow.core.service;

import org.spiderflow.core.mapper.DataSourceMapper;
import org.spiderflow.core.model.DataSource;
import org.spiderflow.core.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSourceService {
	
	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	public List<DataSource> list() {
		return dataSourceMapper.selectList();
	}
	
	public DataSource getById(String id) {
		return dataSourceMapper.selectById(id);
	}
	
	public boolean saveOrUpdate(DataSource dataSource) {
		if (dataSource.getId() != null && !dataSource.getId().isEmpty()) {
			return dataSourceMapper.update(dataSource) > 0;
		} else {
			return dataSourceMapper.insert(dataSource) > 0;
		}
	}
	
	public boolean removeById(String id) {
		return dataSourceMapper.deleteById(id) > 0;
	}
	
	// 分页查询方法
	public Page<DataSource> page(int current, int size) {
		int offset = (current - 1) * size;
		List<DataSource> records = dataSourceMapper.selectPage(offset, size);
		int total = dataSourceMapper.selectCount();
		return new Page<>(records, total, size, current);
	}
}