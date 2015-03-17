package com.effektif.workflow.impl.email;

import com.effektif.workflow.api.workflow.Trigger;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Starts a workflow as a result of receiving an email.
 *
 * @author Peter Hilton
 */
@JsonTypeName("email")
public class EmailTrigger extends Trigger {

  public static final String EMAIL_KEY = "email";
  public static final String EMAIL_ID_KEY = "emailId";

  /** Optional variable that specifies the name to use for the process variable, instead of <code>EMAIL_ID_KEY</code>. */
  protected String emailIdVariableId;

  /**
   * Returns the variable ID to use to look up the {@link com.effektif.workflow.api.model.EmailId},
   * using {@link #EMAIL_ID_KEY} as a default value.
   */
  public String getEmailIdVariableId() {
    return emailIdVariableId == null ? EMAIL_ID_KEY : emailIdVariableId;
  }
  public void setEmailIdVariableId(String emailIdVariableId) {
    this.emailIdVariableId = emailIdVariableId;
  }
  public EmailTrigger emailIdVariableId(String emailIdVariableId) {
    this.emailIdVariableId = emailIdVariableId;
    return this;
  }
}