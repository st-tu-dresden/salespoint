package org.salespointframework.web.spring.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

/**
 * Conforms with DefaultAnnotationsHandlerMapping from Spring except the
 * Handling of {@link Interceptors}
 * 
 * @see Interceptors
 * 
 * @author Lars Kreisz
 * @author Uwe Schmidt
 */
// inspired by http://karthikg.wordpress.com/2009/10/12/athandlerinterceptor-for-spring-mvc/
public class SalespointAnnotationHandlerMapping extends DefaultAnnotationHandlerMapping implements MessageSourceAware {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	private MessageSource messageSource;
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
		HandlerExecutionChain chain = super.getHandlerExecutionChain(handler, request);
		HandlerInterceptor[] interceptors = detectInterceptors(chain.getHandler().getClass());

		if(interceptors.length > 0)
			logInterceptorsAdded(chain.getHandler(), interceptors);
		
		chain.addInterceptors(interceptors);
		return chain;
	}

	private void logInterceptorsAdded(Object handler, HandlerInterceptor[] interceptors) {
		String s = "Controller '"+handler.getClass().getSimpleName()+"' will be intercepted by: ";
		for(HandlerInterceptor hi : interceptors)
			s += hi.getClass().getSimpleName()+" ";
		log.info(s);
	}

	protected HandlerInterceptor[] detectInterceptors(Class<?> handlerClass) {		
		Interceptors interceptorAnnot = AnnotationUtils.findAnnotation(handlerClass, Interceptors.class);
		List<HandlerInterceptor> interceptors = new ArrayList<HandlerInterceptor>();
		if (interceptorAnnot != null) {
			Class<? extends HandlerInterceptor>[] interceptorClasses = interceptorAnnot.value();
			if (interceptorClasses != null) {
				for (Class<? extends HandlerInterceptor> interceptorClass : interceptorClasses) {
					if (!HandlerInterceptor.class.isAssignableFrom(interceptorClass))
						raiseIllegalInterceptorValue(handlerClass, interceptorClass);
					
					HandlerInterceptor hi = (HandlerInterceptor) BeanUtils.instantiateClass(interceptorClass);
					if(MessageSourceAware.class.isAssignableFrom(interceptorClass))
						((MessageSourceAware)hi).setMessageSource(messageSource);
					
					interceptors.add(hi);
				}
			}
		}
		return interceptors.toArray(new HandlerInterceptor[0]);
	}

	protected void raiseIllegalInterceptorValue(Class<?> handlerClass, Class<? extends HandlerInterceptor> interceptorClass) {
		throw new IllegalArgumentException(interceptorClass + " specified on " + handlerClass + " does not implement " + HandlerInterceptor.class.getName());

	}

}
