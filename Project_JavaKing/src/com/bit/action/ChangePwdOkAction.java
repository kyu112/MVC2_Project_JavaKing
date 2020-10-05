package com.bit.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bit.dao.BitDao;

public class ChangePwdOkAction implements BitAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���ڵ�
		request.setCharacterEncoding("UTF-8");

		// Dao ����
		BitDao dao = new BitDao();

		// �������� ����. �⺻���� ���з� ġ�� ��й�ȣ ������������ �̵���Ŵ
		String view = "changePwd.jsp";

		// ��й�ȣ ���� ���������� ������ �� ����.
		String msg = null;

		// findId.jsp ���� ���� ��й�ȣ, ���� ��й�ȣ, ��й�ȣ Ȯ���� �޾ƿ�
		String old_pwd = request.getParameter("old_pwd");
		String new_pwd = request.getParameter("new_pwd");
		String new_pwd_check = request.getParameter("new_pwd_check");
		
		// ��й�ȣ�� ��й�ȣ Ȯ�� ��ġ���� üũ.
		if (!new_pwd.equals(new_pwd_check)) {
			msg = "��й�ȣ Ȯ���� ���� �ʽ��ϴ�.";
		} else {
			// id�� ������
			int id = (Integer) request.getSession().getAttribute("login");

			// pwd ����
			int re = dao.updatePwd(id, old_pwd, new_pwd);

			if (re <= 0) {
				msg = "���� ��й�ȣ�� ���� �ʽ��ϴ�.";
			} else {
				view = "main.do";
			}
		}

		// ���н� ������ �޽���
		request.setAttribute("change_pwd_error", msg);

		return view;
	}
}

