package com.bit.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bit.dao.BitDao;

public class FindIdOkAction implements BitAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���ڵ�
		request.setCharacterEncoding("UTF-8");

		// Dao ����
		BitDao dao = new BitDao();

		// findId.jsp ���� �̸��� �̸����� �޾ƿ�
		String name = request.getParameter("name");
		String email = request.getParameter("email");

		// id�� ������
		String id = dao.getStudentId(name, email) + "";

		// �̸��̳� �̸����� Ʋ����� �����޽���, �Ѵ� ���� ��� id �� �����Ѵ�.
		if (id.equals("-1")) {
			request.setAttribute("findid", "��ϵ��� ���� �̸� �Ǵ� �̸����Դϴ�.");
		} else {
			request.setAttribute("findid", id);
		}
		return "findIdOk.jsp";
	}
}
