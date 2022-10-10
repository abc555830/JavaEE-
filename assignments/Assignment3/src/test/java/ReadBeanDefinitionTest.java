import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReadBeanDefinitionTest {
    @org.junit.jupiter.api.Test
    void createBean() {
        ClassForTest cls = new ClassForTest();
        try
        {
            Map<String, Object> beans = ReadBeanDefinition.createBean();
            Object obj1 = beans.get("obj1");
            if (obj1 != null)
            {
                //确认创造的bean对象为ClassForTest类
                assertEquals(cls.getClass().getName(), obj1.getClass().getName());
                ClassForTest cls1 = (ClassForTest)obj1;
                Field[] fields = cls1.getClass().getDeclaredFields();
                String[] fieldNames = new String[fields.length];
                for (int i = 0; i < fields.length; i++)
                {
                    fieldNames[i] = fields[i].getName();
                }

                String firstLetter = fieldNames[0].substring(0,1).toUpperCase();
                String getter = "get" + firstLetter + fieldNames[0].substring(1);
                Method method = cls.getClass().getMethod(getter);
                Object value = method.invoke(cls1);
                //确认num值
                assertEquals(26, value);

                firstLetter = fieldNames[1].substring(0,1).toUpperCase();
                getter = "get" + firstLetter + fieldNames[1].substring(1);
                method = cls.getClass().getMethod(getter);
                value = method.invoke(cls1);
                //确认flag值
                assertEquals(true, value);

                firstLetter = fieldNames[2].substring(0,1).toUpperCase();
                getter = "get" + firstLetter + fieldNames[2].substring(1);
                method = cls.getClass().getMethod(getter);
                value = method.invoke(cls1);
                //确认name值
                assertNull(value);
            }
        }
        catch (CustomException e)
        {
            System.out.println(e.getErrorType()+":"+e.getMessage());
        } catch (NoSuchMethodException e) {
            System.out.println("未找到对应构造器方法");
        } catch (InvocationTargetException e) {
            System.out.println("InvocationTargetException");
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException");
        }
    }
}