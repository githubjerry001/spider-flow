import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestH2Connection {
    public static void main(String[] args) {
        try {
            // 加载H2驱动
            Class.forName("org.h2.Driver");
            
            // 连接到H2内存数据库
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            
            // 创建Statement对象
            Statement stmt = conn.createStatement();
            
            // 查询所有表
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            
            System.out.println("Tables in the database:");
            boolean hasTables = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
                hasTables = true;
            }
            
            if (!hasTables) {
                System.out.println("No tables found in the database.");
            }
            
            // 关闭连接
            rs.close();
            stmt.close();
            conn.close();
            
            System.out.println("Database connection test completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}