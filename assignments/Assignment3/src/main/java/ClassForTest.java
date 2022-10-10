public class ClassForTest {
    private int num;
    private boolean flag;
    private String name;

    public void setNum(int numParam) {
        num = numParam;
    }

    public int getNum() {
        return num;
    }

    public void setFlag(boolean flagParam) {
        flag = flagParam;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setName(String nameParam) {
        name = nameParam;
    }

    public String getName() {
        return name;
    }
    public ClassForTest() {
        num = 1;
        flag = true;
        name = "Ming";
    }


    public ClassForTest(Integer numParam) {
        num = numParam;
    }

    public ClassForTest(Integer numParam, Boolean flagParam) {
        num = numParam;
        flag = flagParam;
    }

    public ClassForTest(Integer numParam, Boolean flagParam, String nameParam) {
        num = numParam;
        flag = flagParam;
        name = nameParam;
    }
    //用于打印信息
    public String toString() {
        return "ClassForTest{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", flag=" + flag +
                '}';
    }
}
