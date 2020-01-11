public class User {
    public String username;
    public String password;
    public User(){

    }

    public User(String username,String password){
        super();
        this.password=password;
        this.username=username;
    }

    public void ouput(){
        System.out.println(username);
        System.out.println(password);
    }
}
