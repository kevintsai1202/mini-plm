package com.miniplm.formprops;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MultipleProps implements Serializable{
	private String mode = "multiple";
}
