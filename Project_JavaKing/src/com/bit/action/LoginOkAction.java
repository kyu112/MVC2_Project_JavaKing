package com.bit.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bit.dao.BitDao;
import com.bit.dao.StudentDao;
import com.bit.vo.StudentVo;

public class LoginOkAction implements BitAction {

    @Override
    public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Dao ����
    	BitDao dao = new BitDao();
    	 
    	// ���ڵ�
    	request.setCharacterEncoding("UTF-8");
    	
    	// login.jsp ���� id �� ������
        int id = Integer.parseInt(request.getParameter("id"));
        
        // ���̵� ���� üũ ���� Ȯ��. �⺻���� üũ���� �����ɷ� ��
        String remember_id = "notChecked";
        if (request.getParameter("cb_id") != null) {
        	remember_id = request.getParameter("cb_id");
        }
        
        // ���̵� ���� üũ�� ��� login.jsp ���� ����� id ���� ��ȯ����
        if (remember_id.equals("checked")) {
        	request.getSession().setAttribute("re_id", id);
        }
        
        // login.jsp ���� pwd �� ������
        String pwd = request.getParameter("pwd");
       
        // �������� �⺻���� login.jsp �� ��. �α��� �����ߴٰ� ħ
        String view = "login.jsp";
        
        // �α��� ���н� ����� �����޽���
        String msg = null;
        
        // �α��� ���� ���� üũ. ���� 1 �ϰ�� ����. �ʿ��� ��� ������ ���ǿ� ��� main ���������� ����
        int check = dao.login(id, pwd);

        if (check == 1) {
        	String name = dao.getStudentInfo(id).getStd_name();
            int level = dao.getStudentInfo(id).getStd_level();
            int semester = dao.getStudentInfo(id).getStd_semester();
            String major = dao.getStudentInfo(id).getStd_major();
            String email = dao.getStudentInfo(id).getStd_email();
            request.getSession().setAttribute("login", id);
            request.getSession().setAttribute("name", name);
            request.getSession().setAttribute("level", level);
            request.getSession().setAttribute("semester", semester);
            request.getSession().setAttribute("major", major);
            request.getSession().setAttribute("email", email);
            view = "login/main.do";
        } else {
        	msg = "�α��ο� �����Ͽ����ϴ�.";
        }
        // �����޽��� ����
        request.setAttribute("login_error", msg);
        return view;
    }

}

