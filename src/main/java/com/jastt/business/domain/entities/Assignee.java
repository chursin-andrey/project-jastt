// default package
// Generated 31.10.2014 19:02:31 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Assignee generated by hbm2java
 */
@Entity
@Table(name = "ASSIGNEE", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = @UniqueConstraint(columnNames = "EMAIL"))
public class Assignee implements java.io.Serializable {

	private Integer id;
	private String name;
	private String email;
	private Set<Issue> issues = new HashSet<Issue>(0);

	public Assignee() {
	}

	public Assignee(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Assignee(String name, String email, Set<Issue> issues) {
		this.name = name;
		this.email = email;
		this.issues = issues;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EMAIL", unique = true, nullable = false, length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignee")
	public Set<Issue> getIssues() {
		return this.issues;
	}

	public void setIssues(Set<Issue> issues) {
		this.issues = issues;
	}

}
