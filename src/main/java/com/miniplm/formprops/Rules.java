package com.miniplm.formprops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.response.UserFormFieldResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

class RuleRequired{
	public boolean required = true;
	public String message = "此項為必填";
}

@AllArgsConstructor
class RulePatten{
	public String pattern;
	public String message;
}


@Data
@NoArgsConstructor
public class Rules {
	private List rules = new ArrayList();
	
	public Rules(ConfigFormField field, boolean stepRequired) {
		if (stepRequired)
			rules.add(new RuleRequired());
		else if (field.getRequired()) {
			rules.add(new RuleRequired());
		}
		if ((field.getPattern() != null) && (field.getPattern().length() > 0)) {
			rules.add(new RulePatten(field.getPattern(), "輸入內容不符合:"+field.getPatternMsg()));
		}
	}
	
}
