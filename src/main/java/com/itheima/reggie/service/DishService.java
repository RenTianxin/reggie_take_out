package com.itheima.reggie.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import org.springframework.web.bind.annotation.RequestBody;

public interface DishService extends IService<Dish> {
//    public R<Page> getDishPage(int page, int pageSize, String name);
    public R<String> addDish(@RequestBody DishDto dishDto);

    public void saveWithFlavor(DishDto dishDto);
   //查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
