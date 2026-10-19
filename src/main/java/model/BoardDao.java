package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBmanager;

public class BoardDao {
	public void insert(BoardDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into board(bno,title,content, writer,img_path)"
				+ " values(SEQ_BOARD.nextval,?,?,?,?)";
		
		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getWriter());
			pstmt.setString(4, dto.getImgPath());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
			
	}
	
	   // 검색 조건에 맞는 where 절 만들기 (selectCount, selectList 둘 다 같은 조건을 써야 해서 분리)
	   private String buildWhere(String searchType, String keyword) {
	      if(keyword == null || keyword.trim().isEmpty()) {
	         return "";
	      }
	      
	      if("title".equals(searchType)){
	         return " where b.title like ? ";
	      }else if("content".equals(searchType)) {
	         return " where b.content like ? ";
	      }else if("writer".equals(searchType)) {
	         return " where m.nickname like ? ";
	      }
	      return "";
	   }
	   
	   
	   // 검색 조건에 맞는 전체 글 개수
	   public int selectCount(String searchType, String keyword) {
	      
	      String where = buildWhere(searchType, keyword);
	      String sql = "select count(*) from board b join member m on "
	            + " b.writer = m.id" + where;
	      
	      try(Connection conn = DBmanager.getInstance();
	         PreparedStatement pstmt = conn.prepareStatement(sql)){
	         if(!where.isEmpty()) {
	            pstmt.setString(1, "%"+keyword+"%");
	            // % -> 키워드를 포함한 모든 문자열
	         }
	         try(ResultSet rs = pstmt.executeQuery()){
	            if(rs.next()) {
	               return rs.getInt(1);
	            }
	         }
	      }catch (SQLException e) {
	         e.printStackTrace();
	      }
	      return 0;
	   }
	   
	   // 게시글 목록 조회(검색 + 페이징)
	   // startRow 가져올 시작 순번 (1부터)
	   // endRow 가져올 끝 번호
	   // 한 페이지 9개일 때 2페이지 -> startRow=10, endRow=18
	   // int startRow, int endRow 는 서비스에서 넘겨 받아야 함
	   
	   public List<BoardDto> selectList(String searchType, String keyword, int startRow, int endRow){
	      
	      String where = buildWhere(searchType, keyword);
	      
	      Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      
	      // StringBuilder 객체 : 문자열을 담아놓고 내용을 계속 변경할 수 있는 객체. 단일 스레드
	      // StringBuffer : 여러 객체가 동시에 접근할 때에 사용
	      String sql = "select * from (\r\n"
	            + "    select rownum as rn, t.* from(\r\n"
	            + "        select b.bno, b.title, b.writer, m.nickname as writer_nickname,\r\n"
	            + "               to_char(b.reg_date, 'YYYY-MM-DD') as reg_date, b.view_cnt, b.like_cnt, b.img_path\r\n"
	            + "        from board b\r\n"
	            + "        join member m\r\n"
	            + "        on b.writer = m.id\r\n"
	            +          where
	            + "        order by b.bno desc\r\n"
	            + "        ) t where rownum <= ?\r\n"
	            + "    ) where rn >= ?";
	      
	      List<BoardDto> list = new ArrayList<BoardDto>();
	      
	      try {
	    	  conn = DBmanager.getInstance();
	         pstmt = conn.prepareStatement(sql);
	         
	         // ? 대입
	         int idx = 1;
	         if(!where.isEmpty()) {
	            pstmt.setString(idx++, "%"+keyword+"%");
	         }
	         pstmt.setInt(idx++, endRow);
	         pstmt.setInt(idx, startRow);
	         
	         rs = pstmt.executeQuery();
	         
	         while(rs.next()) {
	        	 BoardDto dto = new BoardDto();
	            dto.setBno(rs.getInt("bno"));
	            dto.setTitle(rs.getString("title"));
	            dto.setWriter(rs.getString("writer"));
	            dto.setWriterNickname(rs.getString("writer_nickname"));
	            dto.setReg_date(rs.getString("reg_date"));
	            dto.setViewCnt(rs.getInt("view_cnt"));
	            dto.setLikeCnt(rs.getInt("like_cnt"));
	            dto.setImgPath(rs.getString("img_path"));
	            
	            list.add(dto);
	         }
	      }catch (Exception e) {
	         e.printStackTrace();
	      }finally {
	         try {
	            if(rs != null) rs.close();
	            if(pstmt != null) pstmt.close();
	            if(conn != null) conn.close();
	         }catch (Exception e) {
	            e.printStackTrace();
	         }
	      }
	      return list;
	   }
	
	
