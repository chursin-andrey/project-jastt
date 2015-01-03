package com.jastt.frontend.utils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class Dialogs {

	public static final String NEW_USER_DIALOG_ID = "new_user_dialog";
	public static final String NEW_USER_DIALOG_WIDGET = "newUserDialog";
	public static final String EDIT_USER_DIALOG_ID = "edit_user";
	public static final String EDIT_USER_DIALOG_WIDGET = "editUserDialog";
	public static final String CONFIRM_USER_REMOVAL_DIALOG_ID = "remove_user_dialog";
	public static final String CONFIRM_USER_REMOVAL_DIALOG_WIDGET = "confirmUserRemovalDialog";

}