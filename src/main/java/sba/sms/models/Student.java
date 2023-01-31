package sba.sms.models;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "student")
@Entity
public class Student {
	@NonNull
	@Id @Column(columnDefinition = "varchar(50)", name = "email")
    String email;
	@NonNull
	@Column(columnDefinition = "varchar(50)", name = "name")
	String name;
	@NonNull
	@Column(columnDefinition = "varchar(50)", name = "password")
	String password;
	
	@ToString.Exclude
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
	@JoinTable(name = "student_courses",joinColumns = @JoinColumn(name = "student_email"),inverseJoinColumns = @JoinColumn(name = "courses_id"))
	Set<Course> courses = new LinkedHashSet<>();
	
	public void addCourse(Course c) {
		courses.add(c);
		c.getStudents().add(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) 
			return true;
		if(obj == null) 
			return false ;
			if(getClass() != obj.getClass()) 
			return false;
		Student student = (Student) obj;
		return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email,name,password);
	}
}