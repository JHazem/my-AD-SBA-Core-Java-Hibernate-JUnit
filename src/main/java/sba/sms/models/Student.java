package sba.sms.models;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student {
	@Id
	@NonNull
	@Column(length = 50, name = "email")
	private String email;

	@NonNull
	@Column(length = 50, name = "name")
	private String name;

	@NonNull
	@Column(length = 50, name = "password")
	private String password;

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.DETACH })
	@JoinTable(name = "student_courses", joinColumns = { @JoinColumn(name = "student_email") }, inverseJoinColumns = {
			@JoinColumn(name = "courses_id") })
	private List<Course> courses;

	public void addCourse(Course course) {
		course.getStudents().add(this);
		this.courses.add(course);
	}

	@Override
	public boolean equals(Object s) {
		if (this == s)
			return true;
		if (!(s instanceof Student) || s == null)
			return false;
		Student student = (Student) s;
		return name.equals(student.name) && email.equals(student.email) && password.equals(student.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, email, password);
	}
}
