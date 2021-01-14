package blog.controller;

import blog.entity.Blog;
import blog.service.BlogService;
import blog.service.TagService;
import blog.service.TypeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
public class IndexController {

    @Resource
    BlogService blogService;

    @Resource
    TypeService typeService;

    @Resource
    TagService tagService;
    /*
     * 进入列表页面
     * */
    @GetMapping("/")
    public ModelAndView blogs(@PageableDefault(size=10,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                      Pageable pageable,  Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listPageType(10));
        model.addAttribute("tags",tagService.listPage(6));
        model.addAttribute("recommendBlogs",blogService.listBlogTop(8));
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    /*
     * 进入博客详情页面
     * */
    @GetMapping("/blog/{id}")
    public ModelAndView editInput(@PathVariable Long id, Model model){
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        ModelAndView modelAndView = new ModelAndView("blogDetail");
        return modelAndView;
    }
}
