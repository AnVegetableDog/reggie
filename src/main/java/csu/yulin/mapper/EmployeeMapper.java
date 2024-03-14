package csu.yulin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import csu.yulin.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
