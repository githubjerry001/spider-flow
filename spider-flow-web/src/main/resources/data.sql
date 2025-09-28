-- 插入初始数据以避免空表问题
INSERT INTO sp_variable (name, `value`, description) VALUES ('test', 'value', 'Test variable');
INSERT INTO sp_function (id, name, parameter, script) VALUES ('1', 'testFunc', 'param', 'return param;');;