package com.jastt.utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		String email = value.toString();
		email = email.trim();
		Pattern pattern = Pattern.compile("[a-zA-Z]+[-0-9a-zA-Z._]+@[a-zA-Z]+[-0-9a-zA-Z._]+\\.[a-zA-Z]{2,4}");
		Matcher matcher = pattern.matcher(email);
        
        boolean matchFound = matcher.matches();
        
        if (!matchFound) {
            FacesMessage message = new FacesMessage();
			String messageBundle = context.getApplication().getMessageBundle();
			Locale locale = context.getViewRoot().getLocale();
			ResourceBundle bundle = ResourceBundle.getBundle(messageBundle, locale);
            message.setDetail(bundle.getString("emailValidation_detail"));
            message.setSummary(bundle.getString("emailValidation"));
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(message);
        }
	}

}

