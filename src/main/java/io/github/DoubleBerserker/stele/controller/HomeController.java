package io.github.DoubleBerserker.stele.controller;

import io.github.DoubleBerserker.stele.dto.PostSummaryDto;
import io.github.DoubleBerserker.stele.enums.PageNameEnum;
import io.github.DoubleBerserker.stele.services.PostService;
import io.github.DoubleBerserker.stele.utils.ModelAttributeHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping({"/", "/home"})
    public String getHomepage(Model model) {

        final int NUMBER_OF_LATEST_POSTS_TO_SHOW = 2;
        final String pageHeadTitle = "Home";

        List<PostSummaryDto> latestPostSummaries = postService.getLatestPostsSummarized(NUMBER_OF_LATEST_POSTS_TO_SHOW);
        model.addAttribute("latestPosts", latestPostSummaries);

        ModelAttributeHelper.addPageAttributes(model, pageHeadTitle, PageNameEnum.HOMEPAGE.value);
        return PageNameEnum.BASE.value;
    }

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        String pageHeadTitle = "About";

        ModelAttributeHelper.addPageAttributes(model, pageHeadTitle, PageNameEnum.ABOUT.value);
        return PageNameEnum.BASE.value;
    }

}
