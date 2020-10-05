package com.bit.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bit.dao.BoardDao;

public class ListBoardAction implements BitAction {

	public BoardDao dao;
	public static int board_boardno=100;
	public String board_category = "��������";
	public String board_boardname ="�˸�����";
	public static int board_count = 0;
	public static int page_size = 5;	
	public static int total_page = 1;
	public static int page_max = 5;	

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		HttpSession session = request.getSession();
		
//		session.setAttribute("std_no", "2014104199");
		
		int page_num = 1;
		if(session.getAttribute("page_size")!=null) {
			page_size= (int) session.getAttribute("page_size");
		}

		if(request.getParameter("page_size")!=null) {
			page_size = Integer.parseInt(request.getParameter("page_size"));
			session.setAttribute("page_size", page_size);
		}

		if(request.getParameter("page_num")!= null) {
			page_num = Integer.parseInt(request.getParameter("page_num"));
			session.setAttribute("page_num", page_num);
		}

		System.out.println("page_num��"+page_num);
		//-----------------------------------------------------------------------------------------------------------------------------
		//�˻��� ���� search�� keyword����
		String search=null;
		String keyword=null;
		dao = new BoardDao();

		if((request.getParameter("board_boardno"))!=null) {

			search="";
			keyword="";
			board_boardno = Integer.parseInt(request.getParameter("board_boardno"));
			System.out.println("board_boardno�� " + board_boardno);
			switch (board_boardno) {
			case 100: board_boardname = "�˸�����"; board_category = "��������"; break;
			case 200: board_boardname = "���򸶴�";  board_category = "�ü�QNA"; break;
			case 300: board_boardname = "��������"; board_category = "��ϴ�"; break;
			}
			session.setAttribute("board_boardno", board_boardno);

		}
		if(session.getAttribute("board_boardno")!=null) {
			session.getAttribute("board_boardno");
		}

		//-------------------------------------------------------------------------------------------

		//ī�װ� ���� ��û�޾��� ���, ������ ����
		  if (board_category == null && session.getAttribute("board_category") != null
	                && request.getParameter("board_boardno") == null) {
	            board_category = URLDecoder.decode(request.getParameter("board_category"), "UTF-8");
	        }
	        if (request.getParameter("board_category") != null) {
	            board_category = URLDecoder.decode(request.getParameter("board_category"), "UTF-8");
	            session.setAttribute("board_category", board_category);
	        }
		
		//--------------------------------------------------------------------------------------

		if(session.getAttribute("search")!=null) {
			search = (String)session.getAttribute("search");
			keyword = (String)session.getAttribute("keyword"); 
		}

		if(request.getParameter("search")!=null) {
			search = request.getParameter("search");
			keyword = request.getParameter("keyword");
			session.setAttribute("search", search);
			session.setAttribute(keyword, "keyword");
		}

		System.out.println("board_category�� "+ board_category);

		//board_count������ �ش� �Խ����� ��� �Խù� ���� ��ȯ�� ���� ����
		board_count=dao.getBoardCount(board_boardno, board_category, search, keyword);
		
		//------------------------------------------------------------------------------------
		total_page = (int)Math.ceil((double)board_count/page_size);
		if(total_page==0) {
			total_page=1;
		}
		int start_page = (page_num-1)/page_max*page_max+1;
		int end_page = start_page+page_max-1;
		if(end_page > total_page) {
			end_page = total_page;
		}		


		//--------------------------------------------------------------------------------------
		String page_str = "";
		if(start_page > 1) {
			page_str += "<a href='listBoard.do?page_num="+(start_page-1)+"'>[����]</a>  ";
		}

		for(int i=start_page;i<=end_page;i++) {
			page_str += "<a href='listBoard.do?page_num="+i+"'>"+ i + "</a> ";
		}

		if(total_page > end_page) {
			page_str += "  <a href='listBoard.do?page_num="+(end_page+1)+"'>[����]</a>";
		}

		System.out.println(page_size+""+board_count+""+total_page+""+page_max);

		request.setAttribute("board_boardno", board_boardno);
		request.setAttribute("page_str", page_str);
		request.setAttribute("board_boardname", board_boardname);
		request.setAttribute("category_list", dao.getBoardCategory(board_boardno));
		request.setAttribute("list", dao.listAll(board_boardno, board_category, page_num, search, keyword));
		
		return "listBoard.jsp";
	}
}


