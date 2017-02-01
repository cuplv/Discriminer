package stac.discriminer.callRecord;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class textCrunchrTransformer implements ClassFileTransformer {
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
	    if (!className.startsWith("com.cyberpointllc.stac")) {
	      return null;
	    }
	    if(className.startsWith("com.cyberpointllc.stac.hashmap.HashMap") || className.startsWith("com.cyberpointllc.stac.hashmap.Node")
		   || className.startsWith("com.cyberpointllc.stac.sort.ArrayIndex") || className.startsWith("com.cyberpointllc.stac.textcrunchr.WordFrequencyProcessor")
		   || className.endsWith("com.cyberpointllc.stac.textcrunchr.TCResult"))
	    	return null;
	    
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
				  if((className.startsWith("com.cyberpointllc.stac.textcrunchr") && method.getName().contains("TCResult"))
					  /*|| (className.startsWith("com.cyberpointllc.stac.sort") && method.getName().contains("changingSortHelper"))*/	  
					  || (className.startsWith("com.cyberpointllc.stac.sort.Sorter$SorterHelper1") && method.getName().startsWith("getValue"))
					  ||  (className.startsWith("com.cyberpointllc.stac.sort.DefaultComparator") && method.getName().startsWith("compare")))
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
