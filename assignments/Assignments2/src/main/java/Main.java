import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.function.Supplier;
public class Main {
    public Object createObjByProp(String path) {
        Properties props = new Properties();
        String className = null;
//        从resources文件夹下的配置文件获取输入
        try (InputStream input = this.getClass().getResourceAsStream(path)) {
            if (input == null) {
                return null;
            }
            props.load(input);
//            读取键值为"bootstrapClass"的值
            className = props.getProperty("bootstrapClass");
            System.out.println(className);
        } catch (IOException e) {
            System.out.println("Load properties error!");
            return null;
        }
        if (className != null) {
            try {
                Class<?> newClass = Class.forName(className);
//                使用目标类的构造函数实例化对象
                Constructor<?> co = newClass.getConstructor();
                return co.newInstance();
            } catch (NoSuchMethodException e) {
                System.out.println("No Such Method!");
                return null;
            } catch (InstantiationException e) {
                System.out.println("Instantiation fail!");
                return null;
            } catch (IllegalAccessException e) {
                System.out.println("illegal access to method!");
                return null;
            } catch (InvocationTargetException e) {
                System.out.println("InvocationTargetException!");
                return null;
            } catch (ClassNotFoundException e) {
                System.out.println("class not found!");
                return null;
            }
        } else {
            return null;
        }

    }
    //调用对象的注解方法
    public boolean exeMethod(Object obj, String args) {
        if (obj != null) {
            Class<?> newClass = obj.getClass();
//        获取对应类的所有方法
            Method[] methods = newClass.getDeclaredMethods();
            for (Method method : methods) {
//            对每个方法获取其所有注解
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                for (Annotation annotation : declaredAnnotations) {
//                如果所取为目标注解
                    if ("@InitMethod()".equals(annotation.toString())) {
                        try {
//                        调用目标方法
                            if (args != null) {
                                method.invoke(obj, args);
                                return true;
                            } else {
                                method.invoke(obj);
                                return true;
                            }
                        } catch (IllegalAccessException e) {
                            System.out.println("illegal access to method!");
                            return false;
                        } catch (InvocationTargetException e) {
                            System.out.println("InvocationTargetException!");
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            return false;
        }
        else
        {
            return false;
        }
    }

    public static void main(String[] args) {
        Main toCreate = new Main();
        String path = "/myapp.properties";
        Object obj = toCreate.createObjByProp(path);
        toCreate.exeMethod(obj, null);
    }
}

