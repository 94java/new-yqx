package cc.jiusi.blog.service;

import cc.jiusi.blog.entity.dto.CategoryDto;
import cc.jiusi.blog.entity.po.Category;
import cc.jiusi.blog.entity.vo.CategoryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-17
 */
public interface ICategoryService extends IService<Category> {

    /**
     * @author: 九思.
     * @date: 2023/11/17 19:57
     * @param:  Long id
     * @return: CategoryVo
     * @description: 查询分类详情
     */
    CategoryVo findById(Long id);

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:03
     * @param:  Integer pageNum
     * @param:  Integer pageSize
     * @return: Page<CategoryVo>
     * @description: 分页查询分类
     */
    Page<CategoryVo> list(Integer pageNum, Integer pageSize);

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param:  Long> ids
     * @return: void
     * @description: 查询分类列表（树结构）
     */
    List<CategoryVo> selectTreeList(CategoryDto categoryDto);

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param:  CategoryVo categoryVo
     * @return: void
     * @description: 新增分类信息
     */
    void saveCategory(CategoryVo categoryVo);

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param:  CategoryVo categoryVo
     * @return: void
     * @description: 修改分类信息
     */
    void updateCategory(CategoryVo categoryVo);

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param:  Long> ids
     * @return: void
     * @description: 批量删除分类信息
     */
    void removeCategorys(List<Long> ids);
}
