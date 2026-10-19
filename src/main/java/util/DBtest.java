package util;

import java.sql.Connection;

public class DBtest {

	public static void main(String[] args) {
		try {
			Connection conn = DBmanager.getInstance();
			if(conn != null) {
				System.out.println("접속 성공");
			}else {
				System.out.println("접속 실패");
			}
		}catch(Exception e) {
			e.getStackTrace();
			
		}
	}

}
