package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;







////构造分页构造器对象
//        Page<Dish> pageInfo = new Page<>(page, pageSize);
//        Page<DishDto> dishDtoPage = new Page<>();
//
//        //条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        //添加过滤条件
//        queryWrapper.like(name != null, Dish::getName, name);
//        //添加排序条件
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//
//        //执行分页查询
//        page(pageInfo, queryWrapper);
//
//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
//
//        List<Dish> records = pageInfo.getRecords();
//
//        List<DishDto> list = records.stream().map((item) -> {
//            DishDto dishDto = new DishDto();
//
//            BeanUtils.copyProperties(item, dishDto);
//
//            Long categoryId = item.getCategoryId();//分类id
//            //根据id查询分类对象
//            Category category = categoryService.getById(categoryId);
//
//            if (category != null) {
//                String categoryName = category.getName();
//                dishDto.setCategoryName(categoryName);
//            }
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        dishDtoPage.setRecords(list);
//
//        return R.success(dishDtoPage);
//    }
//
//        Page<Dish> pageInfo = new Page<>(page,pageSize);
//        Page<DishDto> dishDtoPage = new Page<>();
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotEmpty(name)){
//            queryWrapper.like(Dish::getName,name);
//        }
//        queryWrapper.orderByAsc(Dish::getSort);
//
//        page(pageInfo,queryWrapper);
//
//        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
//        //h获取了
//        List<Dish> records = pageInfo.getRecords();
//      List<DishDto>  list =  records.stream().map((item)->{
//            DishDto dishDto = new DishDto();
//            BeanUtils.copyProperties(item,dishDto);
//            Long categoryId = item.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//
//            if (category != null){
//                String categoryName = category.getName();
//                dishDto.setCategoryName(categoryName);
//            }
//
//
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        dishDtoPage.setRecords(list);
//
//        return R.success(dishDtoPage);



    @Override
    public R<String> addDish(@RequestBody DishDto dishDto) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        int count = count(dishQueryWrapper.eq(Dish::getName, dishDto.getName()));
        if (count > 0 ){
           return R.error("已经有这道菜了");
        }
        this.save(dishDto);
        return R.success("添加成功");
    }

    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //获取菜品ID
        Long id = dishDto.getId();
        //获取Flavors
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        //保存到
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);


        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            Long dishId = item.getDishId();
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }


}
