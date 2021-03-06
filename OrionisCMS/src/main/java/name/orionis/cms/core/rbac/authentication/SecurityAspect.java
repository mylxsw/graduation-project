package name.orionis.cms.core.rbac.authentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.orionis.cms.core.exception.ActionFailedException;
import name.orionis.cms.core.rbac.web.AccountController;
import name.orionis.cms.utils.Constant;
import name.orionis.cms.utils.JsonConverter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
	 * @throws Throwable 
	 */
	@Around("hasAnnotation() && controller() && (cmsCore() || cmsExtensions())")
	public Object authentication(ProceedingJoinPoint joinPoint) throws Throwable {
		try{
			// Check authority
			_beforeCheck(joinPoint);
			return joinPoint.proceed();
		} catch (Exception e){
			// Catch Exception
			
			// Permission Deny Exception
			if(e instanceof PermissionDenyException){
				throw e;
			}
			
			String methodName = joinPoint.getSignature().getName();
			// If method name contains "list", in general, this method is
			// an ajax method.
			if(methodName.contains("list")){
				throw new ActionFailedException(e.getMessage());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("info", Constant.MESSAGE_ACTION_FAILED);
			map.put("status", "0");
			return JsonConverter.mapToJson(map);
		} 
	}
	/**
	 * Check authority
	 * @param joinPoint
	 */
	private void _beforeCheck(ProceedingJoinPoint joinPoint){
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
		if(containsElement(invokePermissionName, configHelper.getPublicAccess())){
			return ;
		}
		
		// Get current user
		UserInfo userinfo = (UserInfo) sessionHelper.get(AccountController.ACCOUNT_INFO);
		// Anonymous, Check if the current page are anonymous only
		if (userinfo == null) {
			if(containsElement(invokePermissionName, configHelper.getAnonymousOnlyAccess())){
				return ;
			}
			throw new PermissionDenyException("您没有权限执行该操作!", "account/login");
		}
		
		// God Mode, Permit all
		if(userinfo.isGodMode()){
			log.info("超级管理员模式操作:" 
					+ SessionHelper.getHttpServletRequest().getRequestURI());
			return ;
		}
		
		// If invoke anonymous only method, forbidden
		if(containsElement(invokePermissionName, configHelper.getAnonymousOnlyAccess())){
			throw new PermissionDenyException(
					"您没有权限执行该操作!");
		}
		
		

		// Role Permissions List
		List<String> role_permissions =new ArrayList<String>();
		try{
			role_permissions =  securityHelper
					.getPermissionsByRoleId(userinfo.getRoleId());
		}catch(Exception e){}

		if (!role_permissions.contains(invokePermissionName)) {
			log.info("访问拦截:  " + invokeClass + " : " + invokeMethod);
			throw new PermissionDenyException("您没有权限执行该操作!");
		}
	}
	
	public SecurityAspect() {
		log.info("Instance SecurityAspect");
	}

	/**
	 * Ponitcut : core package
	 */
	@Pointcut("execution(public * name.orionis.cms.core..*.*(..))")
	public void cmsCore() {
	}
	/**
	 * Pointcut : extensions package
	 */
	@Pointcut("execution(public * name.orionis.cms.extensions..*.*(..))")
	public void cmsExtensions(){}
	
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
	

	private final Logger log = LoggerFactory.getLogger(getClass());

	private SecurityHelper securityHelper = SecurityHelper.getInstance();
	private SessionHelper sessionHelper = SessionHelper.getInstance();
	private ConfigHelper configHelper = ConfigHelper.getInstance();

}
