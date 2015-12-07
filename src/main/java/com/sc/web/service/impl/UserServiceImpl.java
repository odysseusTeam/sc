package com.sc.web.service.impl;

import com.sc.core.generic.GenericDao;
import com.sc.core.generic.GenericServiceImpl;
import com.sc.web.dao.UserMapper;
import com.sc.web.model.User;
import com.sc.web.model.UserExample;
import com.sc.web.service.UserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户Service实现类
 *
 * @author StarZou
 * @since 2014年7月5日 上午11:54:24
 */
@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(User model) {
        return userMapper.insertSelective(model);
    }

    @Override
    public int update(User model) {
        return userMapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public int delete(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User authentication(User user) {
        return userMapper.authentication(user);
    }

    @Override
    public User selectById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public GenericDao<User, Long> getDao() {
        return userMapper;
    }

    @Override
    public User selectByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        final List<User> list = userMapper.selectByExample(example);
        return list.get(0);
    }

    /**
     * dateTable 处理
     * @return
     */
    @Override
    public Map dataTable(String searchText , int sEcho) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameLike(searchText);

        final List<User> list = this.userMapper.dataTable(userExample);
        System.out.println("数量。" + list.size());
        Map<String,Object> map=new HashedMap();
        map.put("sEcho", sEcho+1);//不知道是什么,每次加一就好
        map.put("iTotalRecords", list.size());//当前总数据条数
        map.put("iTotalDisplayRecords", list.size());//查询结果的总条数
        map.put("aaData", list);
        return map;


/*        page = this.userMapper.dataTable(userExample);
        System.out.println("page: " + page);
        Map<String,Object> map=new HashedMap();
        map.put("sEcho", sEcho+1);//不知道是什么,每次加一就好
        map.put("iTotalRecords", this.count());//当前总数据条数
        map.put("iTotalDisplayRecords", page.getTotalCount());//查询结果的总条数
        map.put("aaData", page.getResult());
        return map;*/

    }


}
