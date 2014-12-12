/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.extras.java8time.dialect;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.extras.java8time.expression.Temporals;

/**
 * 
 * Thymeleaf Dialect to format and create Java 8 Time objects.
 *
 * @author Jos&eacute; Miguel Samper
 *
 * @since 2.1.0
 */
public class Java8TimeDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String TEMPORAL_EVALUATION_VARIABLE_NAME = "temporals";

    public Java8TimeDialect() {
        super();
    }

    @Override
    public String getPrefix() {
        // No attribute or tag processors, so we don't need a prefix at all and
        // we can return whichever value.
        return "java8time";
    }

    @Override
    public boolean isLenient() {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        Map<String, Object> expressionObjects = new HashMap<>();
        Locale locale = processingContext.getContext().getLocale();
        expressionObjects.put(TEMPORAL_EVALUATION_VARIABLE_NAME, new Temporals(locale));
        return expressionObjects;
    }

}
