package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.common.res.ResultCodeEnum;
import cc.jiusi.blog.entity.dto.TagDto;
import cc.jiusi.blog.entity.po.Tag;
import cc.jiusi.blog.entity.vo.TagVo;
import cc.jiusi.blog.exception.GlobalException;
import cc.jiusi.blog.mapper.TagMapper;
import cc.jiusi.blog.service.ITagService;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-17
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     * @param id
     * @author: 九思.
     * @date: 2023/11/17 16:20
     * @param: id
     * @return: cc.jiusi.blog.entity.vo.TagVo
     * @description: 根据id 查询标签详情
     */
    @Override
    public TagVo findById(Long id) {
        // 参数校验
        if (id == null) {
            return null;
        }
        // 查询标签详情
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new GlobalException(ResultCodeEnum.NOT_FOUND);
        }
        // 封装 vo
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 16:33
     * @param: Integer pageNum 页数
     * @param: Integer pageSize 每页显示条数
     * @return: Page<TagVo>
     * @description: 分页查询标签信息
     */
    @Override
    public Page<TagVo> list(Integer pageNum, Integer pageSize) {
        Page<Tag> page = new Page(pageNum, pageSize);
        tagMapper.selectPage(page, null);

        // 过滤数据（tag -》 tagVo）
        List<TagVo> tagVos = page.getRecords().stream()
                .map((tag) -> {
                    TagVo tagVo = new TagVo();
                    BeanUtils.copyProperties(tag, tagVo);
                    return tagVo;
                }).collect(Collectors.toList());

        // 构造 vo
        Page<TagVo> voPage = new Page<>();

        // 拷贝
        BeanUtils.copyProperties(page, voPage);

        voPage.setRecords(tagVos);

        return voPage;
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 16:55
     * @param: TagVo tag
     * @return: void
     * @description: 新增标签
     */
    @Override
    public String saveTag(TagVo tag) {
        String name = tag.getName();
        String shortName = tag.getShortName();
        // 参数校验
        if (StringUtils.isEmpty(name)) {
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED, "标签名称不能为空");
        }
        // 标签名重复判断
        Integer count = tagMapper.selectCount(
                Wrappers.<Tag>lambdaQuery().eq(Tag::getName, name)
        );
        if (count > 0) {
            throw new GlobalException(ResultCodeEnum.DOUBLE_PARAMS, "标签名称已存在");
        }

        // 如果缩略名为空，自动匹配（纯英文，去掉空格，单词之间下划线分割）
        if (StringUtils.isBlank(shortName)) {
            shortName = convertShortName(name);
        } else {
            // 自定义缩略名时重复判断
            // 判断缩略名是否重复
            Integer shortCount = tagMapper.selectCount(
                    Wrappers.<Tag>lambdaQuery().likeRight(Tag::getShortName, shortName)
            );
            // 缩略名重复
            if (shortCount > 0) {
                throw new GlobalException(ResultCodeEnum.DOUBLE_PARAMS, "缩略名已存在");
            }
        }
        // 插入标签信息
        Tag newTag = new Tag();
        newTag.setName(name);
        newTag.setShortName(shortName);
        tagMapper.insert(newTag);
        return newTag.getId();
    }

    @Override
    public void updateTag(TagVo tagVo) {
        // 参数校验
        if (tagVo.getId() == null) {
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED, "参数错误");
        }
        // name不为空，缩略名为空，则自动重写缩略名
        String shortName;
        if (StringUtils.isNotBlank(tagVo.getName()) && StringUtils.isBlank(tagVo.getShortName())) {
            // 缩略名重写
            shortName = convertShortName(tagVo.getName());

        } else {
            shortName = convertShortName(tagVo.getShortName());
        }

        // 更新实体
        tagVo.setShortName(shortName);
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagVo, tag);
        // 重复性校验
        LambdaQueryWrapper<Tag> wrapper = Wrappers.<Tag>lambdaQuery()
                .eq(Tag::getName, tagVo.getName())
                .ne(Tag::getId, tagVo.getId())
                .or()
                .eq(Tag::getShortName, tagVo.getShortName())
                .ne(Tag::getId, tagVo.getId());
        Integer count = tagMapper.selectCount(wrapper);
        if (count > 0) {
            throw new GlobalException(ResultCodeEnum.DOUBLE_PARAMS, "标签名或缩略名已存在");
        }
        tagMapper.updateById(tag);
    }

    /**
     * @author: 九思.
     * @date: 2023/11/17 19:26
     * @param: Long> ids
     * @return: void
     * @description: 批量删除标签
     */
    @Override
    public void removeTags(List<String> ids) {
        // TODO 标签下有文章内容时删除失败
        ids.forEach(id -> {
            tagMapper.deleteById(id);
        });
    }

    @Override
    public Page<TagVo> selectPage(TagDto tagDto) {
        // 获取分页参数
        long pageNum = tagDto.getPageNum() == null ? 1L : tagDto.getPageNum();
        long pageSize = tagDto.getPageSize() == null ? 10L : tagDto.getPageSize();
        // 查询标签
        Page<Tag> page = new Page<>(pageNum, pageSize);
        // 构造查询条件
        LambdaQueryWrapper<Tag> wrapper = Wrappers.<Tag>lambdaQuery();
        String name = tagDto.getName();
        String createBy = tagDto.getCreateBy();
        Date[] createTime = tagDto.getCreateTime();
        wrapper.like(name != null, Tag::getName, name)
                .like(createBy != null, Tag::getCreateBy, createBy);
        if (createTime != null) {
            wrapper.ge(createTime[0] != null, Tag::getCreateTime, createTime[0])
                    .le(createTime[1] != null, Tag::getCreateTime, createTime[1]);
        }

        tagMapper.selectPage(page, wrapper);
        // 构造结果 Page
        Page<TagVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        // 拷贝 records
        List<TagVo> records = page.getRecords().stream().map(tag -> {
            TagVo tagVo = new TagVo();
            BeanUtils.copyProperties(tag, tagVo);
            return tagVo;
        }).collect(Collectors.toList());
        voPage.setRecords(records);

        return voPage;
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
        Integer count = tagMapper.selectCount(
                Wrappers.<Tag>lambdaQuery().likeRight(Tag::getShortName, sb.toString())
        );
        // 如果重复则进行拼接
        if (count > 0) {
            sb.append("_").append(count);
        }
        return sb.toString();
    }

}
