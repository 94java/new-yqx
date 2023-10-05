package top.hellocode.service.impl;

import top.hellocode.entity.Note;
import top.hellocode.mapper.NoteMapper;
import top.hellocode.service.INoteService;
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
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {

}
