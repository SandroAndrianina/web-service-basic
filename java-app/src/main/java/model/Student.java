package main.java.model;

public class Student {

    private int id;
    private String name;
    private int age;
    private int province_id;

    public Student(){};

    public Student(int id, String name, int age, int province_id){
        this.id = id;
        this.name = name;
        this.age = age;
        this.province_id = province_id;
    }

    //getters
    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getAge(){ return age; }
    public int getProvinceId(){ return province_id; }

    //setters
    public void setId(int id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setAge(int age){ this.age = age; }
    public void setProvinceId(int province_id){ this.province_id = province_id; }

}