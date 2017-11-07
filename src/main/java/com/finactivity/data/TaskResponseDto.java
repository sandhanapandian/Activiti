package  com.finactivity.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter @Setter
	int totalRecords;
	
	@Getter @Setter
	List<TaskDto> taskList = new ArrayList<TaskDto>();

}
