package org.spiderflow.common;

import org.spiderflow.model.JsonBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public abstract class CURDController<T> {
	
	protected abstract List<T> listItems();
	
	protected abstract T getItem(String id);
	
	protected abstract boolean saveItem(T item);
	
	protected abstract boolean deleteItem(String id);
	
	@RequestMapping("/list")
	public List<T> list() {
		return listItems();
	}
	
	@RequestMapping("get")
	public JsonBean<T> get(String id) {
		return new JsonBean<T>(getItem(id));
	}
	
	@RequestMapping("delete")
	public JsonBean<Boolean> delete(String id) {
		return new JsonBean<Boolean>(deleteItem(id));
	}
	
	@RequestMapping("save")
	public JsonBean<Boolean> save(T t) {
		return new JsonBean<Boolean>(saveItem(t));
	}
	
}