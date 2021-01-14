package blog.service.impl;

import blog.dao.TageRepository;
import blog.entity.Tag;
import blog.service.TagService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    TageRepository tageRepository;

    @Override
    public Tag saveTage(Tag tag) {
        return tageRepository.save(tag);
    }

    @Override
    public Tag getTage(Long id) {
        return tageRepository.getOne(id);
    }

    @Override
    public Page<Tag> listTage(Pageable pageable) {
        return tageRepository.findAll(pageable);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) throws InvocationTargetException, IllegalAccessException, NotFoundException {
        Tag findTag = tageRepository.getOne(id);
        if (findTag==null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag,findTag);
        return tageRepository.save(findTag);
    }

    @Override
    public void deleteTag(Long id) {
        tageRepository.deleteById(id);
    }

    @Override
    public Tag findByName(String name) {
        return tageRepository.findByName(name);
    }

    @Override
    public List<Tag> listTage() {
        return tageRepository.findAll();
    }

    @Override
    public List<Tag> listTage(String tagIds) {
        if (tagIds.equals("") || tagIds == null) {
            return tageRepository.findAllById(new ArrayList<>());
        }

        long[] longs = Arrays.stream(tagIds.split(",")).mapToLong(s -> Long.parseLong(s)).toArray();
        List<Long> collect = Arrays.stream(longs).boxed().collect(Collectors.toList());
        return tageRepository.findAllById(collect);
    }

    @Override
    public List<Tag> listPage(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tageRepository.listPage(pageable);
    }
}
