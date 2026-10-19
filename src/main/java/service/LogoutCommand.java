package service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		session.invalidate(); //세션안의 모든 값이 제거
		
		response.sendRedirect(request.getContextPath()+"/"); //마지막의 /는 최상위루트 주소로 시작하겠다
		
		return false; //forward을 안 할 땐 false를 사용
	}

}
