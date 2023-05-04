package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import main.RegisterServlet;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;



import static org.junit.Assert.assertEquals;

public class RegisterServletTest {
	

	private static final String INSERT_QUERY = "INSERT INTO mytable (name, city, mobile, dob) VALUES (?, ?, ?, ?)";

	
    private RegisterServlet servlet;

	
	 @Mock 
	 private HttpServletRequest request;
	   
	 @Mock 
	 private HttpServletResponse response;
	 

    private StringWriter writer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        servlet = new RegisterServlet();
        writer = new StringWriter();
        try {
            when(response.getWriter()).thenReturn(new PrintWriter(writer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoPost() throws ServletException, IOException, SQLException {
    	
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        // Stubbing request parameters
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("city")).thenReturn("New York");
        when(request.getParameter("mobile")).thenReturn("1234567890");
        when(request.getParameter("dob")).thenReturn("12-01-2007");
        
        String url = "jdbc:mysql://test-mysql-java.mysql.database.azure.com:3306/firstdb?useSSL=true&requireSSL=false&serverTimezone=UTC";
		String user = "appuser";
		String pwd = "Abcd1234!";
		
       // when(DriverManager.getConnection(url, user, pwd)).thenReturn(mockConnection);

		try (MockedStatic<DriverManager> driverManager = Mockito.mockStatic(DriverManager.class)) {
			driverManager.when(() -> DriverManager.getConnection(url, user, pwd)).thenReturn(mockConnection);
			// Stubbing JDBC calls
	        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
	        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

	        servlet.doPost(request, response);

	        assertEquals("Record stored into database for::John", writer.toString().trim());
		}
        
    }
    
}
