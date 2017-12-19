package com.control;

import com.dao.p.LoginUserDao;
import com.entity.p.LoginUser;
import com.sun.jndi.toolkit.dir.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.Predicate;

/**
 * Created by jianggk on 2017/1/23.
 */

//登录工号服务及员工信息服务
@RestController
@RequestMapping("/loginUsers")
@PreAuthorize("hasRole('MONTH_ADMIN')")
public class LoginUserAdminController {
    //@Secured("ROLE_ADMIN")
    //@Secured注释是用来定义业务方法的安全性配置属性列表。
    // 您可以使用@Secured在方法上指定安全性要求[角色/权限等]，只有对应角色/权限的用户才可以调用这些方法。
    // 如果有人试图调用一个方法，但是不拥有所需的角色/权限，那会将会拒绝访问将引发异常。
    @Autowired
    LoginUserDao loginUserDao;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    long getUsersCount() {
        return loginUserDao.count();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    Page<LoginUser> getUsers(@RequestParam(value = "pageId", defaultValue = "0") int pageId,
                             @RequestParam(value = "pageSize", defaultValue = "30") int pageSize,
                             @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                             @RequestParam(value = "order", defaultValue = "desc") String order,
                             @RequestParam(value = "filter", required = false) String filter) {
        //支持分页查询
        System.out.println("--getUsers");
        Sort sort = null;
        if (order.equalsIgnoreCase("desc"))
            sort = new Sort(Sort.Direction.DESC, sortBy);
        else
            sort = new Sort(Sort.Direction.ASC, sortBy);

        if (pageId == 0) {
            pageId = 1;
            pageSize = 500;
        }
        PageRequest pageRequest = new PageRequest(pageId - 1, pageSize, sort);

        if(null!=filter) {
            List<String> filterIds=new ArrayList<>();
            System.out.println("filter="+filter);
            filterIds = jdbcTemplate.queryForList("select name from login_user  where " + filter, String.class);
            System.out.println("filteIds.size="+filterIds.size());
            if(filterIds.size()==0||null==filterIds)
                return null;
            List<LoginUser> result = this.loginUserDao.findAll(filterIds);
            Page<LoginUser> p=new PageImpl(result,pageRequest,result.size());
            return p;
        }
        System.out.println("in getUsers :getUsersPageAble pageId=" + pageId + "  pageSize=" + pageSize);
        Page<LoginUser> result = this.loginUserDao. findAll(pageRequest);
        System.out.println("---"+result+"\n"+result.getClass());
        return result;
        //return loginUserDao.findAll(sort);
    }

    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT})
    LoginUser saveUsers(@RequestBody LoginUser[] lu) {
        return loginUserDao.save((LoginUser) Arrays.asList(lu));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    LoginUser getUser(String id) {
        System.out.println("--getUser");
        return loginUserDao.findByName(id);
    }

//    @RequestMapping(value="/",method= RequestMethod.GET)
//    Page<LoginUser> getUsersPageAble(@RequestParam ("pageId") int pageId,@RequestParam("pageSize") int pageSize) {
//        System.out.println("getUsersPageAble pageId="+pageId+"  pageSize="+pageSize);
//        PageRequest request = new PageRequest(pageId - 1, pageSize, null);
//         Page<LoginUser> result= this.loginUserDao.findAll(request);
//        return result;
//        //return loginUserDao.findAll();
//    }

//    @RequestMapping(value="/",method={RequestMethod.POST,RequestMethod.PUT})//新建，更新
//    LoginUser saveUser(@RequestBody LoginUser lu) {
//        return loginUserDao.save(lu);
//    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
//更新
    void delteUser(long id) {
        //loginUserDao.delete(id);
        return;
    }


}


//GET（SELECT）：从服务器取出资源（一项或多项）。
//POST（CREATE）：在服务器新建一个资源。
//PUT（UPDATE）： 在服务器更新资源（客户端提供改变后的完整资源）。
//PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
//DELETE（DELETE）：从服务器删除资源。
//还有两个不常用的HTTP动词
//HEAD：获取资源的元数据。
//OPTIONS：获取信息，关于资源的哪些属性是客户端可以改变的。

//?limit=10：指定返回记录的数量
//?offset=10：指定返回记录的开始位置。
//?page=2&per_page=100：指定第几页，以及每页的记录数。
//?sortby=name&order=asc：指定返回结果按照哪个属性排序，以及排序顺序。
//?animal_type_id=1：指定筛选条件


//GET /collection：返回资源对象的列表（数组）
//GET /collection/resource：返回单个资源对象
//POST /collection：返回新生成的资源对象
//PUT /collection/resource：返回完整的资源对象
//PATCH /collection/resource：返回完整的资源对象
//DELETE /collection/resource：返回一个空文档

//十、Hypermedia API
//RESTful API最好做到Hypermedia，即返回结果中提供链接，连向其他API方法，使得用户不查文档，也知道下一步应该做什么。
//比如，当用户向api.example.com的根目录发出请求，会得到这样一个文档。