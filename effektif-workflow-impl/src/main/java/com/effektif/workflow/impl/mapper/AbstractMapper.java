/* Copyright (c) 2014, Effektif GmbH.
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
 * limitations under the License. */
package com.effektif.workflow.impl.mapper;


/** a mapper is an object that can read and write API objects.
 * 
 * @author Tom Baeyens
 */
public abstract class AbstractMapper<R,W> {
  
  protected Mappings mappings = new Mappings();

  public Mappings getMappings() {
    return mappings;
  }

  public void setMappings(Mappings mappings) {
    this.mappings = mappings;
  }

  public abstract R createReader();

  public abstract W createWriter();
}