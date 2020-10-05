package com.bit.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bit.dao.ClassDao;
import com.bit.dao.ClassRegDao;
import com.bit.dao.StudentDao;
import com.bit.vo.ClassVo;
import com.bit.vo.ClassregVo;
import com.bit.vo.ScheduleVo;
import com.bit.vo.StudentVo;

public class ClassRegOkAction implements BitAction {
	private int pageSize = 6;
	private int totalCount;
	private int totalPage;
	
	@Override
	public String pro(HttpServletRequest request, HttpServletResponse resonse) throws ServletException, IOException {
		ClassRegDao crdao = ClassRegDao.getInstance();
		ClassDao cdao = ClassDao.getInstence();
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		
		int class_no = Integer.parseInt(request.getParameter("classno"));
		ClassVo c =  cdao.findByNo(class_no);
		String class_name = c.getClass_name(); 
		int std_no = Integer.parseInt(request.getParameter("stdno"));
		
		//����¡
		totalCount = cdao.getTotalCount(null);
		totalPage = (int) Math.ceil((double) totalCount/pageSize);
		session.setAttribute("crtotalPage", totalPage);
		int pageNum=1;
		if(request.getParameter("pageNum") !=null) {
			pageNum=Integer.parseInt(request.getParameter("pageNum"));
		}
					int start = (pageNum-1)*pageSize+1;
		int end = (start+pageSize)-1;
		if(end > totalCount) {
			end=totalCount;
		}

		//������û insert
		int re = crdao.insertClassreg(request, class_no,std_no);
		if(re != 1) {
			request.setAttribute("msg",class_name+" ��û�� �� �����ϴ�.");
			request.setAttribute("color","red");
		}else {
			request.setAttribute("msg",class_name+" ��û�Ǿ����ϴ�.");
			request.setAttribute("color","blue");
			
			//insert ���� �� ������ǥ�ݿ� �õ�..
			ClassVo cv = cdao.findByNo(class_no);
			ArrayList<ScheduleVo> timetable = new ArrayList<ScheduleVo>();
			ScheduleVo sv = null;
			String class_dayofweek = cv.getClass_dayofweek();
			String class_time = cv.getClass_time();
			String day = "";
			String time = "";
			if(class_dayofweek.contains(".")) {
				String []dayofweek = class_dayofweek.split(".");
				switch(class_dayofweek) {
				case "��" : day = "1";break;
				case "ȭ" : day = "2";break;
				case "��" : day = "3";break;
				case "��" : day = "4";break;
				case "��" : day = "5";break;
				case "��" : day = "6";break;
				}
				String []classtime = class_time.split(".");
				for(int i=0; i < dayofweek.length; i++) {
					day=dayofweek[i];							
					time=classtime[i];
					if(time.length() > 1) {
						for(int j=0; i< class_time.length(); i++) {
							time= class_time.substring(j,j+1);
							sv = new ScheduleVo(day,time);
							timetable.add(sv);
						}
					}else {
					sv = new ScheduleVo(day,time);
					timetable.add(sv);
					}
				}
			}else {
				switch(class_dayofweek) {
				case "��" : day = "1";break;
				case "ȭ" : day = "2";break;
				case "��" : day = "3";break;
				case "��" : day = "4";break;
				case "��" : day = "5";break;
				case "��" : day = "6";break;
				}
				
				if(class_time.length() > 1) {
					for(int i=0; i< class_time.length(); i++) {
						time= class_time.substring(i,i+1);
						sv = new ScheduleVo(day,time);
						timetable.add(sv);
					}
				}
			}
			for(ScheduleVo svo : timetable) {
				String finalday = svo.getDay();
				String finaltime = svo.getTime();
				session.setAttribute("day", finalday);
				session.setAttribute("time", finaltime);
			}
			
		}
		
		//������û �ϴ��� ��û����
		ArrayList<ClassregVo> list = new ArrayList<ClassregVo>();
		list = crdao.listAll(std_no);
		session.setAttribute("crList", list);
		
		//��û ����, ����
		int countCredit =0;
		int countSubject =0;
		countCredit = crdao.countCredit(std_no);
		countSubject = crdao.countSubject(std_no);
		session.setAttribute("cCredit", countCredit);
		session.setAttribute("cSubject", countSubject);
		
		return "classreg.jsp";
	}

}
