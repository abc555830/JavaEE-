import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.dom4j.Document;
import org.springframework.util.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class MiniApplicationContext {
    public static void main(String[] args){
        try
        {
            Map<String, Object> beans = ReadBeanDefinition.createBean();
            for (Map.Entry<String, Object> next : beans.entrySet()) {
                System.out.println(next.getKey());
                System.out.println(next.getValue());
            }
        }
        catch (CustomException e)
        {
            System.out.println(e.getErrorType()+":"+e.getMessage());
        }
    }
}
