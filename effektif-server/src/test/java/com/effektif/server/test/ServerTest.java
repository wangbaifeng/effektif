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
package com.effektif.server.test;


import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.effektif.mongo.MongoConfiguration;
import com.effektif.server.EffektifJsonProvider;
import com.effektif.server.WorkflowServer;
import com.effektif.workflow.api.Configuration;
import com.effektif.workflow.api.activities.EndEvent;
import com.effektif.workflow.api.activities.NoneTask;
import com.effektif.workflow.api.activities.ReceiveTask;
import com.effektif.workflow.api.activities.StartEvent;
import com.effektif.workflow.api.model.Deployment;
import com.effektif.workflow.api.model.Message;
import com.effektif.workflow.api.model.TriggerInstance;
import com.effektif.workflow.api.model.WorkflowInstanceId;
import com.effektif.workflow.api.workflow.ExecutableWorkflow;
import com.effektif.workflow.api.workflowinstance.WorkflowInstance;
import com.effektif.workflow.impl.json.JsonStreamMapper;

/**
 * @author Tom Baeyens
 */
public class ServerTest extends JerseyTest {
  
//  static {
//    try {
//      final InputStream inputStream = ServerTest.class.getResourceAsStream("/logging.properties");
//      LogManager.getLogManager().readConfiguration(inputStream);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

  public static final Logger log = LoggerFactory.getLogger(ServerTest.class);
  
  static Configuration configuration = null;
  
  @Override
  protected Application configure() {
    return WorkflowServer.buildRestApplication(getConfiguration());
  }

  protected Configuration getConfiguration() {
    if (configuration!=null) {
      return configuration;
    }
    configuration = new MongoConfiguration()
      .server("localhost", 27017);
    configuration.start();
    return configuration;
  }
  
  @Override
  protected void configureClient(ClientConfig clientConfig) {
    JsonStreamMapper jsonMapper = getConfiguration().get(JsonStreamMapper.class);
    clientConfig.register(new EffektifJsonProvider(jsonMapper));
  }

  @Test
  public void test() {
    // Create a workflow
    ExecutableWorkflow workflow = new ExecutableWorkflow()
      .sourceWorkflowId("Server test workflow")
      .activity("One", new StartEvent()
        .transitionToNext())
      .activity("Two", new NoneTask()
        .transitionToNext())
      .activity("Three", new ReceiveTask()
        .transitionToNext())
      .activity("Four", new NoneTask()
        .transitionToNext())
      .activity("Five", new EndEvent());

    // String str = getConfiguration().get(JsonService.class).objectToJsonString(workflow);

    Deployment deployment = target("deploy").request()
            .post(Entity.entity(workflow, MediaType.APPLICATION_JSON))
            .readEntity(Deployment.class);

    assertFalse(deployment.getIssueReport(), deployment.hasIssues());
    
    runProcessInstance();
//    for (int i=0; i<20; i++) {
//      runProcessInstance(workflowId);
//    }
//    long start = System.currentTimeMillis();
//    for (int i=20; i<1000; i++) {
//      log.info("starting "+i);
//      runProcessInstance(workflowId);
//    }
//    long end = System.currentTimeMillis();
//    log.info("1000 process instances in "+((end-start)/1000f)+ " seconds");
//    log.info("1000 process instances in "+(1000000f/(end-start))+ " per second");
  }

  protected void runProcessInstance() {
    TriggerInstance start = new TriggerInstance()
      .sourceWorkflowId("Server test workflow");

    WorkflowInstance workflowInstance = target("start").request()
            .post(Entity.entity(start, MediaType.APPLICATION_JSON))
            .readEntity(WorkflowInstance.class);

    WorkflowInstanceId workflowInstanceId = workflowInstance.getId();
    String subTaskInstanceId = workflowInstance.findOpenActivityInstance("Three").getId();

    Message message = new Message()
      .workflowInstanceId(workflowInstanceId)
      .activityInstanceId(subTaskInstanceId);
    
    workflowInstance = target("message").request()
            .post(Entity.entity(message, MediaType.APPLICATION_JSON))
            .readEntity(WorkflowInstance.class);

    assertTrue(workflowInstance.isEnded());
  }
}
