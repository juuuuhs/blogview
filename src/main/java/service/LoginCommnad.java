package service;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDao;
import model.MemberDto;

public class LoginCommnad implements Command {

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if("get".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		
		
		
		String pwd = request.getParameter("pwd");
		
		MemberDao dao = new MemberDao();
		MemberDto dto =dao.login(id);
		if(dto!= null && BCrypt.checkpw(pwd, dto.getPwd())) { //세션 만들기  = 서버에 정보를 저장 , 쿠키 - 내 컴퓨터에 저장
			//평문->암호화 
			HttpSession session = request.getSession(); 	//이게 세션 하나를 만든 방법 객체하나를 생성해야 함
			session.setAttribute("loginMember", dto);
			response.sendRedirect(request.getContextPath()+"/");
			return false;
		}else{
			request.setAttribute("loginfail","아이디 또는 비밀번호를 확인해 주세요");
			//처리된 값을 jsp로 출력을 하려면 request,session 속성에 저장해서 포워딩한다
			return true;
		}
		
	}

}
