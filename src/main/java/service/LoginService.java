package service;

import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoginService {

	public Boolean loginServ(HttpServletRequest req, HttpServletResponse res) {
		ApplicationContext applicationContext= new ClassPathXmlApplicationContext("resources/dependancy.xml");
		HttpSession session = req.getSession();
		DBConnection dbConnection=(DBConnection)applicationContext.getBean("dbConnectionService");
		VariableService variableService=((VariableService)applicationContext.getBean("variableService"));
		dbConnection.connectionMethod();
		Boolean flag = null ;
		variableService.setId((req.getParameter("UserId")));
		variableService.setPassword((req.getParameter("Password")));
		ResultSet resultSet = (dbConnection.resultData(variableService.getId(),"select * from posLogin where userId=?"));
		try {
			if (resultSet.next()) {
				String dbUserid = resultSet.getString("UserId");
				String dbPass = resultSet.getString("Password");

				if (variableService.getId().equalsIgnoreCase(dbUserid) && variableService.getPassword().equals(dbPass))
					flag = true;;
				
				if (flag == true) {
					String loginSession = variableService.getId();
					session.setAttribute("loginSession", loginSession);
					return true;
				}
			}
		}catch(Exception e){
			System.out.println("Login Service Catch.....  "+ e);
		}
		return false;
	}
}