public class TypeConvertionUtils {
    //将字符串转换为对应类型
    public static Object stringToTarget(String str, String className) {
        if ("int".equals(className)) {
            return Integer.parseInt(str);
        }
        if ("boolean".equals(className)) {
            return !"false".equals(str);
        }
        return str;
    }
}
