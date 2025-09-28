package org.spiderflow.core.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.spiderflow.core.expression.ExpressionGlobalVariables;
import org.spiderflow.core.mapper.VariableMapper;
import org.spiderflow.core.model.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariableService {

	@Autowired
	private VariableMapper variableMapper;

	public boolean removeById(Serializable id) {
		boolean ret = variableMapper.deleteById((Integer) id) > 0;
		this.resetGlobalVariables();
		return  ret;
	}

	public boolean saveOrUpdate(Variable entity) {
		boolean ret = false;
		if (entity.getId() != null) {
			ret = variableMapper.update(entity) > 0;
		} else {
			ret = variableMapper.insert(entity) > 0;
		}
		this.resetGlobalVariables();
		return ret;
	}
	
	public Variable getById(String id) {
		return variableMapper.selectById(Integer.valueOf(id));
	}
	
	public List<Variable> list() {
		return variableMapper.selectList();
	}

	@PostConstruct
	private void resetGlobalVariables(){
		Map<String, String> variables = variableMapper.selectList().stream().collect(Collectors.toMap(Variable::getName, Variable::getValue));
		ExpressionGlobalVariables.reset(variables);
	}
}