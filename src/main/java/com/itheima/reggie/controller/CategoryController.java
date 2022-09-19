package com.itheima.reggie.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/page")
    public R<Page> getCategory(int page,int pageSize){
        log.info("page={}  pageSize={}",page,pageSize);
        R<Page> category = categoryService.getCategory(page, pageSize);
        return category;
    }

    @PostMapping
    public R<String> sava(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @DeleteMapping("{/id}")
    public R<String> delect(@PathVariable Long id){
//        categoryService.removeById(id);
        categoryService.remove(id);
       return R.success("分类删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getCategoryList(Category category){
        log.info(category.toString());
       return categoryService.getCategoryList(category);
    }
}
