package csu.yulin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.entity.AddressBook;
import csu.yulin.mapper.AddressBookMapper;
import csu.yulin.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {

}




