package com.sc.web.controller;

import com.sc.core.util.ApplicationUtils;
import com.sc.web.model.User;
import com.sc.web.security.PermissionSign;
import com.sc.web.security.RoleSign;
import com.sc.web.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by 黄柏樟 on 2015/11/10.
 * @Explain: 用户控制器
 */
@Controller
@RequestMapping(value = "admin/user")
public class UserController extends BaseAdminController<User,Long> {

    @Resource
    private UserService userService;

    @RequestMapping("list")
    public String list(){
        return TEMPLATE_PATH+"list";
    }
    /**
     * 登录页
     */
    @RequestMapping("/loginUI")
    public String login() {
        return TEMPLATE_PATH+"loginUI";
    }

    /**
     * 用户登录
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        System.out.println("用户名："+user.getUsername());
        try {
            //获取个体
            Subject subject = SecurityUtils.getSubject();
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/";
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                return TEMPLATE_PATH+"/loginUI";
            }
            // 身份验证，进入realm的登陆验证
            System.out.println("controller.login之前");
            subject.login(new UsernamePasswordToken(user.getUsername(), ApplicationUtils.sha256Hex(user.getPassword())));
            System.out.println("controller.login之后");
            // 验证成功在Session中保存用户信息
            final User authUserInfo = userService.selectByUsername(user.getUsername());
            request.getSession().setAttribute("userInfo", authUserInfo);
        } catch (AuthenticationException e) {
            // 身份验证失败
            e.printStackTrace();
            System.out.println("realm抛出异常后");
            model.addAttribute("error", "用户名或密码错误 ！");
            return TEMPLATE_PATH+"/loginUI";
        }
        return admin_dir+"index";
    }

    /**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return REDIRECT_URL+"/login";
    }

    /**
     * dataTable传参
     * @param searchText
     * @param sEcho
     * @return
     */
    @RequestMapping("/dataTable")
    @ResponseBody
    public Map dataTable(String searchText,int sEcho){
        return userService.dataTable(searchText,sEcho);
    }



    /**
     * 基于角色 标识的权限控制案例
     */
    @RequestMapping(value = "/admin")
    @ResponseBody
    @RequiresRoles(value = RoleSign.ADMIN)
    public String admin() {
        return "拥有admin角色,能访问";
    }

    /**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @RequiresPermissions(value = PermissionSign.USER_CREATE)
    public String create() {
        return "拥有user:create权限,能访问";
    }
}
