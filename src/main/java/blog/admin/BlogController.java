package blog.admin;

import blog.entity.Blog;
import blog.entity.User;
import blog.service.BlogService;
import blog.service.TagService;
import blog.service.TypeService;
import blog.view.BlogQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blogs-publish";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Resource
    BlogService blogService;

    @Resource
    TypeService typeService;

    @Resource
    TagService tagService;

    /*
    * 进入列表页面
    * */
    @GetMapping("/blogs")
    public ModelAndView blogs(@PageableDefault(size=2,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                          Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        model.addAttribute("types",typeService.listType());
        ModelAndView modelAndView = new ModelAndView(LIST);
        return modelAndView;
    }

   @PostMapping("/blogs/search")
   public ModelAndView search(@PageableDefault(size=2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
       model.addAttribute("page",blogService.listBlog(pageable,blog));
       ModelAndView modelAndView = new ModelAndView("admin/blogs :: blogList");
       return modelAndView;
   }

   @GetMapping("/blogs/input")
   public ModelAndView input(Model model){
       model.addAttribute("blog",new Blog());
       model.addAttribute("types",typeService.listType());
       model.addAttribute("tags",tagService.listTage());
       ModelAndView modelAndView = new ModelAndView(INPUT);
       return modelAndView;
   }

   @PostMapping("/blogs")
    public ModelAndView post(Blog blog, HttpSession session, RedirectAttributes attributes){
       blog.setUser((User) session.getAttribute("user"));
       blog.setType(typeService.getType(blog.getType().getId()));
       blog.setTags(tagService.listTage(blog.getTagIds()));
       Blog b = blogService.saveBlog(blog);
       if (b==null) {
           attributes.addFlashAttribute("message","很遗憾，操作失败");
       }else {
           attributes.addFlashAttribute("message","恭喜，操作成功");
       }
       ModelAndView modelAndView = new ModelAndView(REDIRECT_LIST);
       return modelAndView;
   }
}
