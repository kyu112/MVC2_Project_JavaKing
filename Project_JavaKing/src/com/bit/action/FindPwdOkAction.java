package com.bit.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bit.dao.BitDao;

public class FindPwdOkAction implements BitAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���ڵ�
		request.setCharacterEncoding("UTF-8");

		// Dao ����
		BitDao dao = new BitDao();

		// findId.jsp ���� ���̵�� �̸����� �޾ƿ�
		int id = Integer.parseInt(request.getParameter("id"));
		String email = request.getParameter("email");

		// pwd �� ������
		String pwd = dao.getStudentPwd(id, email);

		// �̸��̳� �̸����� Ʋ����� �����޽���, �Ѵ� ���� ��� pwd �� �����Ѵ�.
		if (pwd != null && !pwd.equals("")) {
			request.setAttribute("findpwd", pwd);
		} else {
			request.setAttribute("findpwd", "��ϵ��� ���� ���̵� �Ǵ� �̸����Դϴ�.");
		}
		return "findPwdOk.jsp";
	}
}
