package com.a2008q.crud.service;

import com.a2008q.crud.bean.Department;
import com.a2008q.crud.bean.Menu;
import com.a2008q.crud.dao.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 *
 * @author a2008q
 * @since 2021/6/23 16:17
 */
@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    public List<Department> getDeps() {
        return departmentMapper.selectByExample(null);
    }

    public List<Department> getAllDeptWithMenu() {
        return departmentMapper.selectByExampleWithMenu(null);
    }

    public int deleteMenuById(Integer dId, Integer mId) {
//        System.out.println(dId);
        return departmentMapper.deleteMenuById(dId, mId);
    }

    public List<Menu> queryMenuByDeptId(Integer dId) {
        return departmentMapper.queryMenuByDeptId(dId);
    }

    public int deleteAllByDeptId(Integer dId) {
        return departmentMapper.deleteAllByDeptId(dId);
    }

    public int addMenuByDeptId(Integer dId, Integer mId) {
        return departmentMapper.addMenuByDeptId(dId, mId);
    }
}
