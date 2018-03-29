package com.wyait.manage.web.user;

import com.wyait.manage.entity.PermissionVO;
import com.wyait.manage.entity.RoleVO;
import com.wyait.manage.pojo.Permission;
import com.wyait.manage.pojo.Role;
import com.wyait.manage.pojo.RolePermissionKey;
import com.wyait.manage.pojo.User;
import com.wyait.manage.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.web.user
 * @类描述：
 * @创建人：wyait
 * @创建时间：2017-12-20 15:42
 * @version：V1.0
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory
			.getLogger(AuthController.class);
	@Autowired private AuthService authService;

	/**
	 * 添加权限【test】
	 * @param permission
	 * @return ok/fail
	 */
	@RequestMapping(value = "/addPermission", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView toPage(
			Permission permission) {
		logger.debug("新增权限--permission-" + permission);
		ModelAndView mav = new ModelAndView("/home");
		try {
			if (null != permission) {
				permission.setInsertTime(new Date());

				authService.addPermission(permission);
				logger.debug("新增权限成功！-permission-" + permission);
				mav.addObject("msg", "ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "fail");
			logger.error("新增权限异常！", e);
		}
		return mav;
	}

	/**
	 * 权限列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/permList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView permList() {
		logger.debug("权限列表！");
		ModelAndView mav = new ModelAndView("/auth/permList");
		try {
			List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);
			mav.addObject("msg", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("权限查询异常！", e);
		}
		return mav;
	}

	/**
	 * 添加权限
	 * @param type [0：编辑；1：新增子节点权限]
	 * @param permission
	 * @return ModelAndView ok/fail
	 */
	@RequestMapping(value = "/setPerm", method = RequestMethod.POST)
	@ResponseBody public String setPerm(
			@RequestParam("type") int type, Permission permission) {
		logger.debug("设置权限--区分type-" + type + "【0：编辑；1：新增子节点权限】，权限--permission-"
				+ permission);
		try {
			if (null != permission) {
				Date date = new Date();
				if (0 == type) {
					permission.setUpdateTime(date);
					//编辑权限
					this.authService.updatePerm(permission);
				} else if (1 == type) {
					permission.setInsertTime(date);
					//增加子节点权限
					this.authService.addPermission(permission);
				}
				logger.debug("设置权限成功！-permission-" + permission);
				return "ok";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置权限异常！", e);
		}
		return "设置权限出错，请您稍后再试";
	}

	/**
	 * 获取权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getPerm", method = RequestMethod.GET)
	@ResponseBody
	public Permission getPerm(
			@RequestParam("id") int id) {
		logger.debug("获取权限--id-" + id);
		try {
			if (id > 0) {
				Permission perm = this.authService.getPermission(id);
				logger.debug("获取权限成功！-permission-" + perm);
				return perm;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取权限异常！", e);
		}
		return null;
	}

	/**
	 * 删除权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public String del(
			@RequestParam("id") int id) {
		logger.debug("删除权限--id-" + id);
		try {
			if (id > 0) {
				return this.authService.delPermission(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除权限异常！", e);
		}
		return "删除权限出错，请您稍后再试";
	}

	/**
	 * 跳转到角色列表
	 * @return
	 */
	@RequestMapping("/roleManage")
	public ModelAndView toPage() {
		return new ModelAndView("/auth/roleManage");
	}

	/**
	 * 角色列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> getRoleList() {
		logger.debug("角色列表！");
		List<Role> roleList=null;
		try {
			roleList = authService.roleList();
			logger.debug("角色列表查询=roleList:" + roleList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("角色查询异常！", e);
		}
		return roleList;
	}

	/**
	 * 查询权限树数据
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/findPerms", method = RequestMethod.GET)
	@ResponseBody
	public List<PermissionVO> findPerms() {
		logger.debug("权限树列表！");
		List<PermissionVO> pvo = null;
		try {
			pvo = authService.findPerms();
			//生成页面需要的json格式
			logger.debug("权限树列表查询=pvo:" + pvo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("权限树列表查询异常！", e);
		}
		return pvo;
	}

	/**
	 * 添加角色并授权
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	@ResponseBody
	public String addRole(@RequestParam("permIds") String permIds, Role role) {
		logger.debug("添加角色并授权！角色数据role："+role+"，权限数据permIds："+permIds);
		try {
			if(StringUtils.isEmpty(permIds)){
				return "未授权，请您给该角色授权";
			}
			if(null == role){
				return "请您填写完整的角色数据";
			}
			role.setInsertTime(new Date());
			return authService.addRole(role,permIds);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加角色并授权！异常！", e);
		}
		return "操作错误，请您稍后再试";
	}
	/**
	 * 根据id查询角色
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/updateRole/{id}", method = RequestMethod.GET)
	//@ResponseBody
	public ModelAndView updateRole(@PathVariable("id") Integer id) {
		logger.debug("根据id查询角色id："+id);
		ModelAndView mv=new ModelAndView("/auth/roleManage");
		try {
			if(null==id){
				mv.addObject("msg","请求参数有误，请您稍后再试");
				return mv;
			}
			mv.addObject("flag","updateRole");
			RoleVO rvo=this.authService.findRoleAndPerms(id);
			//角色下的权限
			List<RolePermissionKey> rpks=rvo.getRolePerms();
			//获取全部权限数据
			List<PermissionVO> pvos = authService.findPerms();
			for (RolePermissionKey rpk : rpks) {
				//设置角色下的权限checked状态为：true
				for (PermissionVO pvo : pvos) {
					if(String.valueOf(rpk.getPermitId()).equals(String.valueOf(pvo.getId()))){
						pvo.setChecked(true);
					}
				}
			}
			mv.addObject("perms", pvos.toArray());
			//角色详情
			mv.addObject("roleDetail",rvo);
			logger.debug("根据id查询角色数据："+mv);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加角色并授权！异常！", e);
			mv.addObject("msg","请求异常，请您稍后再试");
		}
		return mv;
	}

	/**
	 * 更新角色并授权
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/setRole", method = RequestMethod.POST)
	@ResponseBody
	public String setRole(@RequestParam("rolePermIds") String permIds, Role role) {
		logger.debug("更新角色并授权！角色数据role："+role+"，权限数据permIds："+permIds);
		try {
			if(StringUtils.isEmpty(permIds)){
				return "未授权，请您给该角色授权";
			}
			if(null == role){
				return "请您填写完整的角色数据";
			}
			role.setUpdateTime(new Date());
			return authService.updateRole(role,permIds);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新角色并授权！异常！", e);
		}
		return "操作错误，请您稍后再试";
	}

	/**
	 * 删除角色以及它对应的权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delRole", method = RequestMethod.POST)
	@ResponseBody
	public String delRole(
			@RequestParam("id") int id) {
		logger.debug("删除角色以及它对应的权限--id-" + id);
		try {
			if (id > 0) {
				return this.authService.delRole(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除角色异常！", e);
		}
		return "删除角色出错，请您稍后再试";
	}

	/**
	 * 查找所有角色
	 * @return
	 */
	@RequestMapping(value = "/getRoles", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> getRoles() {
		logger.debug("查找所有角色!");
		List<Role> roles=null;
		try {
			return this.authService.getRoles();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查找所有角色异常！", e);
		}
		return roles;
	}

	/**
	 * 根据用户id查询权限树数据
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/getUserPerms", method = RequestMethod.GET)
	@ResponseBody
	public List<PermissionVO> getUserPerms() {
		logger.debug("根据用户id查询限树列表！");
		List<PermissionVO> pvo = null;
		User existUser= (User) SecurityUtils.getSubject().getPrincipal();
		if(null==existUser){
			logger.debug("根据用户id查询限树列表！用户未登录");
			return pvo;
		}
		try {
			pvo = authService.getUserPerms(existUser.getId());
			//生成页面需要的json格式
			logger.debug("根据用户id查询限树列表查询=pvo:" + pvo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据用户id查询权限树列表查询异常！", e);
		}
		return pvo;
	}
}
