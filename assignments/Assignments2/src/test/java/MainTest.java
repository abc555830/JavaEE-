import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class MainTest {
    static Main mainClass;
    @BeforeAll
    static void setUp()
    {
        mainClass = new Main();
    }
    void createObjByProp() {
        User u = new User();
        //获取目标类名
        Class<?> targetClass = u.getClass();
        String path = "/myapp.properties";
        //对成功创建对象进行测试
        assertEquals(targetClass, mainClass.createObjByProp(path).getClass());
        //对类不存在的异常情况进行测试
        String exceptionPath = "/testException.properties";
        //断言类不存在时抛出NullPointerException异常
        assertThrows(NullPointerException.class, ()->{
            Class<?> testClass = mainClass.createObjByProp(exceptionPath).getClass();
        });
    }
    @Test
    void exeMethod() {
        User u = new User();
        //断言能够返回正确结果
        assertTrue(mainClass.exeMethod(u, null));
        //测试在注解方法有参数时抛出异常
        try
        {
            mainClass.exeMethod(u, "hahaha");
        }
        catch (Exception e)
        {
            System.out.println("Exception caught!");
        }
    }
}
