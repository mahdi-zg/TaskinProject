package devteam.tn.taskin.Services;

import devteam.tn.taskin.Entities.Blog;
import devteam.tn.taskin.Entities.User;
import devteam.tn.taskin.Interfaces.IBlogService;
import devteam.tn.taskin.Repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class BlogService implements IBlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }
    private static final String FILE_DIRECTORY = "C:/Users/moham/OneDrive/Bureau/t123/taskin/files";

    @Override
    public Blog addBlog(String title, String content, MultipartFile image, User userBlog) throws IOException {
        Blog blog = new Blog();
        // Définir les autres attributs du blog
        blog.setTitle(title);
        blog.setContent(content);

        // Enregistrer l'image du blog
        if (image != null && !image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            Path blogDirectoryPath = createDirectory(FILE_DIRECTORY + "/blog");
            Path filePath = blogDirectoryPath.resolve(fileName);
            saveImageForBlog(filePath, image);
            blog.setImageBlog(filePath.toString());

            // Récupérer l'ID du blog créé
            Long blogId = blog.getIdBlog();

            // Mettre à jour le nom de l'image avec l'ID du blog
            if (blogId != null) {
                String newFileName = "blog_" + blogId + "." + StringUtils.getFilenameExtension(fileName);
                Path newFilePath = blogDirectoryPath.resolve(newFileName);
                Files.move(filePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                blog.setImageBlog(newFilePath.toString());
            }
        }

        // Définir l'utilisateur associé au blog
        blog.setUserBlog(userBlog);

        // Enregistrer le blog dans la base de données
        Blog savedBlog = blogRepository.save(blog);

        return savedBlog;
    }

    private void saveImageForBlog(Path filePath, MultipartFile image) throws IOException {
        // Créer le dossier parent s'il n'existe pas
        Path parentPath = filePath.getParent();
        if (!Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
        }

        // Enregistrer l'image dans le dossier spécifié
        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Image enregistrée pour le blog : " + filePath);

        // Chemin absolu du dossier principal du blog
        Path absoluteBlogDirectoryPath = parentPath.toAbsolutePath();
        System.out.println("Chemin absolu du dossier principal du blog : " + absoluteBlogDirectoryPath);
    }



    private Path createDirectory(String directoryPath) throws IOException {
        // Créer le dossier s'il n'existe pas
        Path path = Paths.get(directoryPath).toAbsolutePath().normalize();

        // Vérifier si le dossier est "blog"
        if (directoryPath.endsWith("/blog")) {
            Path parentPath = path.getParent();
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
                System.out.println("Dossier créé : " + parentPath);
            }
        }

        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Dossier créé : " + path);
        }

        return path;
    }


    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public Blog updateBlog(Long id, Blog updatedBlog) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog != null) {
            blog.setTitle(updatedBlog.getTitle());
            blog.setContent(updatedBlog.getContent());
            blog.setImageBlog(updatedBlog.getImageBlog());
            return blogRepository.save(blog);
        }
        return null;
    }



    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
