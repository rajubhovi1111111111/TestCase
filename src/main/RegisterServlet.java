package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String INSERT_QUERY = "INSERT INTO user(name,city,mobile,dob) VALUES(?,?,?,?)";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		String city = request.getParameter("city");
		String mobile = request.getParameter("mobile");
		String dob = request.getParameter("dob");
		// create the connection

		String url = "jdbc:mysql://test-mysql-java.mysql.database.azure.com:3306/firstdb?useSSL=true&requireSSL=false&serverTimezone=UTC";
		String user = "appuser";
		String pwd = "Abcd1234!";

		try (Connection con = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = con.prepareStatement(INSERT_QUERY);) {

			// set the values
			ps.setString(1, name);
			ps.setString(2, city);
			ps.setString(3, mobile);
			ps.setString(4, dob);
			// execute the query
			int count = ps.executeUpdate();

			if (count == 0) {
				out.println("Record not stored into databas");
			} else {
				out.println("Record stored into database for::" + name);
			}
		} catch (SQLException se) {

			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		// close the stream
	}

}
