package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BoardDao;
import model.BoardDto;
import model.MemberDto;
import model.ReplyDao;
import model.ReplyDto;

public class MypageCommand implements Command {

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		//새션객체 생성
		HttpSession session = request.getSession();
		//loginMember 속성을 memberDto 개체로 변환
		MemberDto dto = (MemberDto)session.getAttribute("loginMember");
		//loginMember가 null하고 같으면 로그인 페이지로 이동
		if(dto == null) {
			response.sendRedirect(request.getContextPath()+"/member/login");
			return false;
		}
		//loginMember에 있는 id를 변수에 저장하고
		String id = dto.getId();
		// 아이디를 replydao,boarddo 객체를 생성하고 메서드를 호출하면서 아이디를 보낸다
//		String  writer = request.getParameter("writer");
//		String  memberId = request.getParameter("memberId");
		
		List<BoardDto> myPosts = new BoardDao().selectListByWriter(id);
		List<BoardDto> myLikes = new BoardDao().selectLikedList(id);
		List<ReplyDto> myReplies = new ReplyDao().selectListBywriter(id);		
		
		//메서드를 처리하고 리턴된 가변배열의 값 3개를 속성에 담는다
		
		
		request.setAttribute("myPosts", myPosts);
		request.setAttribute("myReplies", myReplies);
		request.setAttribute("myLikes", myLikes);
		return true;
	}

}
