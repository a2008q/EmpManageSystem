package com.a2008q.crud.controller;

import com.a2008q.crud.bean.Department;
import com.a2008q.crud.bean.Menu;
import com.a2008q.crud.bean.Msg;
import com.a2008q.crud.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 *
 * @author a2008q
 * @since 2021/6/23 16:17
 */
@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * 返回所有的部门信息
     */
    @ApiOperation(value = "返回所有的部门信息", httpMethod = "GET")
    @RequestMapping("/depts_json")
    @ResponseBody
    public Msg getDepts() {
        List<Department> list = departmentService.getDeps();
        return Msg.success().add("depts", list);
    }

    @ApiOperation(value = "返回所有带菜单的部门信息", httpMethod = "GET")
    @RequestMapping(value = "/dept", method = RequestMethod.GET)
    @ResponseBody
    public Msg getAllDeptWithMenu() {
        List<Department> allDeptWithMenu = departmentService.getAllDeptWithMenu();
//        System.out.println(allDeptWithMenu);
        return Msg.success().add("dept", allDeptWithMenu);
    }

    @ApiOperation(value = "根据部门和菜单id删除对应权限", httpMethod = "DELETE")
    @RequestMapping(value = "/dept/{dId}/menu/{mId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteMenuById(@PathVariable("dId") Integer dId, @PathVariable("mId") Integer mId) {
//        System.out.println("dId" + dId + "mId" + mId);
        departmentService.deleteMenuById(dId, mId);
        List<Menu> menus = departmentService.queryMenuByDeptId(dId);
        return Msg.success().add("menu", menus);

    }

    @ApiOperation(value = "分配权限", httpMethod = "POST")
    @RequestMapping(value = "/dept/{dId}/menu", method = RequestMethod.POST)
    @ResponseBody
    public Msg allotMenus(@PathVariable("dId") Integer dId, @RequestBody String mIds) {
        departmentService.deleteAllByDeptId(dId);
        String[] mId = mIds.split("\\D");
//        System.out.println(dId);

        for (String s : mId) {
            if (!s.isEmpty()) {
                departmentService.addMenuByDeptId(dId, Integer.parseInt(s));
//                System.out.println(s);
            }

        }
        return Msg.success();
    }

}
