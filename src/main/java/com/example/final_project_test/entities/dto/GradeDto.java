package com.example.final_project_test.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.example.final_project_test.entities.enums.EGradeType;
import com.example.final_project_test.entities.enums.EGradeValue;

public class GradeDto {
	
		@Enumerated(EnumType.STRING)
		@NotNull(message = "Grade value must not be null.")
		private EGradeValue value;

		@Enumerated(EnumType.STRING)
		@NotNull(message = "Grade type must not be null.")
		private EGradeType type;

		public GradeDto() {
			super();
		}

		public EGradeValue getValue() {
			return value;
		}

		public void setValue(EGradeValue value) {
			this.value = value;
		}

		public EGradeType getType() {
			return type;
		}

		public void setType(EGradeType type) {
			this.type = type;
		}
		

}
