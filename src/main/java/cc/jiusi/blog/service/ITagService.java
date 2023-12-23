package cc.jiusi.blog.service;

import cc.jiusi.blog.entity.dto.TagDto;
import cc.jiusi.blog.entity.po.Tag;
import cc.jiusi.blog.entity.vo.TagVo;
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
public interface ITagService extends IService<Tag> {

    /**
     * @author: 九思.
     * @date: 2023/11/17 16:20
     * @param: id
     * @return: cc.jiusi.blog.entity.vo.TagVo
     * @description: 根据id 查询标签详情
     */
    TagVo findById(Long id);

    /**
     * @author: 九思.
     * @date: 2023/11/17 16:33
     * @param:  Integer pageNum 页数
     * @param:  Integer pageSize 每页显示条数
     * @return: Page<TagVo>
     * @description: 分页查询标签信息
     */
    Page<TagVo> list(Integer pageNum, Integer pageSize);

    /**
     * @author: 九思.
     * @date: 2023/11/17 16:55
     * @param: TagVo tag
     * @return: void
     * @description: 新增标签
     */
    String saveTag(TagVo tag);

    /**
     * @author: 九思.
     * @date: 2023/11/17 19:26
     * @param:  TagVo tag
     * @return: void
     * @description: 修改标签信息
     */
    void updateTag(TagVo tag);

    /**
     * @author: 九思.
     * @date: 2023/11/17 19:26
     * @param:  Long> ids
     * @return: void
     * @description: 批量删除标签
     */
    void removeTags(List<String> ids);

    Page<TagVo> selectPage(TagDto tagDto);
}
