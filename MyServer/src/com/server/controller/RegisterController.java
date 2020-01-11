package com.server.controller;
import com.server.service.Service;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterController extends HttpServlet{
    private int type;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username="";
        String password="";
        String name="";
        String age="";
        String telephone="";

        StringBuffer jb= new StringBuffer();
        String line=null;
        BufferedReader reader=request.getReader();
        while((line=reader.readLine())!=null)
            jb.append(line);
        System.out.println("getJsonString"+jb.toString());
        JSONObject jsonObject= JSONObject.fromObject(jb.toString());
        username=jsonObject.getString("username");
        password=jsonObject.getString("password");
        name=jsonObject.getString("name");
        age=jsonObject.getString("age");
        telephone=jsonObject.getString("telephone");

        Service serv = new Service();

        //判断username是否存在
        if (!serv.execute(username)) {
            // 验证处理
            boolean loged = serv.register(username,password,name,age,telephone);
            if (loged) {
                System.out.print("Register Successful !!!");
                type = 0;
            } else {
                System.out.println("插入失败");
                type = 1;
            }
        } else {
            System.out.println("用户已存在，Failed");
            type = 1;
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //返回状态码
        out.print(serv.toJson(type,"",0));
        out.flush();
        out.close();
    }
}
