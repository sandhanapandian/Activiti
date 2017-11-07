package  com.finactivity.data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter @Setter
	String taskId;

	@Getter @Setter
	String processInstanceId;
	
	@Getter @Setter
	boolean claimStatus;
	
	/**Process creation Time  */
	@Getter @Setter
	Date creationTime;

	@Getter @Setter
	String taskName;

	@Getter @Setter
	String category;

	@Getter @Setter
	Long id;

	@Getter @Setter
	String referenceNo;
	
}
