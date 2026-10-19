package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.Command;
import service.IdCheckcommand;
import service.LoginCommnad;
import service.LogoutCommand;
import service.MemberInsert;
import service.MypageCommand;

import java.io.IOException;

@WebServlet("/member/*") //*를 붙이면 membercontroller가 실행됨
//URL을 통해서 요처하는 주소가 /member로 시작하는 모든 요청(*)은 이 클래스가 자동으로 실행
public class membercontroller extends HttpServlet {
	
	//직렬화 관련 버전 번호
	private static final long serialVersionUID = 1L;
       
 
    public membercontroller() {
    	//생성자
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request,response);
	
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String pathInfo = request.getPathInfo();
		//localhost:8030/blogproject/memeber/join 제일 마지막에 있는 방식이 pathInfo
		System.out.println("pathInfo:"+pathInfo);
		
		String page = null;
		Command command =null;
		
		switch(pathInfo) {
		case"/join":
			command = new MemberInsert(); //다형성
			page="/mem/join.jsp";
			break;
		case"/idCheck": //localhost:8030/blogproject/memeber/join/idCheck
		command = new IdCheckcommand();
			page=null;
			break;
		case"/login": //localhost:8030/blogproject/memeber/login/
			command = new LoginCommnad();
			page ="/mem/login.jsp";
			break;
		case"/logout":
			command = new LogoutCommand();
			page=null; //포워딩 할 주소가 없기에 null로 작성 
			break;
		case"/mypage":
			command = new MypageCommand();
			page ="/mem/mypage.jsp";
			break;
		}
		
		boolean needForward = command.docommnad(request, response);
				
		
		if(needForward && page!= null) {
			RequestDispatcher rd = request.getRequestDispatcher(page);
			rd.forward(request, response);
		}
		

	}
}
