package com.bit.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bit.dao.GradeDao;
import com.bit.vo.GradeVo;

public class ListGradeAction implements BitAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		GradeDao dao= new GradeDao();
		//---------------------------------------------------------------------------------
		int std_no = (Integer) session.getAttribute("std_no");
		ArrayList<GradeVo> list = dao.listAll(std_no);
		//---------------------------------------------------------------------------------
		request.setAttribute("list", list);
		//�󼼼����� detailGrade�� �̵��� ��츦 �����
		//listGrade���� �ҷ��� list�� ������ ����
		session.setAttribute("list", list);
		return "listGrade.jsp";
	}

}
