CREATE TABLE IF NOT EXISTS sp_flow (
  id VARCHAR(32) NOT NULL,
  name VARCHAR(64) DEFAULT NULL,
  xml CLOB DEFAULT NULL,
  cron VARCHAR(255) DEFAULT NULL,
  enabled CHAR(1) DEFAULT '0',
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_execute_time TIMESTAMP DEFAULT NULL,
  next_execute_time TIMESTAMP DEFAULT NULL,
  execute_count INT DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sp_datasource (
  id VARCHAR(32) NOT NULL,
  name VARCHAR(255) DEFAULT NULL,
  driver_class_name VARCHAR(255) DEFAULT NULL,
  jdbc_url VARCHAR(255) DEFAULT NULL,
  username VARCHAR(64) DEFAULT NULL,
  password VARCHAR(32) DEFAULT NULL,
  create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sp_variable (
  id INT AUTO_INCREMENT,
  name VARCHAR(32) DEFAULT NULL,
  value VARCHAR(512) DEFAULT NULL,
  description VARCHAR(255) DEFAULT NULL,
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sp_task (
  id INT AUTO_INCREMENT,
  flow_id VARCHAR(32) NOT NULL,
  begin_time TIMESTAMP DEFAULT NULL,
  end_time TIMESTAMP DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sp_function (
  id VARCHAR(32) NOT NULL,
  name VARCHAR(255) DEFAULT NULL,
  parameter VARCHAR(512) DEFAULT NULL,
  script CLOB DEFAULT NULL,
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sp_flow_notice (
  id VARCHAR(32) NOT NULL,
  recipients VARCHAR(200) DEFAULT NULL,
  notice_way CHAR(10) DEFAULT NULL,
  start_notice CHAR(1) DEFAULT '0',
  exception_notice CHAR(1) DEFAULT '0',
  end_notice CHAR(1) DEFAULT '0',
  PRIMARY KEY (id)
);

-- 插入初始数据以避免空表问题
INSERT INTO sp_variable (name, value, description) VALUES ('test', 'value', 'Test variable');
INSERT INTO sp_function (id, name, parameter, script) VALUES ('1', 'testFunc', 'param', 'return param;');
