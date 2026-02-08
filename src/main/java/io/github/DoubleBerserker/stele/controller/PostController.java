package io.github.DoubleBerserker.stele.controller;

import io.github.DoubleBerserker.stele.dto.NewPost;
import io.github.DoubleBerserker.stele.dto.PostResponseDto;
import io.github.DoubleBerserker.stele.entities.Post;
import io.github.DoubleBerserker.stele.enums.PageNameEnum;
import io.github.DoubleBerserker.stele.services.PostService;
import io.github.DoubleBerserker.stele.utils.ModelAttributeHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String getPostsMainPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int numberOfPosts,
            Model model) {
        final String pageHeadTitle = "All Posts";

        // Get latest "numberOfPosts" posts to display on /posts page
        Pageable pageable = PageRequest.of(page, numberOfPosts, Sort.by("createdAt").descending());
        Page<PostResponseDto> posts = postService.getLatestPostsByOffset(pageable);

        model.addAttribute("posts", posts);

        ModelAttributeHelper.addPageAttributes(model, pageHeadTitle, PageNameEnum.POSTS_MAIN_PAGE.value);
        return PageNameEnum.BASE.value;
    }

    @GetMapping("/posts/{id}")
    public String getPostPage(Model model, @PathVariable String id) {
        PostResponseDto post = postService.getPostById(id);
        model.addAttribute("post", post);

        ModelAttributeHelper.addPageAttributes(model, post.title(), PageNameEnum.POST.value);
        return PageNameEnum.BASE.value;
    }

    @GetMapping("/posts/create")
    public String createNewPost(Model model) {
        final String pageHeadTitle = "Create Post";
        NewPost newPost = new NewPost();
        model.addAttribute("post", newPost);

        ModelAttributeHelper.addPageAttributes(model, pageHeadTitle, PageNameEnum.CREATE_POST.value);
        return PageNameEnum.BASE.value;
    }

    @PostMapping("/posts/save")
    public String uploadPost(@ModelAttribute NewPost newpost) {
        System.out.println(newpost);
        return "redirect:" + PageNameEnum.BASE.value;
    }

}
