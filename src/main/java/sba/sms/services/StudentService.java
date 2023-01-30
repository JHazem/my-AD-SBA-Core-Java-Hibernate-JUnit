package sba.sms.services;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import lombok.extern.java.Log;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;
@Log
public class StudentService implements StudentI {
	private static final CourseService courseService = new CourseService();
	@Override
	public List<Student> getAllStudents() {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		List<Student> studentList = new ArrayList<>();
		try {
			trans = s.beginTransaction();
			Query<Student> q = s.createQuery("slect * from student", Student.class);
			studentList = q.getResultList();
			trans.commit();
		} catch (HibernateException ex) {
			if (trans != null)
				trans.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
		return studentList;
	}
	@Override
	public void createStudent(Student student) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		try {
			trans = s.beginTransaction();
			s.persist(student);
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
	public Student getStudentByEmail(String email) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		Student student = null;
		try {
			trans = s.beginTransaction();
			Query<Student> q = s.createQuery("from student where email = :email", Student.class);
			q.setParameter("email", email);
			student = q.getSingleResult();
			trans.commit();
		} catch (Exception ex) {
			if (trans != null)
				trans.rollback();
			ex.printStackTrace();
		} finally {
			s.close();
		}
		return student;
	}
	@Override
	public boolean validateStudent(String email, String password) {
		Student s = getStudentByEmail(email);
		return s != null && s.getPassword().equals(password);
	}
	@Override
	public void registerStudentToCourse(String email, int courseId) {
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans = null;
		try {
			trans = s.beginTransaction();
			Student student = getStudentByEmail(email);
			student.addCourse(courseService.getCourseById(courseId));
			s.merge(student);
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
	public List<Course> getStudentCourses(String email) {
		List<Course> courseList = null;
		Student stud = new Student();
		Transaction trans = null;
		Session s = HibernateUtil.getSessionFactory().openSession();
		try {
			trans = s.beginTransaction();
			stud = s.createQuery("from student where email = :email", Student.class).setParameter("email", email)
					.getSingleResult();
			courseList = stud.getCourses();
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




