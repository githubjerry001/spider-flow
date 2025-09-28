import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class RunSpiderFlowWithInit {
    public static void main(String[] args) {
        try {
            // 初始化数据库
            initDatabase();
            
            // 启动SpiderFlow应用程序
            startSpiderFlow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void initDatabase() throws Exception {
        System.out.println("Initializing database...");
        
        // 加载H2驱动
        Class.forName("org.h2.Driver");
        
        // 连接到H2内存数据库（使用与SpiderFlow相同的URL）
        Connection conn = DriverManager.getConnection(
            "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL", 
            "sa", 
            ""
        );
        
        // 读取SQL文件
        String sql = new String(
            Files.readAllBytes(Paths.get("spider-flow-web/src/main/resources/schema.sql")), 
            StandardCharsets.UTF_8
        );
        
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
        
        // 插入一些初始数据以避免空表问题
        System.out.println("Inserting initial data...");
        stmt.execute("INSERT INTO sp_variable (name, value, description) VALUES ('test', 'value', 'Test variable')");
        stmt.execute("INSERT INTO sp_function (id, name, parameter, script) VALUES ('1', 'testFunc', 'param', 'return param;')");
        
        // 关闭连接
        stmt.close();
        conn.close();
        
        System.out.println("Database initialized successfully!");
    }
    
    private static void startSpiderFlow() throws Exception {
        System.out.println("Starting SpiderFlow application...");
        
        // 使用Runtime.getRuntime().exec()启动应用程序
        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens", "java.base/java.text=ALL-UNNAMED",
            "--add-opens", "java.desktop/java.awt.font=ALL-UNNAMED",
            "-jar", "spider-flow-web/target/spider-flow.jar"
        );
        
        // 继承IO以便查看输出
        pb.inheritIO();
        
        // 启动进程
        Process process = pb.start();
        
        // 等待进程结束（实际上我们希望它一直运行）
        System.out.println("SpiderFlow application started. Press Ctrl+C to stop.");
        process.waitFor();
    }
}