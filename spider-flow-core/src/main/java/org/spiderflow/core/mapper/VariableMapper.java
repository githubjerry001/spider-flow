package org.spiderflow.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.Variable;

import java.util.List;

@Mapper
public interface VariableMapper {
	
	@Select("select * from sp_variable")
	List<Variable> selectList();
	
	@Select("select * from sp_variable where id = #{id}")
	Variable selectById(Integer id);
	
	@Insert("insert into sp_variable(name, value, description, create_date) values(#{name}, #{value}, #{description}, #{createDate})")
	int insert(Variable variable);
	
	@Update("update sp_variable set name = #{name}, value = #{value}, description = #{description}, create_date = #{createDate} where id = #{id}")
	int update(Variable variable);
	
	@Delete("delete from sp_variable where id = #{id}")
	int deleteById(Integer id);

}