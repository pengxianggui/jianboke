package com.jianboke.pub.controller;

import com.jianboke.domain.Chapter;
import com.jianboke.mapper.ChapterMapper;
import com.jianboke.model.ChapterModel;
import com.jianboke.repository.ChapterRepository;
import com.jianboke.web.ChapterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pengxg on 2017/8/9.
 */
@RestController
@RequestMapping(value = "/pub")
public class PubChapterController {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterMapper chapterMapper;

    private static final Logger log = LoggerFactory.getLogger(PubChapterController.class);

    @RequestMapping(value = "/chapter/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChapterModel get(@PathVariable("id") Long id) {
        Chapter c = chapterRepository.findOne(id);
        return chapterMapper.entityToModel(c);
    }
}
