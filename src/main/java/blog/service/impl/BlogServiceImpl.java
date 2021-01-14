package blog.service.impl;

import blog.dao.BlogRepository;
import blog.entity.Blog;
import blog.entity.Type;
import blog.service.BlogService;
import blog.util.MyBeanUtils;
import blog.view.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> {
            //处理条件的动态组合 criteriaQuery 条件容器 criteriaBuilder设置具体的表达式
            List<Predicate> predicateList = new ArrayList<>();
            if (!"".equals(blog.getTitle()) && blog.getTitle()!=null) {
                Predicate title = cb.like(root.get("title"), blog.getTitle());
                predicateList.add(title);
            }
            if (blog.getTypeId()!=null) {
                Predicate type = cb.equal(root.<Type>get("type").get("id"), blog.getTypeId());
                predicateList.add(type);
            }
            //是否推荐 false
            if (blog.isRecommend()) {
                Predicate recommend = cb.equal(root.<Boolean>get("recommend"), blog.isRecommend());
                predicateList.add(recommend);
            }
            cq.where(predicateList.toArray(new Predicate[predicateList.size()]));
            return null;
        },pageable);
    }
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId()==null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) throws NotFoundException {
        Blog b = blogRepository.getOne(id);
        if (b==null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        return blogRepository.save(b);
    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.listBolgTop(pageable);
    }
}
