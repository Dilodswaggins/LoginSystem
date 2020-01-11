package com.server.controller;

import com.server.service.Service;
import com.server.service.Service;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import net.sf.json.JSONObject;

public class ForgetController extends HttpServlet{
    private int type;
    private String username="";
    private String password="";
    private String telephone="";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jb= new StringBuffer();
        String line=null;
        BufferedReader reader=request.getReader();
        while((line=reader.readLine())!=null)
            jb.append(line);
        System.out.println("getJsonString"+jb.toString());
        JSONObject jsonObject= JSONObject.fromObject(jb.toString());
        username=jsonObject.getString("username");
        password=jsonObject.getString("password");
        telephone=jsonObject.getString("telephone");
        // 新建服务对象
        Service serv = new Service();
        // 验证处理
        boolean loged = serv.forget(telephone,username,password);
        if (loged) {
            System.out.print("Modify Successful !!!");
            type = 0;
        } else {
            System.out.print("修改失败，请重试！");
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
