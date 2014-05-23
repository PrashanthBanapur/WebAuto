/**
 * 
 */
package org.seltest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author adityas
 *
 */
public class JdbcDao {
	private static Logger log = LoggerFactory.getLogger(JdbcDao.class);

	static {
		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			log.error("JDBC Class load : {}"+e.getLocalizedMessage());
		}
	}
	
	public void closeConn(Connection conn) {
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("Close Connection : {}"+e.getLocalizedMessage());
			}
		}		
	}
	
	public Connection openConn() throws SQLException {
		//jdbc:sqlserver://server:port;DatabaseName=dbname
		String url = "jdbc:sqlserver://*********;DatabaseName=**********";
		String uname = "********";
		String pass = "********";
	
			Connection conn = (Connection) DriverManager.getConnection(url,uname,pass);
			return conn;
	}
}
