package csu.yulin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.entity.Employee;
import csu.yulin.mapper.EmployeeMapper;
import csu.yulin.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

}


