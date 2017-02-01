package stac.discriminer.callRecord;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class snapBuddyTransformer implements ClassFileTransformer {

	  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      //endregion
      byte[] classfileBuffer) throws IllegalClassFormatException {

	    ClassPool cp = ClassPool.getDefault();
	    cp.insertClassPath(new ClassClassPath(this.getClass()));
	    ClassPool child1 = new ClassPool(cp);
	    ClassPool child2 = new ClassPool(child1);
	    ClassPool child3 = new ClassPool(child2);
	    try {
			child1.insertClassPath("/Users/saeid/Desktop/Documents/Examples_Engagement/External/stac_engagement_2/snapbuddy_1/challenge_program/lib/stac/common/DES.class");
			child2.insertClassPath("/Users/saeid/Desktop/Documents/Examples_Engagement/External/stac_engagement_2/snapbuddy_1/challenge_program/lib/stac/auth/KeyExchangeVerifier.class");
			child3.insertClassPath("/Users/saeid/Desktop/Documents/Examples_Engagement/External/stac_engagement_2/snapbuddy_1/challenge_program/lib/stac/snapservice/LocationServiceImpl.class");
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
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
	    if (!className.startsWith("com.cyberpointllc.stac") && !className.startsWith("com.jhlabs")) {
	      return null;
	    }
	    if(className.startsWith("com.jhlabs.image.PixelUtils") || className.startsWith("com.cyberpointllc.stac.snapservice.model.AccessPoint") 
	      ||  className.startsWith("com.cyberpointllc.stac.snapservice.model.Location") || className.startsWith("com.cyberpointllc.stac.hashmap"))
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

				  if(className.startsWith("com.jhlabs.image.PixelUtils") || className.startsWith("com.jhlabs.vecmath") || className.startsWith("com.jhlabs.math.ImageFunction2D")
						  || className.startsWith("com.jhlabs.image.LightFilter") ||  className.startsWith("com.jhlabs.image.TransferFilter") || className.startsWith("com.jhlabs.image.ImageMath")
						  || (className.startsWith("com.jhlabs.image.KaleidoscopeFilter") && method.getName().startsWith("transformInverse")) || className.startsWith("com.jhlabs.math.Noise")
						  || method.getName().startsWith("transformInverse") || method.getName().startsWith("transformInverseHelper") 
						  || className.contains("$"))
					  return null;
			      method.insertBefore(" { " +
			              "Stack.log(\"" + className + "\" , \"" + method.getName() + "\"); " +
			              "}");
			      if(className.startsWith("com.cyberpointllc.stac.webserver.handler.HttpHandlerResponse") && method.getName().startsWith("addResponseHeadersHelper"))			    	  
			      {
			    	  method.insertAfter("{ Stack.print(\"" + className + "\"); }", true);
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