package com.bit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.bit.action.ListBoardAction;
import com.bit.db.ConnectionProvider;
import com.bit.vo.BoardVo;
import com.bit.vo.ReplyVo;

public class BoardDao {

	// �����ϴ� �޼ҵ�
	public int updateBoard(BoardVo b_vo) {
		System.out.println(b_vo.getBoard_title() + "|" + b_vo.getBoard_content() + "|" + b_vo.getBoard_fname()
				+ b_vo.getBoard_no());
		int re = -1;
		try {
			String sql = "update board set board_title=?, board_content=?, board_fname=? where board_no=?";
//			if(!b_vo.getBoard_pwd().equals(null)) {
//				sql += "and board_pwd="+b_vo.getBoard_pwd();
//			}
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b_vo.getBoard_title());
			pstmt.setString(2, b_vo.getBoard_content());
			pstmt.setString(3, b_vo.getBoard_fname());
			pstmt.setInt(4, b_vo.getBoard_no());

			re = pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt, null);
		} catch (Exception e) {
			System.out.println("BoardDao.updateBoard ���ܹ߼�:" + e.getMessage());
		}
		return re;
	}

	// �����ϴ� �޼ҵ�
	public int deleteBoard(int board_no) {
		int re = -1;
		try {
			String sql = "delete board where board_no=" + board_no;
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			re = stmt.executeUpdate(sql);
			ConnectionProvider.close(conn, stmt, null);
		} catch (Exception e) {
			System.out.println("BoardDao.deleteBoard ���ܹ߼�:" + e.getMessage());
		}
		return re;
	}

	// ��ȸ�� �����ϴ� �޼ҵ�
	public void increaseHit(int board_no) {
		try {
			String sql = "update board set board_hit=board_hit+1 where board_no=?";
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_no);
			pstmt.execute();
			ConnectionProvider.close(conn, pstmt, null);
		} catch (Exception e) {
			System.out.println("increaseHit���ܹ߻�:" + e.getMessage());
		}
	}

	// �󼼺��⸦ ���� �޼ҵ�(�� ����)
	public BoardVo getBoard(int board_no) {
		BoardVo b_vo = null;
		try {
			String sql = "select * from board where board_no=?";
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_no);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				b_vo = new BoardVo(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getDate(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getInt(10));

			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("���ܹ߻�:" + e.getMessage());
		}
		return b_vo;
	}

	// board_boardno(���� �Խ���)���ÿ� ���� ��� �Խ��� �з��� �ҷ����� �޼ҵ�
	public ArrayList<String> getBoardCategory(int board_boardno) {
		ArrayList<String> list = new ArrayList<String>();

		String sql = "select board_category from board where board_boardno=" + board_boardno;
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (list.contains(rs.getNString(1))) {
					continue;
				}
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println("BoardDao.getBoardCategory����:" + e.getMessage());
		}
		return list;
	}

	// ��� �Խù� ����� ��ȯ�ϴ� �޼ҵ带 ����
	public ArrayList<BoardVo> listAll(int board_boardno, String board_category, int page_num, String search,
			String keyword) {
		ArrayList<BoardVo> list = new ArrayList<BoardVo>();
		int end = page_num * ListBoardAction.page_size;
		int start = end - (ListBoardAction.page_size - 1);
		System.out.println("listAll.end =" + end);
		System.out.println("listAll.start=" + start);

		// ���� �Խ��� ���� �з� �Խ��� ����Ʈ�� �̾ƿ��� sql��
		String sql = "select n, board_no,board_boardno,board_category,board_title,board_content,"
				+ "board_regdate,board_hit,board_pwd,board_fname,std_no from"
				+ "(select rownum n, board_no,board_boardno,board_category,board_title,"
				+ "board_content,board_regdate,board_hit,board_pwd,board_fname,std_no"
				+ " from (select * from board order by board_no desc, board_regdate desc)"
				+ "where board_category like '%" + board_category + "%'" + " and board_boardno=" + board_boardno;

		if (search != null && keyword != null && !keyword.equals("")) {
			sql += " and " + keyword + " like '%" + search + "%'";

		}

		sql += ")where n >=" + start + " and n <= " + end;

		System.out.println(sql);

		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(new BoardVo(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getString(9), rs.getString(10),
						rs.getInt(11)));
			}
			ConnectionProvider.close(conn, stmt, rs);

		} catch (Exception e) {
			System.out.println("BoardDao.listAll���ܹ߻�:" + e.getMessage());
		}

		return list;
	}

	// ������ �Խ����� ��� �Խù� ���� ��ȯ���ִ� �޼ҵ�
	public int getBoardCount(int board_boardno, String board_category, String search, String keyword) {
		int n = 0;
		String sql = "select count(*) from board where board_boardno=" + board_boardno + " and board_category='"
				+ board_category;

		if (search != null && keyword != null && !keyword.equals("")) {
			sql += " and " + keyword + " like '%" + search + "%'";

		}
		sql += "' order by board_no";
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				n = rs.getInt(1);
			}
			ConnectionProvider.close(conn, stmt, rs);
		} catch (Exception e) {
			System.out.println("BoardDao.getBoardCount���ܹ߻�:" + e.getMessage());
		}

		return n;
	}

	// ���ο� �۹�ȣ�� ��ȯ�ϴ� �޼ҵ带 ����
	public int getNextNo() {
		int n = 0;
		String sql = "select nvl(max(board_no),0)+1 from board";
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				n = rs.getInt(1);
			}

			ConnectionProvider.close(conn, stmt, rs);
		} catch (Exception e) {
			System.out.println("���ܹ߻�:" + e.getMessage());
		}
		return n;
	}

	// �۵���� ���� �޼ҵ�
	public int insertBoard(BoardVo b) {
		int re = -1;
		System.out.println(b.getBoard_no() + "" + b.getBoard_boardno() + "" + b.getBoard_category() + ""
				+ b.getBoard_title() + "" + b.getBoard_content() + "" + b.getBoard_regdate() + "" + b.getBoard_hit()
				+ "" + b.getBoard_pwd() + "" + b.getBoard_fname() + "" + b.getStd_no());

		String sql = "insert into board values(?,?,?,?,?,sysdate,0,?,?,?)";
		System.out.println("������ ���� 1");
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			System.out.println("������ ���� 2");
			pstmt.setInt(1, b.getBoard_no());
			pstmt.setInt(2, b.getBoard_boardno());
			pstmt.setString(3, b.getBoard_category());
			pstmt.setString(4, b.getBoard_title());
			pstmt.setString(5, b.getBoard_content());
			pstmt.setString(6, b.getBoard_pwd());
			pstmt.setString(7, b.getBoard_fname());
			pstmt.setInt(8, b.getStd_no());
			System.out.println("������ ���� 3");
			re = pstmt.executeUpdate();
			System.out.println("������ ���� 4");
			System.out.println("re�� " + re);
			ConnectionProvider.close(conn, pstmt, null);
		} catch (Exception e) {
			System.out.println("BoardDao.insertBoard���ܹ߻�:" + e.getMessage());
		}
		return re;
	}
	
	public String getName(int std_no) {
		String name = null;
		String sql = "select STD_NAME from student where STD_NO=?";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, std_no);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				name = rs.getString(1);
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("BoardDao.getName���ܹ߻�:" + e.getMessage());
		}
		return name;
	}
}