package org.spiderflow.controller;

import org.apache.commons.lang3.StringUtils;
import org.spiderflow.core.model.Function;
import org.spiderflow.core.model.Page;
import org.spiderflow.core.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/function")
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    @RequestMapping("/list")
    public Page<Function> list(@RequestParam(name = "page",defaultValue = "1")Integer page, @RequestParam(name = "limit",defaultValue = "1")Integer size,String name) {
        return functionService.page(page, size, name);
    }

    @RequestMapping("/save")
    public String save(Function function){
        return functionService.saveFunction(function);
    }

    @RequestMapping("/get")
    public Function get(String id){
        return functionService.getById(id);
    }

    @RequestMapping("/remove")
    public void remove(String id){
        functionService.removeById(id);
    }
}