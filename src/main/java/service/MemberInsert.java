package service;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MemberDao;
import model.MemberDto;

public class MemberInsert implements Command {

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//get방식으로 요청이 들어오면 다음 코딩을 실행을 하지 못하게 한다
		if("get".equalsIgnoreCase(request.getMethod())) { //request.getMethod() 요청방식을 알고 싶을 때 사용하는 매게변수
			return true;
		}
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String hashPwd =BCrypt.hashpw(pwd, BCrypt.gensalt()); //비밀번호 암호화 시키는 거 (Bcrypt)를 다운 받고 lib에 넣어놈 
		String nickname = request.getParameter("nickname");
		String email = request.getParameter("email");
		
		MemberDto dto = new MemberDto();
		dto.setId(id);
		dto.setEmail(email);
		dto.setNickname(nickname);
		dto.setPwd(hashPwd);
		
		
		MemberDao dao = new MemberDao();
		dao.memberInsert(dto);
		
		response.sendRedirect(request.getContextPath()+"/member/login");
		// sendRedirect : 무조건 실행해줘  
		return false;  //위에서 리다이렉트하면 false을 리턴하고 포워딩이 필요하면 ture를 리턴한다 컨트롤러에서 써 먹을라고 
	}

}
