package com.a2008q.crud.controller;

import com.a2008q.crud.bean.Employee;
import com.a2008q.crud.bean.Menu;
import com.a2008q.crud.bean.Msg;
import com.a2008q.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用模块名称<p>
 * 代码描述 处理员工crud请求
 *
 * @author a2008q
 * @since 2021/6/22 14:04
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 查询功能
     * 可以根据搜索框（content）中的内容查询与之有关的员工，包括员工姓名，员工id。
     */
    @ApiOperation(value = "查询功能", httpMethod = "GET")
    @ResponseBody
    @RequestMapping("/empSearch")
    public Msg searchEmpsByVague(@RequestParam("content") String content, @RequestParam(value = "pn", defaultValue = "1") Integer pn) {

//        引入pageHelper分页查询，
//        在查询之前只需要调用PageHelper.startPage()，传入页码，以及每一页显示的数量
        PageHelper.startPage(pn, 5);
//        分页完之后的查询就是分页查询

//        模糊查询，姓名中包含content的或者id为content的都会被搜索出来
        List<Employee> employees = employeeService.getEmpByVague(content);

//        System.out.println(employees);
//        分页查询完之后，可以使用pageInfo来包装查询后的结果，
//        只需要将pageInfo交给页面就行
//        pageInfo封装了详细的分页信息，包括我们查询出来的数据
//        比如总共有多少页，当前是第几页等。。。
//        想要连续显示5页，就加上参数5即可
        PageInfo pageInfo = new PageInfo(employees, 5);

        return Msg.success().add("pageInfo", pageInfo);
    }

    /**
     * 删除员工
     */
    @ApiOperation(value = "删除员工", httpMethod = "DELETE")
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmpById(@PathVariable("id") String ids) {
        System.out.println(ids);
        if (ids.contains("-")) {
            List<Integer> del_ids = new ArrayList<>();
            String[] str_ids = ids.split("-");
            for (String str_id : str_ids) {
                del_ids.add(Integer.parseInt(str_id));
            }
            employeeService.deleteBatch(del_ids);
            return Msg.success();
        } else {
            int id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
            return Msg.success();
        }
    }

    /**
     * 修改员工
     */
    @ApiOperation(value = "修改员工", httpMethod = "PUT")
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveEmp(@RequestBody Employee employee) {
        employeeService.updateEmp(employee);
        return Msg.success();
    }


    /**
     * 获取员工信息
     */
    @ApiOperation(value = "获取员工信息", httpMethod = "GET")
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee emp = employeeService.getEmp(id);
        return Msg.success().add("emp", emp);
    }

    @ApiOperation(value = "登录", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Msg login(@RequestBody Employee employee) {
        Employee employee1 = employeeService.queryOneUser(employee);
        if (employee1 != null) {

            return Msg.success().add("emp", employee1);
        } else {
            return Msg.fail();
        }
    }

    /**
     * 校验用户名
     */
    @RequestMapping(value = "/checkuser")
    @ResponseBody
    public Msg checkUser(String empName) {
        //判断用户是否合法
        String re = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if (!empName.matches(re)) {
            return Msg.fail().add("va_msg", "用户名可以是2-5位中文或者是6-16位英文和数字的组合");
        }
        boolean b = employeeService.checkUser(empName);
        if (b) {
            return Msg.success();
        } else {
            return Msg.fail().add("va_msg", "用户名不可用");
        }
    }

    /**
     * 员工保存
     *
     * @return
     */
    @ApiOperation(value = "添加员工", httpMethod = "POST")
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid @RequestBody Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, Object> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        } else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }

    }

    @ApiOperation(value = "分页获取员工（json)", httpMethod = "GET")
    @RequestMapping("/emps_json")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
        //引入分页插件
        //传入页码 以及每页的大小
        PageHelper.startPage(pn, 5);
        //紧跟着的分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageinfo包装查询后的结果 将pageinfo交给页码
        //传入连续显示的椰树
        PageInfo page = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo", page);
    }

    /**
     * 查询员工数据（分页查询）
     *
     * @return
     */
    @ApiOperation(value = "分页获取员工", httpMethod = "GET")
    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        //引入分页插件
        //传入页码 以及每页的大小
        PageHelper.startPage(pn, 5);
        //紧跟着的分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageinfo包装查询后的结果 将pageinfo交给页码
        //传入连续显示的椰树
        PageInfo page = new PageInfo(emps, 5);
        model.addAttribute("pageInfo", page);
        return "list";
    }

    @ApiOperation(value = "获取男女人数", httpMethod = "GET")
    @RequestMapping("/gender")
    @ResponseBody
    public Msg countGender() {
        Map<String, Object> map = employeeService.countGender();
        return Msg.success().add("gender", map);
    }

    @ApiOperation(value = "获取各部门人数", httpMethod = "GET")
    @RequestMapping("/cdept")
    @ResponseBody
    public Msg countDept() {
        List<Map<String, Object>> map = employeeService.countDept();
        return Msg.success().add("cdept", map);
    }
}
