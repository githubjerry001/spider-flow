package org.spiderflow.core.service;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.core.mapper.FunctionMapper;
import org.spiderflow.core.model.Function;
import org.spiderflow.core.model.Page;
import org.spiderflow.core.script.ScriptManager;
import org.spiderflow.executor.FunctionExecutor;
import org.spiderflow.executor.FunctionExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;

@Service
public class FunctionService {

    private static Logger logger = LoggerFactory.getLogger(FunctionService.class);

    @Autowired
    private FunctionMapper functionMapper;
    
    @Autowired
    private List<FunctionExtension> functionExtensions;

    /**
     * 初始化/重置自定义函数
     */
    @PostConstruct
    private void init(){
        try {
            ScriptManager.lock();
            ScriptManager.clearFunctions();
            ScriptEngine engine = ScriptManager.createEngine();
            functionMapper.selectList().forEach(function -> {
                ScriptManager.registerFunction(engine,function.getName(),function.getParameter(),function.getScript());
            });
            ScriptManager.setScriptEngine(engine);
        } finally {
            ScriptManager.unlock();
        }
    }

    public String saveFunction(Function entity) {
        try {
            ScriptManager.validScript(entity.getName(),entity.getParameter(),entity.getScript());
            if (entity.getId() != null && !entity.getId().isEmpty()) {
                functionMapper.update(entity);
            } else {
                functionMapper.insert(entity);
            }
            init();
            return null;
        } catch (Exception e) {
            logger.error("保存自定义函数出错",e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    public boolean removeById(Serializable id) {
        boolean ret = functionMapper.deleteById((String) id) > 0;
        init();
        return ret;
    }
    
    public Function getById(String id) {
        return functionMapper.selectById(id);
    }
    
    // 分页查询方法
    public Page<Function> page(int current, int size, String name) {
        int offset = (current - 1) * size;
        List<Function> records;
        int total;
        
        if (name != null && !name.isEmpty()) {
            records = functionMapper.selectPageByName(name, offset, size);
            total = functionMapper.selectCountByName(name);
        } else {
            records = functionMapper.selectPage(offset, size);
            total = functionMapper.selectCount();
        }
        
        return new Page<>(records, total, size, current);
    }
}