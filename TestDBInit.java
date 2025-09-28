import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@SpringBootApplication
public class TestDBInit {
    public static void main(String[] args) {
        // First initialize the database
        initDB();
        
        // Then start the SpiderFlow application
        SpringApplication.run(org.spiderflow.SpiderApplication.class, args);
    }
    
    private static void initDB() {
        try {
            // 加载H2驱动
            Class.forName("org.h2.Driver");
            
            // 连接到H2内存数据库
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            
            // 读取SQL文件
            String sql = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("spider-flow-web/src/main/resources/schema.sql")), java.nio.charset.StandardCharsets.UTF_8);
            
            // 分割SQL语句
            String[] statements = sql.split(";");
            
            // 创建Statement对象
            java.sql.Statement stmt = conn.createStatement();
            
            // 执行每个SQL语句
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    System.out.println("Executing: " + statement);
                    stmt.execute(statement);
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
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .build();
    }
}