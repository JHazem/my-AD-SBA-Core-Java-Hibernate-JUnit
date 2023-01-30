package sba.sms.services;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

public class CourseService implements CourseI {

	@Override
	public void createCourse(Course course) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		try {
			trans = s.beginTransaction();
			s.persist(course);
			trans.commit();
		} catch (HibernateException ex) {
			if (trans != null)
				trans.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
	}

	@Override
	public Course getCourseById(int courseId) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		Course course = new Course();
		try {

			trans = s.beginTransaction();
			Query<Course> q = s.createQuery("From Course where id = :id", Course.class);
			q.setParameter("id", courseId);
			course = q.getSingleResult();
			trans.commit();
		} catch (HibernateException ex) {
			if (trans != null)
				trans.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
		return course;
	}

	@Override
	public List<Course> getAllCourses() {

		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		List<Course> courseList = new ArrayList<>();
		try {
			trans = s.beginTransaction();
			Query<Course> q = s.createQuery("From Course ", Course.class);
			courseList = q.getResultList();
			trans.commit();
		} catch (HibernateException ex) {
			if (trans != null)
				trans.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
		return courseList;
	}
}
