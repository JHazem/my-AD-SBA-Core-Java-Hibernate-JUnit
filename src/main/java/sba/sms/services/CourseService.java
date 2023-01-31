package sba.sms.services;

import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;



public class CourseService implements CourseI {
	@Override
	public void createCourse(Course course) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction ts = null;
		
		try {
			ts = s.beginTransaction();
			s.persist(course);
			ts.commit();
		} catch (HibernateException ex) {
			if(ts != null) 
				ts.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
	}
	
	@Override
	public Course getCourseById(int courseId) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction ts = null;
		Course course = new Course();
		
		try {
			ts = s.beginTransaction();
			Query<Course> q = s.createQuery("From Course where id = :id", Course.class);
			q.setParameter("id",courseId);
			course = q.getSingleResult();
			ts.commit();
		} catch (HibernateException ex) {
			if(ts != null)
				ts.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
		return course;
	}
	
	@Override
	public List<Course> getAllCourses() {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction ts = null;
		List<Course> courseList = new ArrayList<>();
		
		try {
			ts = s.beginTransaction();
			Query<Course> q = s.createQuery("From Course ", Course.class);
			courseList = q.getResultList();
			ts.commit();
		} catch (HibernateException ex) {
			if(ts != null) 
				ts.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
		return courseList;
	}

}