/*
 * Copyright 2014 Effektif GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.effektif.workflow.test.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.effektif.workflow.api.model.Start;
import com.effektif.workflow.api.ref.UserReference;
import com.effektif.workflow.api.types.NumberType;
import com.effektif.workflow.api.types.UserReferenceType;
import com.effektif.workflow.api.workflow.Workflow;
import com.effektif.workflow.api.workflowinstance.WorkflowInstance;
import com.effektif.workflow.test.WorkflowTest;


/**
 * @author Tom Baeyens
 */
public class VariableTypesTest extends WorkflowTest {

  @Test
  public void testNumberType() {
    Workflow workflow = new Workflow()
      .variable("v", new NumberType());
    
    deploy(workflow);

    WorkflowInstance workflowInstance = workflowEngine.startWorkflowInstance(new Start()
      .workflowId(workflow.getId())
      .variableValue("v", 5));
    
    assertEquals(new Long(5), workflowInstance.getVariableValueLong("v"));
  }

  @Test
  public void testUserReferenceType() {
    Workflow workflow = new Workflow()
      .variable("v", new UserReferenceType());
    
    deploy(workflow);

    WorkflowInstance workflowInstance = workflowEngine.startWorkflowInstance(new Start()
      .workflowId(workflow.getId())
      .variableValue("v", new UserReference("u2")));
    
    assertEquals(UserReference.class, workflowInstance.getVariableValue("v").getClass());
  }

}