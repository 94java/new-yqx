package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.common.constants.UserConstants;
import cc.jiusi.blog.common.res.ResultCodeEnum;
import cc.jiusi.blog.entity.po.Category;
import cc.jiusi.blog.entity.vo.CategoryVo;
import cc.jiusi.blog.exception.GlobalException;
import cc.jiusi.blog.mapper.CategoryMapper;
import cc.jiusi.blog.service.ICategoryService;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * @author: 九思.
     * @date: 2023/11/17 19:57
     * @param: Long id
     * @return: CategoryVo
     * @description: 查询分类详情
     */
    @Override
    public CategoryVo findById(Long id) {
        // 参数校验
        if(id == null){
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED);
        }
        // 查询
        Category category = categoryMapper.selectById(id);
        if(category == null){
            throw new GlobalException(ResultCodeEnum.NOT_FOUND);
        }
        // 封装vo
        CategoryVo vo = new CategoryVo();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:03
     * @param: Integer pageNum
     * @param: Integer pageSize
     * @return: Page<CategoryVo>
     * @description: 分页查询分类
     */
    @Override
    public Page<CategoryVo> list(Integer pageNum, Integer pageSize) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        categoryMapper.selectPage(page, null);

        // 过滤数据（category -》 categoryVo）
        List<CategoryVo> categoryVos = page.getRecords().stream()
                .map((category) -> {
                    CategoryVo categoryVo = new CategoryVo();
                    BeanUtils.copyProperties(category, categoryVo);
                    return categoryVo;
                }).collect(Collectors.toList());

        // 构造 vo
        Page<CategoryVo> voPage = new Page<>();

        // 拷贝
        BeanUtils.copyProperties(page, voPage);

        voPage.setRecords(categoryVos);

        return voPage;
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param: CategoryVo categoryVo
     * @return: void
     * @description: 新增分类信息
     */
    @Override
    public void saveCategory(CategoryVo categoryVo) {
        String name = categoryVo.getName();
        String shortName = categoryVo.getShortName();
        // 参数校验
        if (StringUtils.isEmpty(name)) {
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED, "分类名称不能为空");
        }
        // 分类名重复判断
        Integer count = categoryMapper.selectCount(
                Wrappers.<Category>lambdaQuery().eq(Category::getName, name)
        );
        if (count > 0) {
            throw new GlobalException(ResultCodeEnum.DOUBLE_PARAMS, "分类名称已存在");
        }

        // 如果缩略名为空，自动匹配（纯英文，去掉空格，单词之间下划线分割）
        if (StringUtils.isBlank(shortName)) {
            shortName = convertShortName(name);
        } else {
            // 自定义缩略名时重复判断
            // 判断缩略名是否重复
            Integer shortCount = categoryMapper.selectCount(
                    Wrappers.<Category>lambdaQuery().likeRight(Category::getShortName, shortName)
            );
            // 缩略名重复
            if (shortCount > 0) {
                throw new GlobalException(ResultCodeEnum.DOUBLE_PARAMS, "缩略名已存在");
            }
        }
        // 插入标签信息
        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setShortName(shortName);
        categoryMapper.insert(fillDefault(newCategory));
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param: CategoryVo categoryVo
     * @return: void
     * @description: 修改分类信息
     */
    @Override
    public void updateCategory(CategoryVo categoryVo) {
        // 参数校验
        if (categoryVo.getId() == null) {
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED, "参数错误");
        }
        // name不为空，缩略名为空，则自动重写缩略名
        String shortName;
        if (StringUtils.isNotBlank(categoryVo.getName()) && StringUtils.isBlank(categoryVo.getShortName())) {
            // 缩略名重写
            shortName = convertShortName(categoryVo.getName());

        } else {
            shortName = convertShortName(categoryVo.getShortName());
        }

        // 更新实体
        categoryVo.setShortName(shortName);
        Category category = new Category();
        BeanUtils.copyProperties(categoryVo, category);
        // 重复性校验
        LambdaQueryWrapper<Category> wrapper = Wrappers.<Category>lambdaQuery()
                .eq(Category::getName, categoryVo.getName())
                .ne(Category::getId,categoryVo.getId())
                .or()
                .eq(Category::getShortName, categoryVo.getShortName())
                .ne(Category::getId,categoryVo.getId());
        Integer count = categoryMapper.selectCount(wrapper);
        if(count > 0){
            throw new GlobalException(ResultCodeEnum.DOUBLE_PARAMS, "分类名或缩略名已存在");
        }
        categoryMapper.updateById(category);
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:07
     * @param: Long> ids
     * @return: void
     * @description: 批量删除分类信息
     */
    @Override
    public void removeCategorys(List<Long> ids) {
        // TODO 分类下有文章内容时删除失败
        ids.forEach(id -> {
            categoryMapper.deleteById(id);
        });
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 17:39
     * @param: String name
     * @return: String
     * @description: 根据标签名构造缩略名
     */
    private String convertShortName(String name) {
        // 汉字转拼音
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (PinyinUtil.isChinese(c)) {
                // 是中文，取首字母,下划线分割
                sb.append(PinyinUtil.getFirstLetter(c));
            }
            // 是数字字母或者下划线
            else if (
                    c == '_' ||
                            (c >= '0' && c <= '9') ||
                            (c >= 'a' && c <= 'z') ||
                            (c >= 'A' && c <= 'Z')
            ) {
                sb.append(c);
            }
        }
        // 判断缩略名是否重复
        Integer count = categoryMapper.selectCount(
                Wrappers.<Category>lambdaQuery().likeRight(Category::getShortName, sb.toString())
        );
        // 如果重复则进行拼接
        if (count > 0) {
            sb.append("_").append(count);
        }
        return sb.toString();
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 20:23
     * @param:  User user
     * @return: User 填充后的User对象
     * @description: 默认信息填充
     */
    private Category fillDefault(Category category) {
        // 默认父级id
        if (category.getParentId() == null) {
            category.setParentId(-1L);
        }
        // 默认order
        if (category.getOrder() == null) {
            category.setOrder(5);
        }
        // 默认逻辑删除标记
        if (StringUtils.isBlank(category.getDelFlag())) {
            category.setDelFlag(UserConstants.DEL_NO);
        }
        return category;
    }
}
