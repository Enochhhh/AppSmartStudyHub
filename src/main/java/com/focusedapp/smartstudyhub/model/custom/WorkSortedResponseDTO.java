package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class WorkSortedResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Object key;
	private List<WorkDTO> worksSorted;

}
