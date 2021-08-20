package com.a2008q.crud.service;

import com.a2008q.crud.bean.Employee;
import com.a2008q.crud.bean.EmployeeExample;
import com.a2008q.crud.bean.Menu;
import com.a2008q.crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 *
 * @author a2008q
 * @since 2021/6/22 14:08
 */
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 根据姓名或id搜索员工
     */
    public List<Employee> getEmpByVague(String empName) {
        return employeeMapper.selectByVague(empName);
    }

    public Employee queryOneUser(Employee employee) {
        return employeeMapper.queryOneUser(employee);
    }


    public List<Employee> getAll() {
        return employeeMapper.selectByExampleWithDept(null);
    }

    public void saveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    /**
     * 检验用户名是否可用
     *
     * @param empName
     * @return true 表示当前姓名可用
     */
    public boolean checkUser(String empName) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(empName);
        long count = employeeMapper.countByExample(example);
        return 0 == count;
    }

    public Employee getEmp(Integer id) {
        Employee employee = employeeMapper.selectByPrimaryKeyWithDept(id);
        return employee;
    }

    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public void deleteEmp(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    public void deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);
    }

    public Map<String, Object> countGender() {
        return employeeMapper.countGender();
    }

    public List<Map<String, Object>> countDept() {
        return employeeMapper.countDept();
    }
}
