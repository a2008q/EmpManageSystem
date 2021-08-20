package com.a2008q.crud.controller;

import com.a2008q.crud.bean.Menu;
import com.a2008q.crud.bean.Msg;
import com.a2008q.crud.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 *
 * @author a2008q
 * @since 2021/8/14 9:46
 */
@Controller
public class MenuController {
    @Autowired
    MenuService menuService;

    @ApiOperation(value = "根据用户id获取菜单", httpMethod = "GET")
    @RequestMapping(value = "/menu/{did}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getMenu(@PathVariable("did") Integer dId) {
        List<Menu> menus = menuService.queryMenusByDeptId(dId);
        return Msg.success().add("menu", menus);
    }

    @ApiOperation(value = "获取全部菜单", httpMethod = "GET")
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    @ResponseBody
    public Msg getAllMenu() {
        List<Menu> allMenus = menuService.getAllMenus();
        return Msg.success().add("menus", allMenus);
    }
}
