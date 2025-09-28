package org.spiderflow.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.DataSource;

import java.util.List;

@Mapper
public interface DataSourceMapper {
	
	@Select("select * from sp_datasource")
	List<DataSource> selectList();
	
	@Select("select * from sp_datasource where id = #{id}")
	DataSource selectById(String id);
	
	@Insert("insert into sp_datasource(id, name, driver_class_name, url, username, password, create_date) values(#{id}, #{name}, #{driverClassName}, #{url}, #{username}, #{password}, #{createDate})")
	int insert(DataSource dataSource);
	
	@Update("update sp_datasource set name = #{name}, driver_class_name = #{driverClassName}, url = #{url}, username = #{username}, password = #{password}, create_date = #{createDate} where id = #{id}")
	int update(DataSource dataSource);
	
	@Delete("delete from sp_datasource where id = #{id}")
	int deleteById(String id);
	
	// 分页查询方法
	@Select("select id, name, driver_class_name, create_date from sp_datasource order by create_date desc limit #{offset}, #{limit}")
	List<DataSource> selectPage(int offset, int limit);
	
	// 获取总记录数
	@Select("select count(*) from sp_datasource")
	int selectCount();
}