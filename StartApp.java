import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class StartApp {
    public static void main(String[] args) {
        try {
            // 初始化数据库
            initDatabase();
            
            // 启动应用程序
            startApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void initDatabase() throws Exception {
        // 加载H2驱动
        Class.forName("org.h2.Driver");
        
        // 连接到H2内存数据库
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        
        // 读取SQL文件
        String sql = new String(Files.readAllBytes(Paths.get("spider-flow-web/src/main/resources/schema.sql")), StandardCharsets.UTF_8);
        
        // 分割SQL语句
        String[] statements = sql.split(";");
        
        // 创建Statement对象
        Statement stmt = conn.createStatement();
        
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
    }
    
    private static void startApplication() throws Exception {
        // 启动应用程序进程
        ProcessBuilder builder = new ProcessBuilder(
            "java",
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens", "java.base/java.text=ALL-UNNAMED",
            "--add-opens", "java.desktop/java.awt.font=ALL-UNNAMED",
            "-jar", "spider-flow-web/target/spider-flow.jar"
        );
        
        builder.inheritIO(); // 继承输入输出
        Process process = builder.start();
        
        // 等待进程结束
        int exitCode = process.waitFor();
        System.out.println("Application exited with code: " + exitCode);
    }
}