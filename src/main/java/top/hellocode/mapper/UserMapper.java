package top.hellocode.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.hellocode.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
