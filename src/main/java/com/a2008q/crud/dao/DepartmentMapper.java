package com.a2008q.crud.dao;

import com.a2008q.crud.bean.Department;
import com.a2008q.crud.bean.DepartmentExample;
import com.a2008q.crud.bean.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {
    long countByExample(DepartmentExample example);

    int deleteByExample(DepartmentExample example);

    int deleteByPrimaryKey(Integer deptId);

    int insert(Department record);

    int insertSelective(Department record);

    List<Department> selectByExample(DepartmentExample example);

    Department selectByPrimaryKey(Integer deptId);

    int updateByExampleSelective(@Param("record") Department record, @Param("example") DepartmentExample example);

    int updateByExample(@Param("record") Department record, @Param("example") DepartmentExample example);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Department> selectByExampleWithMenu(DepartmentExample example);

    int deleteMenuById(Integer dId, Integer mId);

    List<Menu> queryMenuByDeptId(Integer dId);

    int deleteAllByDeptId(Integer dId);

    int addMenuByDeptId(Integer dId, Integer mId);
}