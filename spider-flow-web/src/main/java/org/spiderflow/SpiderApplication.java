package org.spiderflow;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.regex.Pattern;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StreamUtils;

@SpringBootApplication
@EnableScheduling
public class SpiderApplication implements ServletContextInitializer{
	
	public static void main(String[] args) throws IOException {
		// 初始化数据库
		initDatabase();
		
		SpringApplication.run(SpiderApplication.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//设置文本缓存1M
		servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize", Integer.toString((1024 * 1024)));
	}
	
	private static void initDatabase() {
		try {
			// 加载H2驱动
			Class.forName("org.h2.Driver");
			
			// 连接到H2内存数据库（使用与应用程序相同的URL）
			Connection conn = DriverManager.getConnection(
				"jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL", 
				"sa", 
				""
			);
			
			// 读取并执行schema.sql
			ClassPathResource schemaResource = new ClassPathResource("schema.sql");
			String schemaSql = StreamUtils.copyToString(schemaResource.getInputStream(), java.nio.charset.StandardCharsets.UTF_8);
			
			// 清理SQL语句中的特殊字符和注释
			schemaSql = schemaSql.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
			schemaSql = schemaSql.replaceAll("--.*?\\n", "\n"); // 移除单行注释
			
			// 分割并执行SQL语句
			String[] schemaStatements = schemaSql.split(";");
			Statement stmt = conn.createStatement();
			
			for (String statement : schemaStatements) {
				statement = statement.trim();
				if (!statement.isEmpty()) {
					// 进一步清理语句
					statement = statement.replaceAll("[\\r\\n]", " ").trim();
					if (!statement.isEmpty()) {
						System.out.println("Executing schema statement: " + statement);
						stmt.execute(statement);
					}
				}
			}
			
			// 读取并执行data.sql
			ClassPathResource dataResource = new ClassPathResource("data.sql");
			String dataSql = StreamUtils.copyToString(dataResource.getInputStream(), java.nio.charset.StandardCharsets.UTF_8);
			
			// 清理SQL语句中的特殊字符和注释
			dataSql = dataSql.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
			dataSql = dataSql.replaceAll("--.*?\\n", "\n"); // 移除单行注释
			
			// 分割并执行SQL语句
			String[] dataStatements = dataSql.split(";");
			for (String statement : dataStatements) {
				statement = statement.trim();
				if (!statement.isEmpty()) {
					// 进一步清理语句
					statement = statement.replaceAll("[\\r\\n]", " ").trim();
					if (!statement.isEmpty()) {
						System.out.println("Executing data statement: " + statement);
						stmt.execute(statement);
					}
				}
			}
			
			// 关闭连接
			stmt.close();
			conn.close();
			
			System.out.println("Database initialized successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}