package model;

import java.awt.CardLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBmanager;

public class ReplyDao {

	public int insert(ReplyDto dto) {
		String sql = "INSERT INTO reply (rno, bno, writer, content) " + "VALUES (seq_reply.nextval, ?, ?, ?)";

		String seqSql = "SELECT seq_reply.currval FROM dual";

		try (Connection conn = DBmanager.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, dto.getBno());
			pstmt.setString(2, dto.getWriter());
			pstmt.setString(3, dto.getContent());

			int count = pstmt.executeUpdate();

			if (count == 0) {
				return -1;
			}

			try (PreparedStatement pstmt2 = conn.prepareStatement(seqSql); ResultSet rs = pstmt2.executeQuery()) {

				if (rs.next()) {
					return rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public List<ReplyDto> selectList(int bno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT r.rno, r.bno, r.writer, m.nickname, "
		           + "r.content, TO_CHAR(r.reg_date, 'YYYY-MM-DD') AS reg_date "
		           + "FROM reply r "
		           + "JOIN member m ON r.writer = m.id "
		           + "WHERE r.bno = ? "
		           + "ORDER BY r.rno ASC";

		List<ReplyDto> list = new ArrayList<ReplyDto>();

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReplyDto dto = new ReplyDto();
				dto.setBno(rs.getInt("bno"));
				dto.setContent(rs.getString("content"));
				dto.setNickname(rs.getString("nickname"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setRno(rs.getInt("rno"));
				dto.setWriter(rs.getString("writer"));
				list.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return list;

	}

	//댓글 삭제 
	public int delete(int rno ,String writer) {
		
		String sql ="delete from reply where rno=? and writer= ?";
		
		int result =0;
		
		try(Connection conn = DBmanager.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, rno);
			pstmt.setString(2, writer);
			result = pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
//댓글 수정 
	public int update(int rno,String content,String writer) {
		
	Connection conn =null;
	PreparedStatement pstmt = null;
	
	String sql = "update reply set content =? where rno=? and writer =? ";
	
	int result =0;
	
	try {
		conn = DBmanager.getInstance();
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, content);
		pstmt.setInt(2, rno);
		pstmt.setString(3, writer);
	}catch (Exception e) {
		e.printStackTrace();
	}finally {
		try {
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	return result;
	}
	
	//내가 쓴 댓글 
	public List<ReplyDto> selectListBywriter(String writer){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select r.rno,r.bno,b.title as board_title,r.content,\r\n"
				+ "      to_char(r.reg_date,'yyyy-mm-dd') as reg_date\r\n"
				+ " from reply r\r\n"
				+ " join board b\r\n"
				+ " on r.bno = b.bno\r\n"
				+ " where r.writer = ?\r\n"
				+ " order by r.bno desc\r\n";
	
		List<ReplyDto> list = new ArrayList<ReplyDto>();
		
	try {
		conn = DBmanager.getInstance();
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, writer);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			ReplyDto dto = new ReplyDto();
			
			dto.setRno(rs.getInt("rno"));
			dto.setBno(rs.getInt("bno"));
			dto.setBoardTitle(rs.getString("board_title"));
			dto.setContent(rs.getString("content"));
			dto.setReg_date(rs.getString("reg_date"));
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

	
}//Dao전체 끝 
