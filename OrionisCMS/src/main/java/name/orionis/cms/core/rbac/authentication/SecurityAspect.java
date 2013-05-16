package name.orionis.cms.core.rbac.authentication;
import java.util.List;

import name.orionis.cms.core.rbac.web.AccountController;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security AOP
 * @author orionis
 * @2013-5-16
 * Site : http://blog.orionis.name
 *
 */
@Aspect
public class SecurityAspect {
	
	/**
	 * 1. Anonymous, If visit public page, access, otherwise, deny
	 * 2. Non Anonymous, If in god mode, permit all, otherwise check permissions
	 * 3. Check user permissions
	 * @param joinPoint
	 */
	@Before("hasAnnotation() && controller() && publicMethod()")
	public void authentication(JoinPoint joinPoint) {
		// Develop Mode
		if(configHelper.isDevMode()){
			return ;
		}
		// Current invoke Controller-Method
		String invokeClass = joinPoint.getTarget().getClass().getName();
		String invokeMethod = joinPoint.getSignature().getName();
		
		// Current permission name
		String invokePermissionName = SecurityHelper.parsePermissionName(
				invokeClass, invokeMethod);
		
		// Public page, open it
		if(containsElement(invokePermissionName, publicAccess)){
			return ;
		}
		
		// Get current user
		UserInfo userinfo = (UserInfo) sessionHelper.get(AccountController.ACCOUNT_INFO);
		// Anonymous, Check if the current page are anonymous only
		if (userinfo == null) {
			if(containsElement(invokePermissionName, anonymousOnlyAccess)){
				return ;
			}
			throw new PermissionDenyException("Access Denied!");
		}
		
		// God Mode, Permit all
		if(userinfo.isGodMode()){
			log.info("God Mode:" 
					+ SessionHelper.getHttpServletRequest().getRequestURI());
			return ;
		}
		
		// If invoke anonymous only method, forbidden
		if(containsElement(invokePermissionName, anonymousOnlyAccess)){
			throw new PermissionDenyException(
					"You have logan,Need not access this page!");
		}
		
		

		// Role Permissions List
		List<String> role_permissions = securityHelper
				.getPermissionsByRoleId(userinfo.getRoleId());

		if (!role_permissions.contains(invokePermissionName)) {
			log.info("Access Intercept:  " + invokeClass + " : " + invokeMethod);
			throw new PermissionDenyException("Access Denied!");
		}
	}
	
	public SecurityAspect() {
		log.info("Instance SecurityAspect");
	}

	@Pointcut("execution(public * name.orionis..*.*(..))")
	public void publicMethod() {
	}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void hasAnnotation(){}

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controller() {
	}
	/**
	 * Check whether str in list, support regex
	 * @param key
	 * @param list
	 * @return
	 */
	private boolean containsElement(String key, List<String> list){
		for(String l: list){
			if(key.matches(l)){
				return true;
			}
		}
		return false;
	}
	
	public SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	public void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public SecurityHelper getSecurityHelper() {
		return securityHelper;
	}

	public void setSecurityHelper(SecurityHelper securityHelper) {
		this.securityHelper = securityHelper;
	}
	
	public List<String> getPublicAccess() {
		return publicAccess;
	}

	public void setPublicAccess(List<String> publicAccess) {
		this.publicAccess = publicAccess;
	}

	public List<String> getAnonymousOnlyAccess() {
		return anonymousOnlyAccess;
	}

	public void setAnonymousOnlyAccess(List<String> anonymousOnlyAccess) {
		this.anonymousOnlyAccess = anonymousOnlyAccess;
	}

	public ConfigHelper getConfigHelper() {
		return configHelper;
	}

	public void setConfigHelper(ConfigHelper configHelper) {
		this.configHelper = configHelper;
	}

	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> publicAccess;// Public access page
	private List<String> anonymousOnlyAccess;// Anonymous Only Access
	private SecurityHelper securityHelper;
	private SessionHelper sessionHelper;
	private ConfigHelper configHelper;

}