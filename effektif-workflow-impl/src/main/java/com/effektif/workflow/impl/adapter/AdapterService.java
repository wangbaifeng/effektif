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
package com.effektif.workflow.impl.adapter;

import java.util.List;

import com.effektif.workflow.api.datasource.ItemReference;
import com.effektif.workflow.api.model.RequestContext;


public interface AdapterService {
  
  Adapter saveAdapter(Adapter adapter);
  List<Adapter> findAdapters(AdapterQuery adapterQuery);
  Adapter findAdapterById(String adapterId);
  void deleteAdapters(AdapterQuery adapterQuery);

  AdapterService createAdapterService(RequestContext requestContext);

  Adapter refreshAdapter(String adapterId);
  ExecuteResponse executeAdapterActivity(String adapterId, ExecuteRequest executeRequest);
  List<ItemReference> findItems(String adapterId, FindItemsRequest findItemsRequest);
}
