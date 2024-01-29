package com.postech.catalog.infrastructure.video.persistence;

import com.postech.catalog.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

    @Query("""
            select distinct new com.postech.catalog.domain.video.VideoPreview(
                  v.id,
                  v.title,
                  v.description,
                  v.url,
                  v.clickCount,
                  v.createdAt
            )
            from Video v
                left join v.categories categories
            where
                ( :terms is null or UPPER(v.title) like :terms )
            and
                ( :categories is null or categories.id.categoryId in :categories )
            """)
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("categories") Set<String> categories,
            Pageable page
    );

    @Query(value = "select c.id from Video c where c.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);

    @Query(value = "select count(v) from Video v")
    long totalOfVideos();

    @Query(value = "SELECT AVG(v.clickCount) from Video v")
    Double averageClickCount();

    @Modifying
    @Query(value = "Delete from UserVideo uv where uv.id.videoId = :videoId")
    void deleteVideoFromUserVideo(@Param("videoId") String videoId);
}
