package com.miniplm.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApproversRequest {
	List<String> approverIds;
}
