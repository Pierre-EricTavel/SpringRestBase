
package net.itta.springrest.webflux;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class NewSchoolWebApplicationInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer
{

        
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses()  {
        return new Class[] {WebAppContext.class}; 
       
    }

    @Override
    protected boolean isAsyncSupported() {
        return true;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

}