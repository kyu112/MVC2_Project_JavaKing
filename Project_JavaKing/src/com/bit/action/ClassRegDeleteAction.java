package com.bit.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bit.dao.ClassDao;
import com.bit.dao.ClassRegDao;
import com.bit.vo.ClassVo;
import com.bit.vo.ClassregVo;
import com.bit.vo.StudentVo;

public class ClassRegDeleteAction implements BitAction {
	private int pageSize = 6;
	private int totalCount;
	private int totalPage;
	private ClassRegDao crdao;
	
	@Override
	public String pro(HttpServletRequest request, HttpServletResponse resonse) throws ServletException, IOException {
		int classreg_no = Integer.parseInt(request.getParameter("classregno"));
		crdao = ClassRegDao.getInstance();
		ClassDao cdao = ClassDao.getInstence();
		HttpSession session = request.getSession();
		int std_no = Integer.parseInt(request.getParameter("stdno"));
		
		int re = crdao.deleteClassreg(classreg_no);
		if(re > 0 ) {
			request.setAttribute("msg", "������ �����߽��ϴ�.");
			request.setAttribute("color","blue");
		}else {
			request.setAttribute("msg", "������ �����߽��ϴ�.");
			request.setAttribute("color","red");
		}
		
		//��ġ����
		totalCount = cdao.getTotalCount(null);
		totalPage = (int) Math.ceil((double) totalCount/pageSize);
		session.setAttribute("totalPage", totalPage);
		int pageNum=1;
		if(request.getParameter("pageNum") !=null) {
			pageNum=Integer.parseInt(request.getParameter("pageNum"));
		}
					int start = (pageNum-1)*pageSize+1;
		int end = (start+pageSize)-1;
		if(end > totalCount) {
			end=totalCount;
		}
		ArrayList<ClassVo> list2 = cdao.listClass(start, end, null); //��ü���̱�
		session.setAttribute("searchList", list2);
		

		//��û ����, ����
		int countCredit =0;
		int countSubject =0;
		countCredit = crdao.countCredit(std_no);
		countSubject = crdao.countSubject(std_no);
		session.setAttribute("cCredit", countCredit);
		session.setAttribute("cSubject", countSubject);
		
		//������û �ϴ��� ��û����
		ArrayList<ClassregVo> list = new ArrayList<ClassregVo>();
		list = crdao.listAll(std_no);
		session.setAttribute("crList", list);

		return "classreg.jsp";
	}

}
