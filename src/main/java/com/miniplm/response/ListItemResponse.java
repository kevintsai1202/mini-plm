package com.miniplm.response;

import java.io.Serializable;

import com.miniplm.entity.FormData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListItemResponse implements Serializable {
	private String formNumber;
//	private List<FormExtraData> formExtraDatas;
}
