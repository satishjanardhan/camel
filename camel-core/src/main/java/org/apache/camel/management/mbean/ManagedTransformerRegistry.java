/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.management.mbean;

import java.util.Collection;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.api.management.mbean.CamelOpenMBeanTypes;
import org.apache.camel.api.management.mbean.ManagedTransformerRegistryMBean;
import org.apache.camel.spi.EndpointRegistry;
import org.apache.camel.spi.ManagementStrategy;
import org.apache.camel.spi.Transformer;
import org.apache.camel.spi.TransformerRegistry;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.URISupport;

/**
 * @version 
 */
@ManagedResource(description = "Managed TransformerRegistry")
public class ManagedTransformerRegistry extends ManagedService implements ManagedTransformerRegistryMBean {
    private final TransformerRegistry transformerRegistry;

    public ManagedTransformerRegistry(CamelContext context, TransformerRegistry endpointRegistry) {
        super(context, endpointRegistry);
        this.transformerRegistry = endpointRegistry;
    }

    public void init(ManagementStrategy strategy) {
        super.init(strategy);
    }

    public TransformerRegistry getTransformerRegistry() {
        return transformerRegistry;
    }

    public String getSource() {
        return transformerRegistry.toString();
    }

    public Integer getDynamicSize() {
        return transformerRegistry.dynamicSize();
    }

    public Integer getStaticSize() {
        return transformerRegistry.staticSize();
    }

    public Integer getSize() {
        return transformerRegistry.size();
    }

    public Integer getMaximumCacheSize() {
        return transformerRegistry.getMaximumCacheSize();
    }

    public void purge() {
        transformerRegistry.purge();
    }

    @SuppressWarnings("unchecked")
    public TabularData listTransformers() {
        try {
            TabularData answer = new TabularDataSupport(CamelOpenMBeanTypes.listTransformersTabularType());
            Collection<Transformer> transformers = transformerRegistry.values();
            for (Transformer transformer : transformers) {
                CompositeType ct = CamelOpenMBeanTypes.listTransformersCompositeType();
                String transformerString = transformer.toString();
                boolean fromStatic = transformerRegistry.isStatic(transformerString);
                boolean fromDynamic = transformerRegistry.isDynamic(transformerString);

                CompositeData data = new CompositeDataSupport(ct, new String[]{"string", "static", "dynamic"}, new Object[]{transformerString, fromStatic, fromDynamic});
                answer.put(data);
            }
            return answer;
        } catch (Exception e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

}
