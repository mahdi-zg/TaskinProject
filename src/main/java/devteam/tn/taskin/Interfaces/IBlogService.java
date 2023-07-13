package devteam.tn.taskin.Interfaces;

import devteam.tn.taskin.Entities.Blog;
import devteam.tn.taskin.Entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBlogService {
    Blog addBlog(String title, String content, MultipartFile image, User userBlog) throws IOException;

    Blog getBlogById(Long id);

    List<Blog> getAllBlogs();

    Blog updateBlog(Long id, Blog updatedBlog);

    void deleteBlog(Long id);
}