public BoardDto selectList1(int bno){
		
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sql = "select b.bno,b.title,b.content,b.writer,m.nickname\r\n"
				+ "    as writer_nickname,to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
				+ "    b.view_cnt,b.like_cnt,b.img_path\r\n"
				+ "from board b\r\n"
				+ "join member m \r\n"
				+ "on b.writer =m.id\r\n"
				+ "where b.bno=? \r\n";
			
		BoardDto dto = null;

		
		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, bno);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				dto = new BoardDto();
				dto.setBno(rs.getInt("bno"));
				dto.setWriter(rs.getString("writer"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setImgPath(rs.getString("img_path"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setContent(rs.getString("content"));

			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			
			}
		}
		return dto;
	}

//view-cnt
	public void increViewCnt(int bno) {
		Connection conn= null;
		PreparedStatement pstmt = null;
		
		String sql = "update board set view_cnt = view_cnt + 1 where bno=?";
		
		try {
			conn = DBmanager.getInstance();
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.executeUpdate(); //실행하세요 ~ 라는 거니까 무조건 코딩에 넣기
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			}
			
		}	

//delete
	public String Delete(int bno,String writer) {
		
		String sql2="select img_path from board where bno=? and writer=?";
		String sql="delete from board where bno=? and writer=?";
		String imgPath = null;
		
		try (Connection conn = DBmanager.getInstance()){
			try(PreparedStatement pstmt = conn.prepareStatement(sql2)) {
			pstmt.setInt(1, bno);
			pstmt.setString(2, writer);
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					imgPath = rs.getString("img_path");
				}else {
					return null;
				}
			}
		}
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, bno);
			pstmt.setString(2, writer);
			pstmt.executeLargeUpdate();
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return imgPath;
	}

