package zhangyi.training.school.rp.rxjava.observables.transforming;

public final class Person implements Cloneable {
    String name;
    Integer age;
    String sex;

    public Person(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person copy() {
        try {
            return (Person) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
