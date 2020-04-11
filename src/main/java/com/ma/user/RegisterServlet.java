package com.ma.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/users/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        String username = req.getParameter("username");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        //1.获取数据库连接
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        //2.创建并拼装sql语句
        String sql = "insert into users(id,username,nickname,password) value(null,?,?,?)";
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,username);
            statement.setString(2,nickname);
            statement.setString(3,password);
            //3.执行sql语句
            int i = statement.executeUpdate();
            int id = -1;
            if (i == 1){
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                id = rs.getInt(1);
                resp.getWriter().println("数据插入成功！");
                resp.getWriter().println(id);
                resp.getWriter().println(username);
                resp.getWriter().println(nickname);
                resp.getWriter().println(password);
            }else {
                resp.getWriter().write("数据插入失败！");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                //4.关闭连接和statement对象,必须执行在finally块中
                DBUtil.close(connection,statement,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
