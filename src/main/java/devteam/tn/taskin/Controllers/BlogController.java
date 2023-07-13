package devteam.tn.taskin.Controllers;

import devteam.tn.taskin.Entities.Blog;
import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Interfaces.IBlogService;
import devteam.tn.taskin.Interfaces.IUserService;
import devteam.tn.taskin.Services.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final IBlogService iBlogService;
    private final IUserService iUserService;

    public BlogController(IBlogService iBlogService, IUserService iUserService) {
        this.iBlogService = iBlogService;
        this.iUserService = iUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<Blog> addBlog(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam(value = "image", required = false) MultipartFile image,
                                        @RequestParam("userId") Long userId) {
        // Vérifier si l'utilisateur existe
        User userBlog = iUserService.getUserById(userId);
        if (userBlog == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Appeler le service pour ajouter le blog
            Blog createdBlog = iBlogService.addBlog(title, content, image, userBlog);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable("id") Long id) {
        Blog blog = iBlogService.getBlogById(id);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = iBlogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable("id") Long id, @RequestBody Blog updatedBlog) {
        Blog blog = iBlogService.updateBlog(id, updatedBlog);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") Long id) {
        iBlogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
}

