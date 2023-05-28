import java.io.*;
import java.util.Properties;

class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.load(new FileReader("./testFolder/dog.properties"));
        String name = properties.getProperty("name");
        int age = new Integer(properties.getProperty("age"));
//        int age = Integer.getInteger(properties.getProperty("age"));
        String color = properties.getProperty("color");
        Dog dog = new Dog(name, age, color);
        System.out.println(dog);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("./testFolder/dog.dat"));
        objectOutputStream.writeObject(dog);
        objectOutputStream.close();
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("./testFolder/dog.dat"));
        System.out.println(objectInputStream.readObject());
    }
}

class Dog implements Serializable {
    private String name;
    private int age;
    private String color;

    public Dog(String name, int age, String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                '}';
    }
}