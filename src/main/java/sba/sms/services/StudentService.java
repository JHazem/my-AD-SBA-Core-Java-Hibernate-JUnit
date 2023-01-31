package sba.sms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
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
	Transaction ts = null;
	List<Student> studentList = new ArrayList<>();
	
	try {
		ts = s.beginTransaction();
		Query<Student> q = s.createQuery("From Student" , Student.class);
		studentList = q.getResultList();
		ts.commit();
	} catch (HibernateException ex) {
		if(ts != null) ts.rollback();
		ex.printStackTrace();
	} finally {
		s.close();
	}
	return studentList;
}

@Override
public void createStudent(Student student) {
	Session s = HibernateUtil.getSessionFactory().openSession();
	Transaction ts = null;
	
	try {
		ts = s.beginTransaction();
		s.persist(student);
		ts.commit();
	} catch (HibernateException ex) {
		if(ts != null) ts.rollback();
		ex.printStackTrace();
	} finally {
		s.close();
	}
}

@Override
public Student getStudentByEmail(String email) {
	Session s = HibernateUtil.getSessionFactory().openSession();
	Transaction ts = null;
	Student student = null;
	
	try {
		ts = s.beginTransaction();
		Query<Student> q = s.createQuery("From Student where email = :email", Student.class);
		q.setParameter("email", email);
		student = q.getSingleResult();
		ts.commit();
	} catch (Exception ex) {
		if(ts != null) ts.rollback();
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
	Transaction ts = null;
	
	try {
		ts = s.beginTransaction();
		Student student = getStudentByEmail(email);
		student.addCourse(courseService.getCourseById(courseId));
		s.merge(student);
		ts.commit();
	} catch (HibernateException ex) {
		if (ts != null) ts.rollback();
		ex.printStackTrace();
	} finally {
		s.close();
	}
}

@Override
public Set<Course> getStudentCourses(String email) {
	Set<Course> courseList = null; 
	Student stud = new Student();
	Transaction ts = null;
	Session s = HibernateUtil.getSessionFactory().openSession();
	
	try {
		ts = s.beginTransaction();
		stud = s.createQuery("From Student where email = :email", Student.class).setParameter("email",email).getSingleResult();
		courseList = stud.getCourses();
	    ts.commit();
	} catch (HibernateException ex) {
		if(ts != null) ts.rollback();
		ex.printStackTrace();
	} finally {
		s.close();
	}
	return courseList;
}
}