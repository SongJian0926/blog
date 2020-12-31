package blog.service.impl;


import blog.dao.TypeRepository;
import blog.entity.Type;
import blog.service.TypeService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Resource
    TypeRepository typeRepository;

    @Override
    @Transactional
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Type updateType(Long id, Type type) throws NotFoundException {
        Type findType = typeRepository.getOne(id);
        if (findType==null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,findType);
        return typeRepository.save(findType);
    }

    @Override
    @Transactional
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public Type findByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }
}
