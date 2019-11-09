package com.example.demo.converter;


import com.example.demo.entity.Project;

public class UserDaoImpl extends GetGenericUtil<User, Project>{

    public User getUser() throws Exception{
        Object obj = this.getFirstClass().newInstance();//得到泛型类，然后创建一个新实例。
        if(obj instanceof User){
            return (User)obj;
        }else{
            return null;
        }

    }

    public Project getProject() throws Exception{
        Object obj = this.getSecondClass().newInstance();//得到泛型类，然后创建一个新实例。
        if(obj instanceof Project){
            return (Project)obj;
        }else{
            return null;
        }

    }

    public static void main(String[] args) throws Exception {

        UserDaoImpl dao = new UserDaoImpl();
        User u = dao.getUser();
        Project p = dao.getProject();

        System.out.println(u+"----");
        System.out.println(p+"----");

    }

}