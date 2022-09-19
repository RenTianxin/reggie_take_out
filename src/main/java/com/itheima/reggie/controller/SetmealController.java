package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    public SetmealDishService setmealDishService;
    @Autowired
    public CategoryService categoryService;


    @GetMapping("/page")
    public R<Page> getSetmealPage(int page,int pageSize,String name){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();

            Category c = categoryService.getById(categoryId);

            if(c != null){
                String categoryName = c.getName();

                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);


        return R.success(setmealDtoPage);
    }


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> check(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    @DeleteMapping
    @Transactional
    public  R<String> delete(@RequestParam Long[] ids){
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        boolean flag = false;
        for (Long id : ids) {
            if (setmealService.getById(id).getStatus() == 1){
                flag = true;
            }
        }
        if (flag){
            return R.error("您想要删除的套餐为起售状态，删除失败,请停售后删除");
        }
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
            setmealDishService.remove(queryWrapper);
            setmealService.removeById(id);
        }
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status,String[] ids){
        for (String id:ids){
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<Setmeal>> list (Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
