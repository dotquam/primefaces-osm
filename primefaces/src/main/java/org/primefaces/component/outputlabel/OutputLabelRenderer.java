/*
 * Copyright 2009-2012 Prime Teknoloji.
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
package org.primefaces.component.outputlabel;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.InputHolder;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;

public class OutputLabelRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        OutputLabel label = (OutputLabel) component;
        String clientId = label.getClientId();
        Object value = label.getValue();
        
        UIInput target = findTarget(context, label);
        String _for = (target instanceof InputHolder) ? ((InputHolder) target).getInputClientId() : target.getClientId(context);
        
        String defaultStyleClass = target.isValid() ? OutputLabel.VALID_CLASS : OutputLabel.INVALID_CLASS;
        String styleClass = label.getStyleClass();
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;
        
        writer.startElement("label", label);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "id");
        writer.writeAttribute("for", _for, "for");
        renderPassThruAttributes(context, label, HTML.LABEL_ATTRS);
        
        if(value != null) {
            String text = value.toString();
            if(label.isEscape())
                writer.writeText(text, "value");
            else
                writer.write(text);
            
            //assign label of target
            target.getAttributes().put("label", value);
        }
        
        if(target.isRequired()) {
            writer.startElement("span", label);
            writer.writeAttribute("class", OutputLabel.REQUIRED_FIELD_INDICATOR_CLASS, null);
            writer.write("*");
            writer.endElement("span");   
        }
        
        writer.endElement("label");        
    }
    
    protected UIInput findTarget(FacesContext context, OutputLabel label) {
        UIComponent _forComponent = null;
        String _for = label.getFor();
        
        if(_for != null) {
            _forComponent = label.findComponent(_for);
            if(_forComponent == null) {
                throw new FacesException("Cannot find component with identifier \"" + _for + "\" referenced from \"" + label.getClientId(context) + "\".");
            }
        }
        
        return (UIInput) _forComponent;
    }
}