public void update(BoardDto dto) {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	
	String sql ="update board set title=?,content=?,img_path=?,mod_date=sysdate "
				+"where bno=? and writer =?";
	
	try {
		conn = DBmanager.getInstance();
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, dto.getTitle());
		pstmt.setString(2, dto.getContent());
		pstmt.setString(3, dto.getImgPath());
		pstmt.setInt(4, dto.getBno());
		pstmt.setString(5, dto.getWriter());
		pstmt.executeUpdate();
	}catch(Exception e ){
		e.printStackTrace();
	}finally {
		try {
			if(pstmt!=null) pstmt.close();
			if(conn!=null) conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

//인기글
	public List<BoardDto> selectPopualrList(){
	
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from(\r\n"
				+ "        select b.bno,b.title,m.nickname as writer_nickname,\r\n"
				+ "        to_char(b.reg_date,'yyyy-mm-dd') as reg_date,\r\n"
				+ "        b.view_cnt,b.like_cnt,b.img_path\r\n"
				+ " from board b\r\n"
				+ " join member m\r\n"
				+ " on b.writer = m.id\r\n"
				+ " order by b.like_cnt desc, b.view_cnt desc,b.bno desc )\r\n"
				+ " where rownum <=6\r\n";
		
		List<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		while(rs.next()) {
			BoardDto dto = new BoardDto();
			dto.setBno(rs.getInt("bno"));
			dto.setTitle(rs.getString("title"));
			dto.setWriterNickname(rs.getString("writer_nickname"));
			dto.setReg_date(rs.getString("reg_date"));
			dto.setViewCnt(rs.getInt("view_cnt"));
			dto.setLikeCnt(rs.getInt("like_cnt"));
			dto.setImgPath(rs.getString("img_path"));
			list.add(dto);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
		try {
			if(rs !=null)rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		}
		return list;
}
	//홈인덱스에서 인기글이 보여지게 하기
	public List<BoardDto> selectPopualrList1(){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from(\r\n"
				+ "        select b.bno,b.title,m.nickname as writer_nickname,\r\n"
				+ "        to_char(b.reg_date,'yyyy-mm-dd') as reg_date,\r\n"
				+ "        b.view_cnt,b.like_cnt,b.img_path\r\n"
				+ " from board b\r\n"
				+ " join member m\r\n"
				+ " on b.writer = m.id\r\n"
				+ " order by b.like_cnt desc, b.view_cnt desc,b.bno desc )\r\n"
				+ " where rownum <=3\r\n";
		
		List<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		while(rs.next()) {
			BoardDto dto = new BoardDto();
			dto.setBno(rs.getInt("bno"));
			dto.setTitle(rs.getString("title"));
			dto.setWriterNickname(rs.getString("writer_nickname"));
			dto.setReg_date(rs.getString("reg_date"));
			dto.setViewCnt(rs.getInt("view_cnt"));
			dto.setLikeCnt(rs.getInt("like_cnt"));
			dto.setImgPath(rs.getString("img_path"));
			list.add(dto);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
		try {
			if(rs !=null)rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		}
		return list;
}
	// 홈인덱스에서 최신글이 보여지게 하기
		public List<BoardDto> selectNewlist() {
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
			String sql ="select * from(select b.bno,b.title,m.nickname as writer_nickname,\r\n"
					+ "        to_char(b.reg_date,'yyyy-mm-dd') as reg_date,b.img_path\r\n"
					+ " from board b\r\n"
					+ " join member m\r\n"
					+ " on b.writer = m.id\r\n"
					+ " order by  b.bno desc)\r\n"
					+ " where rownum <=3\r\n";
		
			List<BoardDto> list = new ArrayList<BoardDto>();
			try {
				conn = DBmanager.getInstance();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDto dto = new BoardDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setImgPath(rs.getString("img_path"));
				list.add(dto);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
			try {
				if(rs !=null)rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
				
			}
			return list;
		
}
		
		//내가 쓴 글
		public List<BoardDto> selectListByWriter(String writer){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
		String sql ="select bno,title,to_char(reg_date,'yyyy-mm-dd') as reg_date,\r\n"
				+ "       view_cnt,like_cnt,img_path\r\n"
				+ " from board\r\n"
				+ " where writer=?\r\n"
				+ " order by bno desc\r\n";
		
		List<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writer);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDto dto = new BoardDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				dto.setImgPath(rs.getString("img_path"));
				list.add(dto);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null)rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
			return list;
			
		}
		
	//좋아요 한 글
		public List<BoardDto> selectLikedList(String memberId){
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
			String sql = " select b.bno,b.title,m.nickname as writer_nickname,b.like_cnt,b.img_path \r\n"
					+ " from board_like l\r\n"
					+ " join board b\r\n"
					+ " on l.bno = b.bno\r\n"
					+ " join member m\r\n"
					+ " on b.writer = m.id\r\n"
					+ " where l.member_id = ?\r\n"
					+ " order by l.like_date desc\r\n";
			
			List<BoardDto> list = new ArrayList<BoardDto>();
			
			try {
				conn = DBmanager.getInstance();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberId);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					BoardDto dto = new BoardDto();
					dto.setBno(rs.getInt("bno"));
					dto.setTitle(rs.getString("title"));
					dto.setWriterNickname(rs.getString("writer_nickname"));
					dto.setLikeCnt(rs.getInt("like_cnt"));
					dto.setImgPath(rs.getString("img_path"));
					list.add(dto);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null)rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				}catch (Exception e) {
					e.printStackTrace();
			  }
			  }
			return list;
		      }
			 
			
		
}//코딩의 전체 
