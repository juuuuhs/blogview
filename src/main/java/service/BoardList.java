package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDao;
import model.BoardDto;

public class BoardList implements Command {
	
	private static final int page_size = 10; //한페이지에 보여줄 개수
	private static final int page_block = 5; //하단에 보여줄 페이지 번호 1 2 3 4 5 

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String searchType = request.getParameter("searchType");
		String keyword = request.getParameter("keyword");
		
		//현재 페이지 번호 (없거나 이상한 값이면 1페이지)
		int currentPage=1;
		String pageParam = request.getParameter("page");
		if(pageParam != null) {
			try {
				currentPage =Integer.parseInt(pageParam);
			}catch (NumberFormatException e) {
				currentPage =1;
			}
		}
		
		BoardDao dao = new BoardDao();
		//전체 페이지 수 계산
		int totalCount = dao.selectCount(searchType, keyword);
		int totalPage = (int)Math.ceil((double)totalCount /page_size); //math_ceil : 무조건 올림 반올림도 아님 
		if(totalPage == 0)totalPage = 1; //글이 하나도 없을 때 1페이지라는 말 
		if(currentPage < 1) currentPage =1;
		if(currentPage >totalPage) currentPage = totalPage;
		
		//이번 페이지 가져올 구간
		int startRow = (currentPage -1) * page_size +1 ;
		int endRow = currentPage * page_size;
		
		List<BoardDto> list = dao.selectList(searchType, keyword, startRow, endRow);
		
		//하단 페이지 번호 구간 계산
		int startPage = ((currentPage -1)/ page_block)*page_block+1;
		int endPage = Math.min(startPage+page_block -1, totalPage);
		
		//속성에 담아 포워딩 
		request.setAttribute("boardList", list);
		request.setAttribute("searchType", searchType);
		request.setAttribute("keyword", keyword);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		return true;
	}

	

}
