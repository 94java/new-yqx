package top.hellocode.service.impl;

import top.hellocode.entity.Role;
import top.hellocode.mapper.RoleMapper;
import top.hellocode.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
