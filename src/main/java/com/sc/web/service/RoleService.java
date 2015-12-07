package com.sc.web.service;

import java.util.List;
import com.sc.core.generic.GenericService;
import com.sc.web.model.Role;
import com.sc.core.generic.GenericService;
import com.sc.web.model.Role;

/**
 * 角色 业务接口
 * 
 * @author StarZou
 * @since 2014年6月10日 下午4:15:01
 **/
public interface RoleService extends GenericService<Role, Long> {
    /**
     * 通过用户id 查询用户 拥有的角色
     * 
     * @param userId
     * @return
     */
    List<Role> selectRolesByUserId(Long userId);
}
