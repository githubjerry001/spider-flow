package org.spiderflow.controller;

import org.spiderflow.common.CURDController;
import org.spiderflow.core.mapper.VariableMapper;
import org.spiderflow.core.model.Variable;
import org.spiderflow.core.service.VariableService;
import org.spiderflow.model.JsonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/variable")
public class VariableController extends CURDController<Variable> {
	
	@Autowired
	private VariableService variableService;
	
	@Override
	protected List<Variable> listItems() {
		return variableService.list();
	}
	
	@Override
	protected Variable getItem(String id) {
		return variableService.getById(id);
	}
	
	@Override
	protected boolean saveItem(Variable item) {
		return variableService.saveOrUpdate(item);
	}
	
	@Override
	protected boolean deleteItem(String id) {
		return variableService.removeById(id);
	}
}