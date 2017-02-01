package stac.discriminer.callRecord;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class gabFeedTransformer implements ClassFileTransformer {
	public static boolean flag = false;
	public byte[] transform(
    ClassLoader loader,
    String className,
    Class<?> classBeingRedefined,
    ProtectionDomain protectionDomain,
    //endregion
    byte[] classfileBuffer) throws IllegalClassFormatException {

	    ClassPool cp = ClassPool.getDefault();
	    
	    className = className.replace("/", ".");

	    //Skip all agent classes
	    //region filter agent classes
	    // we do not want to profile ourselves
	    if (className.startsWith("stac.discriminer.callRecord")) {
	      return null;
	    }
	    //endregion
	    
	    //region filter out non-application classes
	    // Application filter. Can be externalized into a property file.
	    // For instance, profilers use blacklist/whitelist to configure this kind of filters
	    if (!className.startsWith("gab_feed1")) {
	      return null;
	    }
	    //endregion

	    try 
	    {
			  cp.importPackage("stac.discriminer.callRecord");
			
			  CtClass ct = cp.makeClass(new ByteArrayInputStream(classfileBuffer));
			
			  CtMethod[] declaredMethods = ct.getDeclaredMethods();
			  for (CtMethod method : declaredMethods) {
			    //region instrument method
				  if(Modifier.isNative(method.getModifiers()))
					  return null;

			      method.insertBefore(" { " +
			              "Stack.log(\"" + className + "\" , \"" + method.getName() + "\"); " +
			              "}");
			      if(!flag)			    	  
			      {
			    	  method.insertAfter("{ Stack.print(\"" + className + "\"); }", true);
			    	  flag = true;
			      }
			    //endregion
	      }
		   return ct.toBytecode();
	    } catch (Throwable e) {
	      e.printStackTrace();
		   return null;
	    }
	 }
}
