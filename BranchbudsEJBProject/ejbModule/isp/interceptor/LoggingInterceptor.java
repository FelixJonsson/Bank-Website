package isp.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class LoggingInterceptor {

    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        System.out.println("LOG (Interceptor): Calling method -> " 
            + context.getMethod().getName() + "() in " 
            + context.getTarget().getClass().getSimpleName());

        Object result = context.proceed(); 
        
        return result;
    }
}