package net.frcdb.api.award;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author tim
 */
@Entity
public class Award {

	private String name;
	
	@Id
	private Long id;

	public Award() {

	}

	public Award(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
