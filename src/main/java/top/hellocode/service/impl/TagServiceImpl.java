package top.hellocode.service.impl;

import top.hellocode.entity.Tag;
import top.hellocode.mapper.TagMapper;
import top.hellocode.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}
