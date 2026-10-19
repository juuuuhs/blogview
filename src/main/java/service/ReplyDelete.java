package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDto;
import model.ReplyDao;

public class ReplyDelete implements Command {

	@Override
	public boolean docommnad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDto dto = (MemberDto)session.getAttribute("loginMember");
		
		Map<String, Object> result = new HashMap<>();
		if(dto ==null) {
			result.put("success", false);
			result.put("message","로그인 해주세요");
			sendJson(response,result);
			return false;
		}
		int rno = Integer.parseInt(request.getParameter("rno"));
		int cnt = new ReplyDao().delete(rno, dto.getId());
		
		if(cnt>0) {
			result.put("success", true);
		}else {
			result.put("success", false);
			result.put("message", "본인 댓글에만 삭제가 가능합니다");
		}
		
		sendJson(response,result);
		
		
		return false;
		
	}
	private void sendJson(HttpServletResponse response, Map<String, Object> result) {
		response.setContentType("application/json; charset=utf-8");
		Gson gson = new Gson();
		String json = gson.toJson(result); //result 객체를 ,json 형식으로 데이터를 변환
		try {
			PrintWriter out = response.getWriter();
			out.print(json); //호출한 클라이언트 json 데이터를 보낸다
		}catch (Exception e) {
			e.printStackTrace();
			
		}
	}

}
