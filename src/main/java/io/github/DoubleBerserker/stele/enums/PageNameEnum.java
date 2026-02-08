package io.github.DoubleBerserker.stele.enums;

public enum PageNameEnum {

    BASE("base"),

    // Navigation bar pages
    HOMEPAGE("homepage"),
    POSTS_MAIN_PAGE("allPosts"),
    POST("post"),
    ABOUT("about"),
    CREATE_POST("createPost");

    public final String value;

    PageNameEnum(String value) {
        this.value = value;
    }

}
