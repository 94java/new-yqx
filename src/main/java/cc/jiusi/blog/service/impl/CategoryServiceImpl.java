package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.entity.Category;
import cc.jiusi.blog.mapper.CategoryMapper;
import cc.jiusi.blog.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-17
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
