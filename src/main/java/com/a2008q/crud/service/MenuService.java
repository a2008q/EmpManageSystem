package com.a2008q.crud.service;

import com.a2008q.crud.bean.Menu;
import com.a2008q.crud.dao.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 *
 * @author a2008q
 * @since 2021/8/14 9:46
 */
@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    public List<Menu> queryMenusByDeptId(int deptId) {
        return menuMapper.queryMenusByDeptId(deptId);
    }

    public List<Menu> getAllMenus() {
        return menuMapper.selectByExample(null);
    }
}
