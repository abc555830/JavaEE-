import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadBeanDefinition {
    static public Map<String, Object> createBean() throws CustomException {
        //xml路径
        String path = "src/main/resources/applicationContext.xml";
        //创建dom4j解析器
        SAXReader saxReader = new SAXReader();
        Map<String, Object> beans = new HashMap<>();
        try{
            //解析xml，变为document对象
            Document document = saxReader.read(path);
            //获取根节点
            Element root = document.getRootElement();
            //获取根节点下所有标签名为bean的element节点
            List<Element> beanList = root.elements("bean");
            if (beanList == null)
            {
                throw new CustomException(CustomException.ErrorType.TAG_NOT_EXISTS, "没找到标签名为bean的节点");
            }
            for (Element bean: beanList)
            {
                //根据属性名获取element属性值
                String id = bean.attributeValue("id");
                if (id == null)
                {
                    throw new CustomException(CustomException.ErrorType.PROPERTY_NOT_EXISTS, "没找到属性名为id的属性");
                }
                String className = bean.attributeValue("class");
                if (className == null)
                {
                    throw new CustomException(CustomException.ErrorType.PROPERTY_NOT_EXISTS, "没找到属性名为class的属性");
                }
                List<Element> tagList = bean.elements("constructor-arg");
                List<Class<?>> classList = new ArrayList<>();
                List<Object> valueList = new ArrayList<>();

                for (Element tag: tagList)
                {
                    String typeProp = tag.attributeValue("type");
                    String valueProp = tag.attributeValue("value");
                    if (valueProp==null || typeProp == null)
                    {
                        throw new CustomException(CustomException.ErrorType.PROPERTY_NOT_EXISTS, "没找到bean属性名为type或value的属性");
                    }
                    Object valueObj = TypeConvertionUtils.stringToTarget(valueProp, typeProp);
                    classList.add(valueObj.getClass());
                    valueList.add(valueObj);
                }

                //反射创建对象
                try{
                    int listSize = classList.size();
                    Class<?>[] paramTypeArray = classList.toArray(new Class[listSize]);
                    Object[] valueArray = valueList.toArray(new Object[listSize]);
                    //Method[] methods = ClassForTest.class.getMethods();
                    //for (Method method: methods)
                    //{
                    //    Class<?>[] parameterTypes = method.getParameterTypes();
                    //    for(Class parameterType: parameterTypes){
                    //        System.out.println(parameterType.getClass() + "===="+
                    //                parameterType.getName());
                    //    }
                    //}
                    Class<?> beanClass = Class.forName(className);
                    Object o = beanClass.getConstructor(paramTypeArray).newInstance(valueArray);
                    beans.put(id, o);
                }
                catch (java.lang.ClassNotFoundException e)
                {
                    throw new CustomException(CustomException.ErrorType.CLASS_NOT_FOUND, "未找到对应bean类名的类");
                }
                catch (java.lang.NoSuchMethodException e)
                {
                    //System.out.println(e.getMessage());
                    throw new CustomException(CustomException.ErrorType.NO_SUCH_METHOD, "未找到对应构造器方法");
                }
                catch (java.lang.InstantiationException e)
                {
                    throw new CustomException(CustomException.ErrorType.INSTANTIATION_FAIL, "实例化异常");
                }
                catch (java.lang.IllegalAccessException e)
                {
                    throw new CustomException(CustomException.ErrorType.NO_LEGAL_ACCESS, "没有访问权限的异常");
                }
                catch (java.lang.reflect.InvocationTargetException e)
                {
                    throw new CustomException(CustomException.ErrorType.INVOCATION_EXCEPTION, "被调用的方法的内部抛出了异常");
                }
            }
        }
        catch(org.dom4j.DocumentException e)
        {
            //System.out.println(e.getMessage());
            throw new CustomException(CustomException.ErrorType.FILE_NOT_EXITS, "路径下无匹配XML文件！");
        }
        return beans;
    }
}
