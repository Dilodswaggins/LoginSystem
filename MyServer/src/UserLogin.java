import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import net.sf.json.JSONObject;

import java.io.PrintWriter;



public class UserLogin extends HttpServlet {
    private int type=0;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jb= new StringBuffer();
        String line=null;
        BufferedReader reader=request.getReader();
        while((line=reader.readLine())!=null)
            jb.append(line);
        JSONObject jsonObject= JSONObject.fromObject(jb.toString());
        String username=jsonObject.getString("username");
        String password=jsonObject.getString("password");
        User newuser=new User(username,password);




        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //返回状态码
        JSONObject jsonObject1= new JSONObject();
        String jsonString="";
        jsonObject1.put("type",type);
        jsonString= jsonObject1.toString();
        System.out.println(jsonString);
        out.print(jsonString);
        out.flush();
        out.close();
    }
}