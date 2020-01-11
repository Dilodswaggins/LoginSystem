package com.server.service;

import com.server.dbutil.DbManager;
import net.sf.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Service {

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    public boolean register(String username, String password,String name,String age,String telephone) {

        if ((!("").equals(username)) && (!("").equals(password))) {
            // 获取Sql查询语句
            String regSql = "insert into user(username, password,name,age,telephone) values('" + username + "','" + password + "','"+ name + "','"+ age +"','"+telephone+"') ";
            // 获取DB对象
            DbManager sql = DbManager.createInstance();
            sql.connectDB();

            int ret = sql.executeUpdate(regSql);
            if (ret != 0) {
                sql.closeDB();
                return true;
            }
            sql.closeDB();
            return false;
        } else {
            return false;
        }

    }

    /**
     * 查询是否存在
     *
     * @param username
     * @return
     */
    public boolean execute(String username) {

        if ((!("").equals(username))) {

            String exeSql = "select * from user where username ='" + username + "'";
            boolean flag = true;
            DbManager sql = DbManager.createInstance();
            sql.connectDB();

            try {
                ResultSet exe = sql.executeQuery(exeSql);
                //username存在，返回true
                if (exe.next()) {
                    sql.closeDB();
                    flag = true;
                } else {
                    flag = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return flag;
        } else {
            return false;
        }

    }



    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public boolean login(String username, String password) {

        if ((!("").equals(username)) && (!("").equals(password))) {
            // 获取Sql查询语句
            String logSql = "select * from user where username ='" + username
                    + "' and password ='" + password + "'";

            // 获取DB对象
            DbManager sql = DbManager.createInstance();
            sql.connectDB();

            // 操作DB对象
            try {
                ResultSet rs = sql.executeQuery(logSql);
                if (rs.next()) {
                    sql.closeDB();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql.closeDB();
            return false;
        } else {
            return false;
        }
    }

    /**
     * 封装json
     *
     * @param type
     * @return
     */
    public String toJson(int type,String username,int num) {
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";
        jsonObject.put("type", type);
        jsonObject.put("username",username);
        jsonObject.put("num",num);
        jsonString = jsonObject.toString();
        return jsonString;
    }

    /**
     * 查询
     *
     * @param username
     * @return num
     */
    public int search(String username) {
        int num=0;
        if (!("").equals(username)) {
            String sql = "select * from user where username ='" + username + "'";
            DbManager DB = DbManager.createInstance();
            DB.connectDB();
            try {
                ResultSet rs = DB.executeQuery(sql);
                while (rs.next()) {
                    num = rs.getInt("num");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return num;
    }

    public boolean forget(String telephone,String username,String password){
        // 获取Sql查询语句
        String exeSql="";
        if ((!("").equals(username)) && (!("").equals(telephone)) && (!("").equals(password))) {
            String identisql = "select * from user where username ='" + username
                    + "' and telephone ='" + telephone + "'";
            DbManager sql = DbManager.createInstance();
            sql.connectDB();
            try{
                ResultSet rs=sql.executeQuery(identisql);
                if(rs.next()){
                    exeSql = "update user set password='" + password + "'where username='"+username+"'";
                    if (sql.executeUpdate(exeSql)==1) {
                        sql.closeDB();
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }
    /**
     * 修改
     */
    public boolean change(String num,String username,String password,String telephone,String age){
        // 获取Sql查询语句
        String exeSql="";
        if ((!("").equals(num))) {
            if(username!="") {
                exeSql = "update user set name='" + username + "'" ;
            }else{
                exeSql = "update user set num='"+num+"'";
            }
            if(password!=""){
                exeSql = exeSql+",password='"+password+"'";
            }
            if(telephone!=""){
                exeSql = exeSql+",telephone='"+telephone+"'";
            }
            if(age!=""){
                exeSql = exeSql+",age='"+age+"'";
            }
            exeSql = exeSql+"where num='"+num+"'";
            DbManager sql = DbManager.createInstance();
            sql.connectDB();
            if (sql.executeUpdate(exeSql)==1) {
                sql.closeDB();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }
}
