package com.a2008q.crud.dao;

import com.a2008q.crud.bean.Employee;
import com.a2008q.crud.bean.EmployeeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {


    Employee selectByPrimaryKeyWithDept(Integer empId);

    //    模糊查询
    List<Employee> selectByVague(String empName);

//======================================================================================

    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Integer empId);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Integer empId);

    //带部门的员工查询
    List<Employee> selectByExampleWithDept(EmployeeExample example);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    Employee queryOneUser(Employee employee);

    Map<String, Object> countGender();

    List<Map<String, Object>> countDept();


}