package com.bit.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bit.dao.BitDao;
import com.bit.vo.StudentVo;

public class StartEndDateAction implements BitAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int std_no = (Integer) request.getSession().getAttribute("login");
		BitDao dao = new BitDao();
		StudentVo sv2 = dao.getStartEndDate(std_no);
		request.setAttribute("sv2", sv2);
		return "startEndDate.jsp";
	}

}
