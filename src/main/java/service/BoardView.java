package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDao;
import model.BoardDto;
import model.ReplyDao;
import model.ReplyDto;

public class BoardView implements Command {

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		
		BoardDao dao = new BoardDao();
		dao.increViewCnt(bno);
		
		BoardDto dto = dao.selectList1(bno);
		List<ReplyDto> list = new ReplyDao().selectList(bno);
		
		request.setAttribute("view", dto);
		request.setAttribute("replylist", list);
		return true;
	}

}
